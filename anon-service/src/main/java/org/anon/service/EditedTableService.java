package org.anon.service;

import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.service.EditedTableServiceImpl.RowFilterTestResult;
import org.anon.service.where.WhereConditionBuilder.Applicability;

public interface EditedTableService {

	void addAnonymisation(DatabaseTableInfo editedTable,
			AnonymisedColumnInfo anonymizedColumn,
			List<RelatedTableColumnInfo> selectedRelatedTableColumns,
			AnonymisationMethod anonymisationMethod);

	void removeAnonymisation(DatabaseTableInfo selectedEditedTable,
			AnonymisedColumnInfo selectedAnonymizedColumn,
			List<RelatedTableColumnInfo> relatedTableColumnsToRemove);
	
	void changeAnonymisation(DatabaseTableInfo selectedEditedTable,
			AnonymisedColumnInfo selectedAnonymizedColumn,
			List<RelatedTableColumnInfo> selectedRelatedTableColumns,
			AnonymisationMethod anonymisationMethod);

	void saveRowFilter(AnonymisedColumnInfo col, String whereCondition, Applicability applicability,
			List<RelatedTableColumnInfo> relatedTableColumnInfos, AnonymisationMethod anonymisationMethod);

	void deleteRowFilter(AnonymisedColumnInfo anonymisedColumnInfo,
			List<RelatedTableColumnInfo> selectedRelatedTableColumns, AnonymisationMethod anonymisationMethod);

	RowFilterTestResult testRowFilter(AnonymisedColumnInfo col, String whereCondition,
			Applicability applicability, List<RelatedTableColumnInfo> relatedTableColumnInfos,
			AnonymisationMethod anonymisationMethod);

	
	
}
