package org.anon.data;

import java.util.List;

public interface ReductionMethod {

	public abstract List<? extends ReductionMethodReferencingTable> getReferencingTableDatas();

	public abstract String getWhereCondition();

	public abstract String getSchemaName();

	public abstract String getTableName();

	public abstract Long getDatabaseConfigId();

	public abstract ReductionType getReductionType();

	public abstract Long getId();

}
