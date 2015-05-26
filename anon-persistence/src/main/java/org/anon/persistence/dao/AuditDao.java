package org.anon.persistence.dao;

import java.util.List;

import org.anon.data.ReductionMethod;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.audit.ExecutionColumnData;
import org.anon.persistence.data.audit.ExecutionData;
import org.anon.persistence.data.audit.ExecutionMethodData;
import org.anon.persistence.data.audit.ReductionExecutionData;
import org.anon.persistence.data.audit.RefTableReductionExecutionData;

public interface AuditDao {

	void save(ExecutionData execution);

	void save(ExecutionMethodData executionMethodData);

	void save(ExecutionColumnData executionColumnData);

	void merge(ExecutionData executionData);

	List<ExecutionData> loadExecutionDatas();

	List<ExecutionMethodData> loadExecutionMethodDatas(ExecutionData executionData);

	List<ExecutionColumnData> loadExecutionColumnDatas(ExecutionMethodData executionMethodData);

	ExecutionColumnData getLastExecutionColumnData(AnonymisedColumnData anonymisedColumnData);

	void save(ReductionExecutionData reductionExecutionData);

	void save(RefTableReductionExecutionData data);

	ReductionExecutionData loadReductionExecutionData(ReductionMethod reductionMethod, ExecutionData executionData);

	ReductionExecutionData loadLastReductionExecutionData(ReductionMethod reductionMethod);

}
