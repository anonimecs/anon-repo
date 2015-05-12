package org.anon.data;

public interface ReductionMethodDefinition {
	public abstract Long getId();
	
	public abstract String getWhereCondition();

	public abstract String getSchemaName();

	public abstract String getTableName();

	public abstract ReductionType getReductionType();

}
