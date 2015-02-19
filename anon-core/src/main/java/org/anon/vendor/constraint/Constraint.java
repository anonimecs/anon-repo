package org.anon.vendor.constraint;

import java.util.Arrays;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;



public abstract class Constraint {

	private boolean active = true;
	private String message = null;
	
	protected String constraintName;
	protected String sourceTableName;
	protected String targetTableName;

	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	@Override
	public String toString() {
		
		try {
			return getClass().getSimpleName() + " FK:" + constraintName + " " + sourceTableName 
					+ Arrays.toString(getSourceColumnNames())  + " -> "  +targetTableName + Arrays.toString(getTargetColumnNames());
		} catch (Exception e) {
			return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
		}
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean isMultiColumnReferentialConstraint(){
		return getSourceColumnNames().length > 1;
	}

	final public String getSourceTableName(){
		return sourceTableName;
	}
	
	final public String getConstraintName() {
		return constraintName;
	}
	
	final public String getTargetTableName() {
		return targetTableName;
	}

	public boolean containsTargetColumn(String columnName) {
		return Arrays.asList(getTargetColumnNames()).contains(columnName);
	}

	public boolean containsSourceColumn(String columnName) {
		return Arrays.asList(getSourceColumnNames()).contains(columnName);
	}

	
	abstract public String createActivateSql();
	abstract public String createDeactivateSql();
	
	abstract public String[] getSourceColumnNames();
	abstract public String[] getTargetColumnNames();
}
