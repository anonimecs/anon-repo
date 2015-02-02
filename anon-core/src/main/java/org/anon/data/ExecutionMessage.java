package org.anon.data;

public class ExecutionMessage {

	private String message;
	private Integer rowCount;
	public ExecutionMessage(String message, Integer rowCount) {
		super();
		this.message = message;
		this.rowCount = rowCount;
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
