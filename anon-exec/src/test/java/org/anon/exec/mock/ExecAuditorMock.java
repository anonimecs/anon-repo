package org.anon.exec.mock;

import org.anon.data.MethodExecution;
import org.anon.exec.audit.ExecAuditor;
import org.anon.persistence.data.DatabaseConfig;

public class ExecAuditorMock implements ExecAuditor{

	@Override
	public void insertExecution(String description, String user, DatabaseConfig databaseConfig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executionFinished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executionFailed(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveMethodExecution(MethodExecution methodExecution) {
		// TODO Auto-generated method stub
		
	}

}
