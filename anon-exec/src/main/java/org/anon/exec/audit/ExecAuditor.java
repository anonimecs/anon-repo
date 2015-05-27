package org.anon.exec.audit;

import org.anon.data.MethodExecution;
import org.anon.data.MethodExecution.Status;
import org.anon.data.ReductionMethod;
import org.anon.data.ReductionMethodReferencingTable;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.audit.ExecutionData;
import org.anon.persistence.data.audit.ReductionExecutionData;
import org.anon.persistence.data.audit.RefTableReductionExecutionData;

public interface ExecAuditor {

	ExecutionData insertExecution(String description, String username, DatabaseConfig databaseConfig);

	void executionFinished(ExecutionData executionData);

	void executionFailed(ExecutionData executionData, String message);

	void saveMethodExecution(ExecutionData executionData, MethodExecution methodExecution);

	ReductionExecutionData auditReduction(ExecutionData executionData, ReductionMethod reductionMethod, String resultText, Status status);

	void auditRefTableReduction(ReductionExecutionData reductionExecutionData,
			ReductionMethodReferencingTable referencingTable, String resultText, Status status);

	ReductionExecutionData loadReductionExecutionData(ExecutionData executionData, ReductionMethod reductionMethod);

	ReductionExecutionData loadLastReductionExecutionData(ReductionMethod reductionMethod);

	RefTableReductionExecutionData loadRefTableReductionExecutionData(ExecutionData executionData,
			ReductionMethod reductionMethod, ReductionMethodReferencingTable reductionMethodReferencingTable);

	RefTableReductionExecutionData loadLastRefTableReductionExecutionData(ReductionMethod reductionMethod,
			ReductionMethodReferencingTable reductionMethodReferencingTable);

}
