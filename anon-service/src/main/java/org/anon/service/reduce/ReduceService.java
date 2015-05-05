package org.anon.service.reduce;

import java.util.List;

import org.anon.data.DatabaseTableInfo;
import org.anon.data.ReductionType;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.persistence.data.ReductionMethodData;
import org.anon.service.RowFilterTestResult;

public interface ReduceService {

	RowFilterTestResult test(DatabaseTableInfo editedTable, String guiWhereCondition, ReductionType reductionType, List<RelatedTableColumnInfo> selectedRelatedTables);

	ReductionMethodData save(DatabaseTableInfo editedTable, String guiWhereCondition, ReductionType reductionType, List<RelatedTableColumnInfo> selectedRelatedTables);

	void delete(ReductionMethodData reductionMethodData);

}
