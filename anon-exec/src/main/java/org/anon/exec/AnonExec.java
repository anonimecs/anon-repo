package org.anon.exec;

import java.util.List;

import org.anon.AbstractDbConnection;
import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.ExecutionMessage;
import org.anon.data.MethodExecution;
import org.anon.exec.audit.ExecAuditor;
import org.anon.license.LicenseManager;
import org.anon.logic.AnonymisationMethod;
import org.anon.service.DbConnectionFactory;
import org.anon.vendor.constraint.ColumnConstraintBundle;
import org.anon.vendor.constraint.Constraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AnonExec extends AbstractExec{
	
	
	@Autowired
	@Qualifier("execConfig")
	protected AnonConfig execConfig;
	
	
	
	@Override
	public void runAll() {
		tablesProcessed = 0; 
		
		try {
			execAuditor.insertExecution("Run All", userName,dbConnectionFactory.getDatabaseConfig());
			for (AnonymisationMethod anonymisationMethod : execConfig.getAnonMethods()) {
					do_run(anonymisationMethod);
			}
			execAuditor.executionFinished();
		} catch (RuntimeException e){
			execAuditor.executionFailed(e.getMessage());
			throw e;
		}
	}

	public void run(AnonymisationMethod anonymisationMethod) {
		try {
			execAuditor.insertExecution("Run Method Only", userName,dbConnectionFactory.getDatabaseConfig());
			do_run(anonymisationMethod);
			execAuditor.executionFinished();
		} catch (RuntimeException e){
			execAuditor.executionFailed(e.getMessage());
			throw e;
		}
	}
	
	protected void do_run(AnonymisationMethod anonymisationMethod) {
		licenseManager.checkLicenseExpired();
		
		AbstractDbConnection connection = 
				dbConnectionFactory.getConnectionForSchema(getExecSchema(anonymisationMethod));
		
		logger.debug("Database " + connection.getDefaultSchema());
		this.setDataSource(connection.getDataSource());
		
		anonymisationMethod.setDataSource(dataSource);
		MethodExecution methodExecution = execConfig.getMethodExecution(anonymisationMethod);
		try{
			methodExecution.started();
			anonymisationMethod.setSchema(connection.getDefaultSchema());
			anonymisationMethod.setupInDb();
			for (AnonymisedColumnInfo col : anonymisationMethod.getApplyedToColumnsInExecutionOrder()) {
				assertFreeEditionAnonTables();
				methodExecution.startedCol(col);
				
				ColumnConstraintBundle constraintBundle = constraintBundleFactory.createConstraintBundle(getDatabaseSpecifics() , col, dataSource);
				addMessage(methodExecution, col, new ExecutionMessage("Deacivating constraints", null));
				List<Constraint> deactivatedConstraints = constraintBundle.deactivate();
				
				if(deactivatedConstraints.isEmpty()){
					addMessage(methodExecution, col, new ExecutionMessage("No relevant constraints found", null));
				}
				else {
					addMessage(methodExecution, col, new ExecutionMessage("Deacivated constraints " + deactivatedConstraints, deactivatedConstraints.size()));
				}

				ExecutionMessage runResult;
				try{
					addMessage(methodExecution, col, new ExecutionMessage("Anonymising rows", col.getTable().getRowCount()));
					runResult = anonymisationMethod.runOnColumn(col);
				}
				finally{
					if(!deactivatedConstraints.isEmpty()){
						addMessage(methodExecution, col, new ExecutionMessage("Reacivating constraints", deactivatedConstraints.size()));
						constraintBundle.activate();
						showConstaintProblems(col, methodExecution, deactivatedConstraints);
					}
				}
				methodExecution.finishedCol(col,runResult);
					
			}
			methodExecution.finished();
		}
		catch(RuntimeException e){		
			logger.debug("anonymisationMethod failed : " + anonymisationMethod, e);
			methodExecution.failed(e);
			throw e;				
		}
		finally{
			anonymisationMethod.cleanupInDb();
			execAuditor.saveMethodExecution(methodExecution);
		}
	}
	

	private String getExecSchema(AnonymisationMethod anonymisationMethod) {
		String schema = null;
		for(AnonymisedColumnInfo col : anonymisationMethod.getApplyedToColumns()) {		
			if(schema==null) {
				schema = col.getTable().getSchema();
			}
			else if(!schema.equals(col.getTable().getSchema())) {
				throw new RuntimeException("multiple schema not allowed");
			}
		}
		return schema;
	}	
	
	private void addMessage(MethodExecution methodExecution, AnonymisedColumnInfo col, ExecutionMessage executionMessage) {
		logger.debug("Action on " + col + " : " + executionMessage);
		methodExecution.addMessage(col, executionMessage);
		guiNotifier.refreshExecGui(executionMessage.toString());
		
	}
	
	private void showConstaintProblems(AnonymisedColumnInfo col, MethodExecution methodExecution,List<Constraint> deactivatedContstraints) {
		for (Constraint constraint : deactivatedContstraints) {
			if(! constraint.isActive()){
				methodExecution.addMessage(col, new ExecutionMessage(constraint.getMessage(), null));
			}
		}
	}


	

	public void setExecConfig(AnonConfig execConfig) {
		this.execConfig = execConfig;
	}


	public void setLicenseManager(LicenseManager licenseManager) {
		this.licenseManager = licenseManager;
	}

	public void setDbConnectionFactory(DbConnectionFactory dbConnectionFactory) {
		this.dbConnectionFactory = dbConnectionFactory;
	}
	public void setExecAuditor(ExecAuditor execAuditor) {
		this.execAuditor = execAuditor;
	}
	
	public void setGuiNotifier(GuiNotifier guiNotifier) {
		this.guiNotifier = guiNotifier;
	}
}
