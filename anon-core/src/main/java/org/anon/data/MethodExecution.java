package org.anon.data;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.anon.logic.AnonymisationMethod;
import org.apache.log4j.Logger;

public class MethodExecution {
	protected Logger logger = Logger.getLogger(getClass());
	enum Status {NOT_RUN, RUNNING, ANONYMISED, FAILED}
	
	private AnonymisationMethod anonymisationMethod;
	private Status status = Status.NOT_RUN;
	private Date startTime, finishedTime;
	private Map<AnonymisedColumnInfo, LinkedList<RunMessage>> messageMap = new HashMap<AnonymisedColumnInfo, LinkedList<RunMessage>>();
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
		getMessages(col).clear();
		
	}

	public void addMessage(AnonymisedColumnInfo col, RunMessage runResult) {
		List<RunMessage> messages = getMessages(col);
		messages.add(runResult);
	}

	public LinkedList<RunMessage> getMessages(AnonymisedColumnInfo col) {
		LinkedList<RunMessage> messages = messageMap.get(col);
		if(messages == null){
			messages = new LinkedList<>();
			messageMap.put(col, messages);
		}
		return messages;
	}

	public RunMessage getLastMessage(AnonymisedColumnInfo anonymisedColumnInfo) {
		LinkedList<RunMessage> messages = getMessages(anonymisedColumnInfo);
		if(messages.isEmpty()){
			return null;
		}
			
		return messages.getLast();
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
}
