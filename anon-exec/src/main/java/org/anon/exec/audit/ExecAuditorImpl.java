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

	
	@PostConstruct
	public void init() {
	}
	
	@Override
	public ExecutionData insertExecution(String description, String username, DatabaseConfig databaseConfig) {
		ExecutionData executionData = new ExecutionData();
		executionData.setDescription(description);
		executionData.setStartTime(new Date());
		executionData.setUserName(username);
		executionData.setStatusEnum(Status.RUNNING);
		executionData.setDatabaseConfig(databaseConfig);
		
		auditDao.save(executionData);
		
		return executionData;
	}

	@Override
	public void executionFinished(ExecutionData executionData) {
		executionData.setEndTime(new Date());
		executionData.setStatusEnum(Status.ANONYMISED);
		auditDao.merge(executionData);		
		
	}

	@Override
	public void executionFailed(ExecutionData executionData, String message) {
		executionData.setEndTime(new Date());
		executionData.setStatusEnum(Status.FAILED);
		executionData.addDescription(message);
		auditDao.merge(executionData);		
	}
	
	@Override
	public void saveMethodExecution(ExecutionData executionData, MethodExecution methodExecution) {
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
	public ReductionExecutionData auditReduction(ExecutionData executionData, ReductionMethod reductionMethod, String resultText, Status status) {
		ReductionExecutionData reductionExecutionData  = new ReductionExecutionData();

		reductionExecutionData.setReductionMethodData((ReductionMethodData)reductionMethod);
		reductionExecutionData.setExecutionData(executionData);
		reductionExecutionData.setResultText(resultText);
		reductionExecutionData.setRuntimeSec(0l);
		reductionExecutionData.setStatusEnum(status);

		auditDao.save(reductionExecutionData);
		
		return reductionExecutionData;
	}

	@Override
	public void auditRefTableReduction(ReductionExecutionData reductionExecutionData, ReductionMethodReferencingTable referencingTable, String resultText, Status status) {
		RefTableReductionExecutionData data = new RefTableReductionExecutionData();

		data.setReductionExecutionData(reductionExecutionData);
		data.setReductionMethodReferencingTableData((ReductionMethodReferencingTableData)referencingTable);
		data.setResultText(resultText);
		data.setRuntimeSec(0l);
		data.setStatusEnum(status);

		auditDao.save(data);
		
		
	}

	@Override
	public ReductionExecutionData loadReductionExecutionData(ExecutionData executionData, ReductionMethod reductionMethod) {
		if(executionData == null){
			return null;
		}
		return auditDao.loadReductionExecutionData(reductionMethod, executionData);
	}

	@Override
	public ReductionExecutionData loadLastReductionExecutionData(ReductionMethod reductionMethod) {
		return auditDao.loadLastReductionExecutionData(reductionMethod);
	}

	@Override
	public RefTableReductionExecutionData loadRefTableReductionExecutionData(ExecutionData executionData,
			ReductionMethod reductionMethod, ReductionMethodReferencingTable reductionMethodReferencingTable) {
		return auditDao.loadRefTableReductionExecutionData(reductionMethod, executionData, reductionMethodReferencingTable);
	}

	@Override
	public RefTableReductionExecutionData loadLastRefTableReductionExecutionData(ReductionMethod reductionMethod,
			ReductionMethodReferencingTable reductionMethodReferencingTable) {
		return  auditDao.loadLastRefTableReductionExecutionData(reductionMethod, reductionMethodReferencingTable);
	}


	
}
