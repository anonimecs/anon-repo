package org.anon.exec.audit;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.anon.Utils;
import org.anon.data.ColumnExecution;
import org.anon.data.ExecutionMessage;
import org.anon.data.MethodExecution;
import org.anon.data.MethodExecution.Status;
import org.anon.data.ReductionMethod;
import org.anon.data.ReductionMethodReferencingTable;
import org.anon.persistence.dao.AuditDao;
import org.anon.persistence.dao.EntitiesDao;
import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.ReductionMethodData;
import org.anon.persistence.data.ReductionMethodReferencingTableData;
import org.anon.persistence.data.audit.ExecutionColumnData;
import org.anon.persistence.data.audit.ExecutionData;
import org.anon.persistence.data.audit.ExecutionMessageData;
import org.anon.persistence.data.audit.ExecutionMethodData;
import org.anon.persistence.data.audit.ReductionExecutionData;
import org.anon.persistence.data.audit.RefTableReductionExecutionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecAuditorImpl implements ExecAuditor{

	@Autowired
	protected AuditDao auditDao;
	
	@Autowired
	protected EntitiesDao entitiesDao; 

	ExecutionData executionData;
	
	@PostConstruct
	public void init() {
	}
	
	public void insertExecution(String description, String username, DatabaseConfig databaseConfig) {
		executionData = new ExecutionData();
		executionData.setDescription(description);
		executionData.setStartTime(new Date());
		executionData.setUserName(username);
		executionData.setStatusEnum(Status.RUNNING);
		executionData.setDatabaseConfig(databaseConfig);
		
		auditDao.save(executionData);		
	}

	public void executionFinished() {
		executionData.setEndTime(new Date());
		executionData.setStatusEnum(Status.ANONYMISED);
		auditDao.merge(executionData);		
		
	}

	public void executionFailed(String message) {
		executionData.setEndTime(new Date());
		executionData.setStatusEnum(Status.FAILED);
		executionData.addDescription(message);
		auditDao.merge(executionData);		
	}

	public void saveMethodExecution(MethodExecution methodExecution) {
		ExecutionMethodData executionMethodData  = new ExecutionMethodData();
		AnonymisationMethodData anonymisationMethodData = entitiesDao.loadAnonymisationMethodData(methodExecution.getAnonymisationMethod().getId());
		executionMethodData.setAnonymisationMethodData(anonymisationMethodData);
		executionMethodData.setExecutionData(executionData);
		executionMethodData.setResultText(getResultText(methodExecution));
		Long seconds = Utils.getDiffInSeconds(methodExecution.getStartTime(), methodExecution.getFinishedTime());
		executionMethodData.setRuntimeSec(seconds);
		executionMethodData.setStatusEnum(methodExecution.getStatus());
		
		auditDao.save(executionMethodData);
		
		for(ColumnExecution columnExecution:methodExecution.getColumnExecutions().values()){
			saveExecutionColumnData(executionMethodData, columnExecution, methodExecution);
		}
		
	}

	private void saveExecutionColumnData(ExecutionMethodData executionMethodData, ColumnExecution columnExecution, MethodExecution methodExecution) {
		ExecutionColumnData executionColumnData = new ExecutionColumnData();
		
		AnonymisedColumnData anonymisedColumnData = entitiesDao.loadAnonymisedColumnData(columnExecution.getAnonymisedColumnInfo());
		executionColumnData.setAnonymisedColumnData(anonymisedColumnData);
		executionColumnData.setExecutionMethodData(executionMethodData);
		executionColumnData.setResultText(getResultText(columnExecution, methodExecution));
		executionColumnData.setStatusEnum(columnExecution.getStatus());
		Long seconds = Utils.getDiffInSeconds(columnExecution.getStartTime(), columnExecution.getFinishedTime());
		executionColumnData.setRuntimeSec(seconds);
		
		for(ExecutionMessage executionMessage: columnExecution.getMessages() ){
			ExecutionMessageData executionMessageData = new ExecutionMessageData(executionMessage.toString());
			executionColumnData.addMessage(executionMessageData);
		}
		
		auditDao.save(executionColumnData);
		
	}

	private String getResultText(ColumnExecution columnExecution, MethodExecution methodExecution) {
		if(columnExecution.getStatus() == Status.FAILED){
			return "Error : "+ methodExecution.getLastException().getMessage();
		}else if(!columnExecution.getMessages().isEmpty()){
			return columnExecution.getMessages().getLast().toString();
		}
		return null;
	}

	private String getResultText(MethodExecution methodExecution) {
		if(methodExecution.getStatus() == Status.FAILED){
			return "Error : "+ methodExecution.getLastException().getMessage();
		}
		return null;
		
	}

	@Override
	public ReductionExecutionData auditReduction(ReductionMethod reductionMethod, int rowCount) {
		ReductionExecutionData reductionExecutionData  = new ReductionExecutionData();

		reductionExecutionData.setReductionMethodData((ReductionMethodData)reductionMethod);
		reductionExecutionData.setExecutionData(executionData);
		reductionExecutionData.setResultText("Row Count :" + rowCount);
		reductionExecutionData.setRuntimeSec(0l);
		reductionExecutionData.setStatusEnum(Status.REDUCED);

		auditDao.save(reductionExecutionData);
		
		return reductionExecutionData;
	}

	@Override
	public void auditRefTableReduction(ReductionExecutionData reductionExecutionData, ReductionMethodReferencingTable referencingTable, int rowCount) {
		RefTableReductionExecutionData data = new RefTableReductionExecutionData();

		data.setReductionExecutionData(reductionExecutionData);
		data.setReductionMethodReferencingTableData((ReductionMethodReferencingTableData)referencingTable);
		data.setResultText("Row Count :" + rowCount);
		data.setRuntimeSec(0l);
		data.setStatusEnum(Status.REDUCED);

		auditDao.save(data);
		
		
	}


	
}
