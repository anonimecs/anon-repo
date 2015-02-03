package org.anon.exec.audit;

import org.anon.data.MethodExecution;

public interface ExecAuditor {

	void insertExecution(String description, String username);

	void executionFinished();

	void executionFailed(String message);

	void saveMethodExecution(MethodExecution methodExecution);

}