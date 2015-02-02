package org.anon.persistence.dao;

import java.util.List;

import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.audit.ExecutionColumnData;
import org.anon.persistence.data.audit.ExecutionData;
import org.anon.persistence.data.audit.ExecutionMethodData;

public interface AuditDao {

	void save(ExecutionData execution);

	void save(ExecutionMethodData executionMethodData);

	void save(ExecutionColumnData executionColumnData);

	void merge(ExecutionData executionData);

	List<ExecutionData> loadExecutionDatas();

	List<ExecutionMethodData> loadExecutionMethodDatas(ExecutionData executionData);

	List<ExecutionColumnData> loadExecutionColumnDatas(ExecutionMethodData executionMethodData);

	ExecutionColumnData getLastExecutionColumnData(AnonymisedColumnData anonymisedColumnData);

}
