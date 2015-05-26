package org.anon.exec.mock;

import org.anon.data.MethodExecution;
import org.anon.data.ReductionMethod;
import org.anon.data.ReductionMethodReferencingTable;
import org.anon.exec.audit.ExecAuditor;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.audit.ExecutionData;
import org.anon.persistence.data.audit.ReductionExecutionData;

public class ExecAuditorMock implements ExecAuditor{

	@Override
	public ExecutionData insertExecution(String description, String username, DatabaseConfig databaseConfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executionFinished(ExecutionData executionData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executionFailed(ExecutionData executionData, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveMethodExecution(ExecutionData executionData, MethodExecution methodExecution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ReductionExecutionData auditReduction(ExecutionData executionData, ReductionMethod reductionMethod,
			int rowCount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void auditRefTableReduction(ReductionExecutionData reductionExecutionData,
			ReductionMethodReferencingTable referencingTable, int rowCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ReductionExecutionData loadReductionExecutionData(ExecutionData executionData,
			ReductionMethod reductionMethod) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReductionExecutionData loadLastReductionExecutionData(ReductionMethod reductionMethod) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
