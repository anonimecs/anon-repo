package org.anon.exec;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.MethodExecution;
import org.anon.data.RunMessage;
import org.anon.exec.constraint.Constraint;
import org.anon.exec.constraint.ConstraintManager;
import org.anon.license.LicenseException;
import org.anon.license.LicenseManager;
import org.anon.logic.AnonymisationMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseExec {
	
	protected Logger logger = Logger.getLogger(getClass());
	
	protected DataSource dataSource;
	
	@Autowired
	protected AnonConfig anonConfig;

	@Autowired
	protected LicenseManager licenseManager;
	
	int tablesAnonimised;
	
	public void runAll() {
		tablesAnonimised = 0; 
		
		
		for (AnonymisationMethod anonymisationMethod : anonConfig.getAnonMethods()) {
			run(anonymisationMethod);
		}
	}

	public void run(AnonymisationMethod anonymisationMethod) {
		licenseManager.checkLicenseExpired();
		
		anonymisationMethod.setDataSource(dataSource);
		MethodExecution methodExecution = anonConfig.getMethodExecution(anonymisationMethod);
		try{
			methodExecution.started();
			anonymisationMethod.setupInDb();
			for (AnonymisedColumnInfo col : anonymisationMethod.getApplyedToColumns()) {
				assertFreeEditionRunCount();
				methodExecution.startedCol(col);
				methodExecution.addMessage(col, new RunMessage("Deacivating constraints", null));
				ConstraintManager constraintManager = getConstraintManager(dataSource);
				List<? extends Constraint> deactivatedContstraints = constraintManager.deactivateConstraints(col);
				methodExecution.addMessage(col, new RunMessage("Deacivated constraints", deactivatedContstraints.size()));

				RunMessage runResult;
				try{
					methodExecution.addMessage(col, new RunMessage("Anonymising rows", col.getTable().getRowCount()));
					runResult = anonymisationMethod.runOnColumn(col);
				}
				finally{
					methodExecution.addMessage(col, new RunMessage("Reacivating constraints", deactivatedContstraints.size()));
					constraintManager.activateConstraints(col, deactivatedContstraints);
					showConstaintProblems(col, methodExecution, deactivatedContstraints);
				}
				methodExecution.addMessage(col, runResult);

				
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
		}
	}
	
	
	private void showConstaintProblems(AnonymisedColumnInfo col, MethodExecution methodExecution,List<? extends Constraint> deactivatedContstraints) {
		for (Constraint constraint : deactivatedContstraints) {
			if(! constraint.isActive()){
				methodExecution.addMessage(col, new RunMessage(constraint.getMessage(), null));
			}
		}
		
	}

	private void assertFreeEditionRunCount() {
		if(licenseManager.reachedMaxTablesAnonimised(tablesAnonimised)){
			throw new LicenseException("Reached maximal number of tables " + licenseManager.getMaxTablesAnonimised());
		}
		tablesAnonimised++;
		
	}

	
	protected abstract ConstraintManager getConstraintManager(DataSource dataSource);


	public void setAnonConfig(AnonConfig anonConfig) {
		this.anonConfig = anonConfig;
	}	

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setLicenseManager(LicenseManager licenseManager) {
		this.licenseManager = licenseManager;
	}
}
