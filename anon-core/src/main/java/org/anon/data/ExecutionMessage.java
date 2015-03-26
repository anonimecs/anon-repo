package org.anon.data;

import java.util.Date;

public class ExecutionMessage {

	private Date createDate;
	private String message;
	private Integer rowCount;
	public ExecutionMessage(String message, Integer rowCount) {
		super();
		this.message = message;
		this.rowCount = rowCount;
		this.createDate = new Date();
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	@Override
	public String toString() {
		if(rowCount != null){
			return message + " " + rowCount;
		}
		else {
			return message;
		}
	}
	
	
}
