package org.anon.exec;

import java.util.List;

import javax.sql.DataSource;

import org.anon.AbstractDbConnection;
import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.ExecutionMessage;
import org.anon.data.MethodExecution;
import org.anon.exec.audit.ExecAuditor;
import org.anon.license.LicenseException;
import org.anon.license.LicenseManager;
import org.anon.logic.AnonymisationMethod;
import org.anon.service.DbConnectionFactory;
import org.anon.vendor.constraint.referential.ForeignKeyConstraint;
import org.anon.vendor.constraint.referential.ForeignKeyConstraintManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BaseExec {
	
	protected Logger logger = Logger.getLogger(getClass());
	
	protected DataSource dataSource;
	
	@Autowired
	@Qualifier("execConfig")
	protected AnonConfig execConfig;

	@Autowired
	protected LicenseManager licenseManager;
	
	@Autowired
	protected ExecAuditor execAuditor;
	
	@Autowired
	protected GuiNotifier guiNotifier;
	
	@Autowired
	protected DbConnectionFactory dbConnectionFactory;
	
	private String userName;
	
	int tablesAnonimised;
	
	public void runAll() {
		tablesAnonimised = 0; 
		
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
		
		logger.debug("Database " + connection.getProperties());
		this.setDataSource(connection.getDataSource());
		
		anonymisationMethod.setDataSource(dataSource);
		MethodExecution methodExecution = execConfig.getMethodExecution(anonymisationMethod);
		try{
			methodExecution.started();
			anonymisationMethod.setupInDb();
			for (AnonymisedColumnInfo col : anonymisationMethod.getApplyedToColumns()) {
				assertFreeEditionRunCount();
				methodExecution.startedCol(col);
				addMessage(methodExecution, col, new ExecutionMessage("Deacivating constraints", null));
				ForeignKeyConstraintManager constraintManager = getConstraintManager(dataSource);
				List<? extends ForeignKeyConstraint> deactivatedContstraints = constraintManager.deactivateConstraints(col.getTable().getName(), col.getName(), col.getTable().getSchema());
				addMessage(methodExecution, col, new ExecutionMessage("Deacivated constraints", deactivatedContstraints.size()));

				ExecutionMessage runResult;
				try{
					addMessage(methodExecution, col, new ExecutionMessage("Anonymising rows", col.getTable().getRowCount()));
					runResult = anonymisationMethod.runOnColumn(col);
				}
				finally{
					addMessage(methodExecution, col, new ExecutionMessage("Reacivating constraints", deactivatedContstraints.size()));
					constraintManager.activateConstraints(deactivatedContstraints);
					showConstaintProblems(col, methodExecution, deactivatedContstraints);
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
		methodExecution.addMessage(col, executionMessage);
		guiNotifier.refreshExecGui(executionMessage.toString());
		
	}
	
	private void showConstaintProblems(AnonymisedColumnInfo col, MethodExecution methodExecution,List<? extends ForeignKeyConstraint> deactivatedContstraints) {
		for (ForeignKeyConstraint constraint : deactivatedContstraints) {
			if(! constraint.isActive()){
				methodExecution.addMessage(col, new ExecutionMessage(constraint.getMessage(), null));
			}
		}
	}

	private void assertFreeEditionRunCount() {
		if(licenseManager.reachedMaxTablesAnonimised(tablesAnonimised)){
			throw new LicenseException("Reached maximal number of tables " + licenseManager.getMaxTablesAnonimised());
		}
		tablesAnonimised++;
	}

	
	protected abstract ForeignKeyConstraintManager getConstraintManager(DataSource dataSource);
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setExecConfig(AnonConfig execConfig) {
		this.execConfig = execConfig;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
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
