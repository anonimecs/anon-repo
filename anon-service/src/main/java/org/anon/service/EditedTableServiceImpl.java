package org.anon.service;

import java.util.LinkedList;
import java.util.List;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodMapping;
import org.anon.logic.map.MappingRule;
import org.anon.persistence.dao.EntitiesDao;
import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisationMethodMappingData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.MappingDefaultData;
import org.anon.persistence.data.MappingRuleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="editedTableService")
public class EditedTableServiceImpl implements EditedTableService{
	
	@Autowired
	protected AnonConfig anonConfig;
	
	@Autowired
	protected EntitiesDao entitiesDao;
	
	@Autowired
	private	DbConnectionFactory dbConnectionFactory;


	public void addAnonymisation(DatabaseTableInfo editedTable,
			AnonymisedColumnInfo anonymizedColumn,
			List<RelatedTableColumnInfo> selectedRelatedTableColumns,
			AnonymisationMethod anonymisationMethod) {
		
		anonymisationMethod.addColumn(anonymizedColumn);
		editedTable.addAnonymisedColumn(anonymizedColumn);
		anonymiseRelatedTables(selectedRelatedTableColumns, anonymisationMethod);
		
		anonConfig.addAnonMethod(anonymisationMethod);
		
		persist_AddAnonymisation(anonymisationMethod);
	}

	private void persist_AddAnonymisation(AnonymisationMethod anonymisationMethod) {
		AnonymisationMethodData anonymisationMethodData;
		if(anonymisationMethod instanceof AnonymisationMethodMapping){
			anonymisationMethodData = createAnonymisationMethodMappingData((AnonymisationMethodMapping)anonymisationMethod);
		}
		else {
			anonymisationMethodData = new AnonymisationMethodData();
		}
		
		anonymisationMethodData.setDatabaseConfigId(dbConnectionFactory.getDatabaseConfig().getId());
		anonymisationMethodData.setAnonymizationType(anonymisationMethod.getType());
		anonymisationMethodData.setAnonMethodClass(anonymisationMethod.getClass().getName());
		
		for(AnonymisedColumnInfo column:anonymisationMethod.getApplyedToColumns()){
			AnonymisedColumnData anonymisedColumnData = new AnonymisedColumnData();
			anonymisedColumnData.setColumnName(column.getName());
			anonymisedColumnData.setColumnType(column.getType());
			anonymisedColumnData.setTableName(column.getTable().getName());
			anonymisedColumnData.setSchemaName(column.getTable().getSchema());
			anonymisationMethodData.addColumn(anonymisedColumnData);	
		}
		
		
		entitiesDao.save(anonymisationMethodData);
		anonymisationMethod.setId(anonymisationMethodData.getId());
	}
	
	private AnonymisationMethodMappingData createAnonymisationMethodMappingData(AnonymisationMethodMapping anonymisationMethodMapping){
		AnonymisationMethodMappingData anonymisationMethodMappingData = new AnonymisationMethodMappingData();
		MappingDefaultData mappingDefaultData = new MappingDefaultData();
		mappingDefaultData.setDefaultValue(anonymisationMethodMapping.getMappingDefault().getDefaultValue());
		anonymisationMethodMappingData.setMappingDefaultData(mappingDefaultData);
		
		for(MappingRule mappingRule:anonymisationMethodMapping.getMappingRulesList()){
			MappingRuleData mappingRuleData = new MappingRuleData();
			mappingRuleData.setMappingRuleType(mappingRule.getMappingRuleType());
			mappingRuleData.setMappedValue(mappingRule.getMappedValue());
			mappingRuleData.setBoundary(mappingRule.getBoundary());
			
			anonymisationMethodMappingData.addMappingRuleData(mappingRuleData);
			
		}
		
		return anonymisationMethodMappingData;
	}

	public void removeAnonymisation(DatabaseTableInfo selectedEditedTable,
			AnonymisedColumnInfo selectedAnonymizedColumn,
			List<RelatedTableColumnInfo> relatedTableColumnsToRemove) {
		
		AnonymisationMethod anonymisationMethod = selectedAnonymizedColumn.getAnonymisationMethod();
	
		// remove the selection
		selectedAnonymizedColumn.getTable().removeAnonymisedColumn(selectedAnonymizedColumn);
		anonymisationMethod.removeColumn(selectedAnonymizedColumn);
		entitiesDao.removeAnonymizedColumnData(selectedAnonymizedColumn.getTable().getName(), selectedAnonymizedColumn.getName(), selectedAnonymizedColumn.getTable().getSchema());
		
		// remove the related selected
		List<AnonymisedColumnInfo> toRemoveList = new LinkedList<>(); 
		for(AnonymisedColumnInfo relatedCol : anonymisationMethod.getApplyedToColumns()){
			if(selected(relatedTableColumnsToRemove, relatedCol)){
				toRemoveList.add(relatedCol);
			}
		}
		for(AnonymisedColumnInfo relatedCol:toRemoveList){
			relatedCol.getTable().removeAnonymisedColumn(relatedCol);
			anonymisationMethod.removeColumn(relatedCol);
			entitiesDao.removeAnonymizedColumnData(relatedCol.getTable().getName(), relatedCol.getName(), relatedCol.getTable().getName());
		}
		
		// remove the method if empty
		if(anonymisationMethod.getApplyedToColumns().isEmpty()){
			anonConfig.removeAnonMethod(anonymisationMethod);
			entitiesDao.removeAnonymisationMethodData(anonymisationMethod.getId());
		}
		
		// reset method to null
		selectedAnonymizedColumn.setAnonymisationMethod(null);
	}

	public void changeAnonymisation(DatabaseTableInfo selectedEditedTable,
			AnonymisedColumnInfo selectedAnonymizedColumn,
			List<RelatedTableColumnInfo> selectedRelatedTableColumns,
			AnonymisationMethod anonymisationMethod) {
		removeAnonymisation(selectedEditedTable, selectedAnonymizedColumn, selectedRelatedTableColumns);
		addAnonymisation(selectedEditedTable, selectedAnonymizedColumn, selectedRelatedTableColumns, anonymisationMethod);
			
	}

	private void anonymiseRelatedTables(
			List<RelatedTableColumnInfo> selectedRelatedTableColumns,
			AnonymisationMethod anonymisationMethod) {
		if(selectedRelatedTableColumns == null){
			return;
		}
		for(RelatedTableColumnInfo relatedTableColumnInfo:selectedRelatedTableColumns){
			DatabaseTableInfo databaseTableInfo = anonConfig.findTable(relatedTableColumnInfo.getTableName());
			DatabaseColumnInfo databaseColumnInfo = databaseTableInfo.findColumn(relatedTableColumnInfo.getColumnName());
			AnonymisedColumnInfo anonymisedColumn = new AnonymisedColumnInfo(databaseColumnInfo);
			anonymisationMethod.addColumn(anonymisedColumn);
			databaseTableInfo.addAnonymisedColumn(anonymisedColumn);
		}
	}

	private boolean selected(List<RelatedTableColumnInfo> relatedTableColumnsToRemove, AnonymisedColumnInfo anonymisedColumnInfo) {
		for(RelatedTableColumnInfo relatedTableColumnInfo:relatedTableColumnsToRemove){
			if(relatedTableColumnInfo.sameAs(anonymisedColumnInfo)){
				return true;
			}
		}
		return false;
	}

	
	
}
