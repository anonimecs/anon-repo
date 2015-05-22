package org.anon.exec.mock;

import org.anon.data.MethodExecution;
import org.anon.data.ReductionMethod;
import org.anon.data.ReductionMethodReferencingTable;
import org.anon.exec.audit.ExecAuditor;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.audit.ReductionExecutionData;

public class ExecAuditorMock implements ExecAuditor{

	@Override
	public void insertExecution(String description, String user, DatabaseConfig databaseConfig) {

		
	}

	@Override
	public void executionFinished() {

		
	}

	@Override
	public void executionFailed(String message) {

		
	}

	@Override
	public void saveMethodExecution(MethodExecution methodExecution) {

		
	}

	@Override
	public ReductionExecutionData auditReduction(ReductionMethod reductionMethod, int rowCount) {

		return null;
	}

	@Override
	public void auditRefTableReduction(ReductionExecutionData reductionExecutionData,
			ReductionMethodReferencingTable referencingTable, int rowCount) {

		
	}

}
