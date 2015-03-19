package org.anon.exec.audit;

import org.anon.data.MethodExecution;
import org.anon.persistence.data.DatabaseConfig;

public interface ExecAuditor {

	void insertExecution(String description, String username, DatabaseConfig databaseConfig);

	void executionFinished();

	void executionFailed(String message);

	void saveMethodExecution(MethodExecution methodExecution);

}
