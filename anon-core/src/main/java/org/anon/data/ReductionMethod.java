package org.anon.data;

import java.util.List;

public interface ReductionMethod extends ReductionMethodDefinition{

	public abstract List<? extends ReductionMethodReferencingTable> getReferencingTableDatas();

	public abstract Long getDatabaseConfigId();


}
