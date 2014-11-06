package org.anon.data;

public class RunResult {

	private String message;
	private int rowCount;
	public RunResult(String message, int rowCount) {
		super();
		this.message = message;
		this.rowCount = rowCount;
	}
	
	@Override
	public String toString() {
		return message + " " + rowCount;
	}
	
	
}
