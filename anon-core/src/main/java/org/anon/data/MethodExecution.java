package org.anon.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.anon.logic.AnonymisationMethod;
import org.apache.log4j.Logger;

public class MethodExecution {
	protected Logger logger = Logger.getLogger(getClass());
	public enum Status {NOT_RUN, RUNNING, ANONYMISED, FAILED, REDUCED}
	
	private AnonymisationMethod anonymisationMethod;
	private Status status = Status.NOT_RUN;
	private Date startTime, finishedTime;
	private Map<AnonymisedColumnInfo, ColumnExecution> columnExecutions = new HashMap<AnonymisedColumnInfo, ColumnExecution>();
	private Exception lastException;

	public MethodExecution(AnonymisationMethod anonymisationMethod) {
		super();
		this.anonymisationMethod = anonymisationMethod;
	}
	
	public AnonymisationMethod getAnonymisationMethod() {
		return anonymisationMethod;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public Date getFinishedTime() {
		return finishedTime;
	}

	public void started() {
		status = Status.RUNNING;
		startTime = new Date();
		lastException = null;
	}

	public void startedCol(AnonymisedColumnInfo col) {
		logger.debug("Started on col " + col);
		ColumnExecution columnExecution = getColumnExecution(col);
		columnExecution.clear();
		columnExecution.setStartTime(new Date());
		columnExecution.setFinishedTime(new Date());
		columnExecution.setStatus(Status.RUNNING);
		
	}

	public void finishedCol(AnonymisedColumnInfo col, ExecutionMessage executionMessage) {
		ColumnExecution columnExecution = getColumnExecution(col);
		columnExecution.setFinishedTime(new Date());
		columnExecution.add(executionMessage);
		columnExecution.setStatus(Status.ANONYMISED);
		
	}

	public void addMessage(AnonymisedColumnInfo col, ExecutionMessage runResult) {
		getColumnExecution(col).add(runResult);
	}

	public ColumnExecution getColumnExecution(AnonymisedColumnInfo col) {
		ColumnExecution columnExecution = columnExecutions.get(col);
		if(columnExecution == null){
			columnExecution = new ColumnExecution(col);
			columnExecutions.put(col, columnExecution);
		}
		return columnExecution;
	}
	

	public ExecutionMessage getLastMessage(AnonymisedColumnInfo anonymisedColumnInfo) {
		ColumnExecution columnExecution = getColumnExecution(anonymisedColumnInfo);
		if(columnExecution.getMessages().isEmpty()){
			return null;
		}
			
		return columnExecution.getMessages().getLast();
	}

	public void finished() {
		status = Status.ANONYMISED;
		finishedTime = new Date();
		
	}

	public void failed(Exception e) {
		status = Status.FAILED;
		finishedTime = new Date();
		setLastException(e);
		
	}
	
	public Exception getLastException() {
		return lastException;
	}

	public void setLastException(Exception lastException) {
		this.lastException = lastException;
	}
	

	public Map<AnonymisedColumnInfo, ColumnExecution> getColumnExecutions() {
		return columnExecutions;
	}
}
