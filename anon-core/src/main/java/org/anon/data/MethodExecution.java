package org.anon.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.anon.logic.AnonymisationMethod;
import org.apache.log4j.Logger;

public class MethodExecution {
	protected Logger logger = Logger.getLogger(getClass());
	enum Status {NOT_RUN, RUNNING, ANONYMISED, FAILED}
	
	private AnonymisationMethod anonymisationMethod;
	private Status status = Status.NOT_RUN;
	private Date startTime, finishedTime;
	private Map<AnonymisedColumnInfo, RunResult> lastRunInfo = new HashMap<AnonymisedColumnInfo, RunResult>();
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
		
	}

	public void finishedColumn(AnonymisedColumnInfo col, RunResult runResult) {
		logger.debug("Finished on col " + col);
		lastRunInfo.put(col, runResult);
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
	
	public RunResult getLastRunInfo(AnonymisedColumnInfo anonymisedColumnInfo) {
		return lastRunInfo.get(anonymisedColumnInfo);
	}

	public Exception getLastException() {
		return lastException;
	}

	public void setLastException(Exception lastException) {
		this.lastException = lastException;
	}
}
