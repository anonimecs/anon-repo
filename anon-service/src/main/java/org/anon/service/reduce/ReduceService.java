package org.anon.service.reduce;

import java.util.List;

import org.anon.data.DatabaseTableInfo;
import org.anon.data.ReductionMethod;
import org.anon.data.ReductionType;
import org.anon.persistence.data.ReductionMethodData;

public interface ReduceService {

	ReduceTestResult test(DatabaseTableInfo editedTable, String guiWhereCondition, ReductionType reductionType, List<RelatedTable> selectedRelatedTables);

	ReductionMethod save(DatabaseTableInfo editedTable, String guiWhereCondition, ReductionType reductionType, List<RelatedTable> selectedRelatedTables);

	void delete(ReductionMethod reductionMethodData);

	List<RelatedTable> findRelatedTablesForReduce(DatabaseTableInfo editedTable);

	List<RelatedTable> extractRelatedTablesForReduce(DatabaseTableInfo editedTable, ReductionMethod reductionMethodData);

	List<ReductionMethodData> loadPersistedReductions();

}
