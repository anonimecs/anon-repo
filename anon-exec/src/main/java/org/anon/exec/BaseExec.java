package org.anon.exec;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.MethodExecution;
import org.anon.data.RunResult;
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
		// deactivate the foreign keys
		List<Constraint> deactivatedContstraints = null;
		try{
			deactivatedContstraints = deactivateConstraints(anonymisationMethod);

			MethodExecution methodExecution = anonConfig.getMethodExecution(anonymisationMethod);
			try{
				methodExecution.started();
				anonymisationMethod.setupInDb();
				for (AnonymisedColumnInfo col : anonymisationMethod.getApplyedToColumns()) {
					assertFreeEditionRunCount();
					methodExecution.startedCol(col);
					RunResult runResult = anonymisationMethod.runOnColumn(col);
					methodExecution.finishedColumn(col, runResult);
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
		finally{
			// reactivate the constraints
			activateConstraints(anonymisationMethod, deactivatedContstraints);
		}
	}
	
	
	private void assertFreeEditionRunCount() {
		if(licenseManager.reachedMaxTablesAnonimised(tablesAnonimised)){
			throw new LicenseException("Reached maximal number of tables " + licenseManager.getMaxTablesAnonimised());
		}
		tablesAnonimised++;
		
	}

	protected abstract List<Constraint> deactivateConstraints(AnonymisationMethod anonymisationMethod);
	protected abstract void activateConstraints(AnonymisationMethod anonymisationMethod, List<Constraint> deactivatedContstraints);
	


	public void setAnonConfig(AnonConfig anonConfig) {
		this.anonConfig = anonConfig;
	}	

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
