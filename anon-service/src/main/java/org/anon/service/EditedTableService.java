package org.anon.service;

import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.logic.AnonymisationMethod;

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

	
	
}
