package org.anon.vendor.constraint.referential;

import java.util.Arrays;

import org.anon.vendor.constraint.NamedConstraint;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;



public abstract class ForeignKeyConstraint extends NamedConstraint{

	protected String sourceTableName;
	protected String targetTableName;

	
	
	@Override
	public String toString() {
		
		try {
			return getClass().getSimpleName() + " FK:" + constraintName + " " + sourceTableName 
					+ Arrays.toString(getSourceColumnNames())  + " -> "  +targetTableName + Arrays.toString(getTargetColumnNames());
		} catch (Exception e) {
			return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
		}
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

	
	
	abstract public String[] getSourceColumnNames();
	abstract public String[] getTargetColumnNames();
}
