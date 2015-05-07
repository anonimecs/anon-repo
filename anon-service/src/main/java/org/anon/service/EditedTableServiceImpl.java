package org.anon.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.Lookups;
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
import org.anon.service.where.WhereConditionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="editedTableService")
public class EditedTableServiceImpl implements EditedTableService{
	
	@Autowired
	protected AnonConfig anonConfig;
	
	@Autowired
	protected EntitiesDao entitiesDao;
	
	@Autowired
	private	DbConnectionFactory dbConnectionFactory;
	
	@Autowired
	private WhereConditionBuilder whereConditionBuilder;

	@Override
	public RowFilterTestResult testRowFilter(AnonymisedColumnInfo col, String whereCondition, WhereConditionBuilder.Applicability applicability, List<RelatedTableColumnInfo> relatedTableColumnInfos, AnonymisationMethod anonymisationMethod) {
		
		RowFilterTestResult res = new RowFilterTestResult();
		
		TestResult headColumnResult = new TestResult();
		res.setHeadColumnresult(headColumnResult);
		headColumnResult.setWhereClause(whereConditionBuilder.build(applicability, whereCondition));
		testWhereClause(headColumnResult,col);
		
		if(relatedTableColumnInfos != null){
			for(RelatedTableColumnInfo relatedTableColumnInfo : relatedTableColumnInfos){
				
				TestResult relatedColumnResult = new TestResult();
				res.addRelatedColumnresult(relatedColumnResult, relatedTableColumnInfo);
				AnonymisedColumnInfo relatedCol = Lookups.findTableColumn(relatedTableColumnInfo.getColumnName(), relatedTableColumnInfo.getTableName(), anonymisationMethod.getApplyedToColumns());
				relatedColumnResult.setWhereClause(whereConditionBuilder.buildForRelatedTable(applicability, whereCondition, relatedCol, col));
				testWhereClause(relatedColumnResult, relatedCol);
			}
		}		
		
		return res;
				
	}
	
	private void testWhereClause(TestResult columnResult, AnonymisedColumnInfo col) {
		String sql = "select count(*) from " + col.getTable().getName() + " where " + columnResult.getWhereClause();
		try{
			int rowCount = new JdbcTemplate(dbConnectionFactory.getConnectionForSchema(col.getTable().getSchema()).getDataSource()).queryForInt(sql);
			columnResult.setRowCount(rowCount);
		}
		catch(Exception e){
			columnResult.setException(e);
		}
		
	}



	
	@Override
	@Transactional
	public void saveRowFilter(AnonymisedColumnInfo col, String whereCondition, WhereConditionBuilder.Applicability applicability, List<RelatedTableColumnInfo> relatedTableColumnInfos, AnonymisationMethod anonymisationMethod) {
		col.setGuiFieldWhereCondition(whereCondition);
		col.setGuiFieldApplicability(applicability.name());
		col.setWhereCondition(whereConditionBuilder.build(applicability, whereCondition));
		saveRowFilter(col);

		// handle related tables
		if(relatedTableColumnInfos != null){
			for(RelatedTableColumnInfo relatedTableColumnInfo : relatedTableColumnInfos){
				
				AnonymisedColumnInfo relatedCol = Lookups.findTableColumn(relatedTableColumnInfo.getColumnName(), relatedTableColumnInfo.getTableName(), anonymisationMethod.getApplyedToColumns());
				relatedCol.setGuiFieldWhereCondition("Condition was defined for " +col + " : "+ whereCondition);
				relatedCol.setGuiFieldApplicability(applicability.name());
				relatedCol.setWhereCondition(whereConditionBuilder.buildForRelatedTable(applicability, whereCondition, relatedCol, col));
				saveRowFilter(relatedCol);
			}
		}		
		
	}

	
	@Override
	public void deleteRowFilter(AnonymisedColumnInfo col, List<RelatedTableColumnInfo> relatedTableColumnInfos, AnonymisationMethod anonymisationMethod) {
		col.setGuiFieldWhereCondition(null);
		col.setGuiFieldApplicability(null);
		col.setWhereCondition(null);
		saveRowFilter(col);

		// handle related tables
		if(relatedTableColumnInfos != null){
			for(RelatedTableColumnInfo relatedTableColumnInfo : relatedTableColumnInfos){
				
				AnonymisedColumnInfo relatedCol = Lookups.findTableColumn(relatedTableColumnInfo.getColumnName(), relatedTableColumnInfo.getTableName(), anonymisationMethod.getApplyedToColumns());
				relatedCol.setGuiFieldWhereCondition(null);
				relatedCol.setGuiFieldApplicability(null);
				relatedCol.setWhereCondition(null);
				saveRowFilter(relatedCol);
			}
		}		
		
	}


	private void saveRowFilter(AnonymisedColumnInfo anonymisedColumnInfo){
		AnonymisedColumnData anonymisedColumnData = entitiesDao.loadAnonymisedColumnData(anonymisedColumnInfo);
		anonymisedColumnData.setWhereCondition(anonymisedColumnInfo.getWhereCondition());
		anonymisedColumnData.setGuiFieldApplicability(anonymisedColumnInfo.getGuiFieldApplicability());
		anonymisedColumnData.setGuiFieldWhereCondition(anonymisedColumnInfo.getGuiFieldWhereCondition());
		entitiesDao.save(anonymisedColumnData);
	}
	
	@Override
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
		
		if(anonymisationMethod.getType().equals(AnonymizationType.ENCRYPT)) {
			anonymisationMethodData.setPassword(anonymisationMethod.getPassword());
		}
		
		entitiesDao.save(anonymisationMethodData);
		anonymisationMethod.setId(anonymisationMethodData.getId());

		for(AnonymisedColumnInfo column:anonymisationMethod.getApplyedToColumns()){
			AnonymisedColumnData anonymisedColumnData = new AnonymisedColumnData();
			anonymisedColumnData.setColumnName(column.getName());
			anonymisedColumnData.setColumnType(column.getType());
			anonymisedColumnData.setTableName(column.getTable().getName());
			anonymisedColumnData.setSchemaName(column.getTable().getSchema());
			anonymisedColumnData.setWhereCondition(column.getWhereCondition());
			anonymisedColumnData.setGuiFieldApplicability(column.getGuiFieldApplicability());
			anonymisedColumnData.setGuiFieldWhereCondition(column.getGuiFieldWhereCondition());
			anonymisationMethodData.addColumn(anonymisedColumnData);	
			
			entitiesDao.save(anonymisedColumnData);
			column.setId(anonymisedColumnData.getId());
		}
		
		
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

	@Transactional
	@Override
	public void removeAnonymisation(DatabaseTableInfo selectedEditedTable,
			final AnonymisedColumnInfo selectedAnonymizedColumn,
			List<RelatedTableColumnInfo> relatedTableColumnsToRemove) {
		
		final AnonymisationMethod anonymisationMethod = selectedAnonymizedColumn.getAnonymisationMethod();
	
		// first run all the DB work, and if successful, do the VO changes
		List<Runnable> workParts = new ArrayList<>();
		
		// remove the selection
		workParts.add(new Runnable() {
			@Override
			public void run() {
				selectedAnonymizedColumn.getTable().removeAnonymisedColumn(selectedAnonymizedColumn);
				anonymisationMethod.removeColumn(selectedAnonymizedColumn);
			}});
		entitiesDao.removeAnonymizedColumnData(selectedAnonymizedColumn.getId());
		
		// remove the related selected
		List<AnonymisedColumnInfo> toRemoveList = new LinkedList<>(); 
		for(AnonymisedColumnInfo relatedCol : anonymisationMethod.getApplyedToColumns()){
			if(selected(relatedTableColumnsToRemove, relatedCol)){
				toRemoveList.add(relatedCol);
			}
		}
		for(final AnonymisedColumnInfo relatedCol:toRemoveList){
			workParts.add(new Runnable() {
				@Override
				public void run() {
					relatedCol.getTable().removeAnonymisedColumn(relatedCol);
					anonymisationMethod.removeColumn(relatedCol);
				}});
			entitiesDao.removeAnonymizedColumnData(relatedCol.getId());
		}
		
		// remove the method if empty
		if(entitiesDao.isEmptyAnonymisationMethod(anonymisationMethod.getId())){
			workParts.add(new Runnable() {
				@Override
				public void run() {
					anonConfig.removeAnonMethod(anonymisationMethod);
				}});
			entitiesDao.removeAnonymisationMethodData(anonymisationMethod.getId());
		}
		
		// at this point all DB was fine, run the retained java changes
		for(Runnable workPart:workParts){
			workPart.run();
		}
		
		// reset method to null
		selectedAnonymizedColumn.setAnonymisationMethod(null);
	}

	@Override
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
