package org.anon.exec;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.MethodExecution;
import org.anon.data.RunResult;
import org.anon.logic.AnonymisationMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseExec {
	
	protected Logger logger = Logger.getLogger(getClass());
	
	protected DataSource dataSource;
	
	@Autowired
	protected AnonConfig anonConfig;
	

	
	public void runAll() {

		for (AnonymisationMethod anonymisationMethod : anonConfig.getAnonMethods()) {
			run(anonymisationMethod);
		}
	}

	public void run(AnonymisationMethod anonymisationMethod) {
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
					methodExecution.startedCol(col);
					RunResult runResult = anonymisationMethod.runOnColumn(col);
					methodExecution.finishedColumn(col, runResult);
				}
				methodExecution.finished();
			}
			catch(RuntimeException e){		
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
	
	
	protected abstract List<Constraint> deactivateConstraints(AnonymisationMethod anonymisationMethod);
	protected abstract void activateConstraints(AnonymisationMethod anonymisationMethod, List<Constraint> deactivatedContstraints);
	


	public void setAnonConfig(AnonConfig anonConfig) {
		this.anonConfig = anonConfig;
	}	

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
