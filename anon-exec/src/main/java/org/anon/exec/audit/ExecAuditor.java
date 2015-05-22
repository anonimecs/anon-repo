package org.anon.exec.audit;

import org.anon.data.MethodExecution;
import org.anon.data.ReductionMethod;
import org.anon.data.ReductionMethodReferencingTable;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.audit.ReductionExecutionData;

public interface ExecAuditor {

	void insertExecution(String description, String username, DatabaseConfig databaseConfig);

	void executionFinished();

	void executionFailed(String message);

	void saveMethodExecution(MethodExecution methodExecution);

	ReductionExecutionData auditReduction(ReductionMethod reductionMethod, int rowCount);

	void auditRefTableReduction(ReductionExecutionData reductionExecutionData,
			ReductionMethodReferencingTable referencingTable, int rowCount);

}
