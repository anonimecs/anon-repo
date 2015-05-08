package org.anon.data;

public interface ReductionMethodReferencingTable {

	public abstract String getWhereCondition();

	public abstract String getSchemaName();

	public abstract String getTableName();

	public abstract ReductionType getReductionType();

	public abstract ReductionMethod getReductionMethodData();

	public abstract Long getId();

}
