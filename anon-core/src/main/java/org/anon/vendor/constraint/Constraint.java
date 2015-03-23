package org.anon.vendor.constraint;

public abstract class Constraint {

	protected boolean active = true;
	protected String message = null;
	
	protected String constraintName;
	protected String tableName;

	
	abstract public String createActivateSql();
	abstract public String createDeactivateSql();

	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}


}
