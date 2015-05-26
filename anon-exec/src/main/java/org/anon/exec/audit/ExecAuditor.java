package org.anon.exec.audit;

import org.anon.data.MethodExecution;
import org.anon.data.ReductionMethod;
import org.anon.data.ReductionMethodReferencingTable;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.audit.ExecutionData;
import org.anon.persistence.data.audit.ReductionExecutionData;

public interface ExecAuditor {

	ExecutionData insertExecution(String description, String username, DatabaseConfig databaseConfig);

	void executionFinished(ExecutionData executionData);

	void executionFailed(ExecutionData executionData, String message);

	void saveMethodExecution(ExecutionData executionData, MethodExecution methodExecution);

	ReductionExecutionData auditReduction(ExecutionData executionData, ReductionMethod reductionMethod, int rowCount);

	void auditRefTableReduction(ReductionExecutionData reductionExecutionData,
			ReductionMethodReferencingTable referencingTable, int rowCount);

	ReductionExecutionData loadReductionExecutionData(ExecutionData executionData, ReductionMethod reductionMethod);

	ReductionExecutionData loadLastReductionExecutionData(ReductionMethod reductionMethod);

}
