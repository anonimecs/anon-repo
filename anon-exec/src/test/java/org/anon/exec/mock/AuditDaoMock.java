package org.anon.exec.mock;

import java.util.List;

import org.anon.persistence.dao.AuditDao;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.audit.ExecutionColumnData;
import org.anon.persistence.data.audit.ExecutionData;
import org.anon.persistence.data.audit.ExecutionMethodData;

public class AuditDaoMock implements AuditDao {

	@Override
	public void save(ExecutionData execution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(ExecutionMethodData executionMethodData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(ExecutionColumnData executionColumnData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void merge(ExecutionData executionData) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ExecutionData> loadExecutionDatas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExecutionMethodData> loadExecutionMethodDatas(ExecutionData executionData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExecutionColumnData> loadExecutionColumnDatas(ExecutionMethodData executionMethodData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionColumnData getLastExecutionColumnData(AnonymisedColumnData anonymisedColumnData) {
		// TODO Auto-generated method stub
		return null;
	}

}
