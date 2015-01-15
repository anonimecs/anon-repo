package org.anon.data;

public class RunMessage {

	private String message;
	private int rowCount;
	public RunMessage(String message, int rowCount) {
		super();
		this.message = message;
		this.rowCount = rowCount;
	}
	
	@Override
	public String toString() {
		return message + " " + rowCount;
	}
	
	
}
