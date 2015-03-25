package org.anon.vendor.constraint;


public abstract class NamedConstraint extends Constraint{

	protected String constraintName;
	protected String tableName;

	
	abstract public String createActivateSql();
	abstract public String createDeactivateSql();

	

}
