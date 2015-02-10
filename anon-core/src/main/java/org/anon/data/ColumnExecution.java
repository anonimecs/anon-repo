package org.anon.data;

import java.util.Date;
import java.util.LinkedList;

import org.anon.data.MethodExecution.Status;

public class ColumnExecution {

	private LinkedList<ExecutionMessage> messages = new LinkedList<>();
	private Status status = Status.NOT_RUN;
	private Date startTime, finishedTime;
	private AnonymisedColumnInfo anonymisedColumnInfo;
	
	
	public ColumnExecution(AnonymisedColumnInfo anonymisedColumnInfo) {
		super();
		this.anonymisedColumnInfo = anonymisedColumnInfo;
	}
	public LinkedList<ExecutionMessage> getMessages() {
		return messages;
	}
	public void setMessages(LinkedList<ExecutionMessage> messages) {
		this.messages = messages;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getFinishedTime() {
		return finishedTime;
	}
	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}
	public AnonymisedColumnInfo getAnonymisedColumnInfo() {
		return anonymisedColumnInfo;
	}
	public void setAnonymisedColumnInfo(AnonymisedColumnInfo anonymisedColumnInfo) {
		this.anonymisedColumnInfo = anonymisedColumnInfo;
	}
	public void add(ExecutionMessage runMessage) {
		messages.add(runMessage);
		
	}
	public void clear() {
		messages = new LinkedList<>();
		
	}
	
	

}
