package org.anon.service.reduce;

import java.util.ArrayList;
import java.util.List;

import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.ReductionMethod;
import org.anon.data.ReductionMethodReferencingTable;
import org.anon.data.ReductionType;
import org.anon.persistence.dao.EntitiesDao;
import org.anon.persistence.data.ReductionMethodData;
import org.anon.persistence.data.ReductionMethodReferencingTableData;
import org.anon.service.DatabaseLoaderService;
import org.anon.service.DbConnectionFactory;
import org.anon.service.TestResult;
import org.anon.service.where.WhereConditionBuilder;
import org.anon.service.where.WhereConditionBuilder.Applicability;
import org.anon.vendor.constraint.ConstraintBundleFactory;
import org.anon.vendor.constraint.referential.ForeignKeyConstraint;
import org.anon.vendor.constraint.referential.ForeignKeyConstraintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service(value="reduceService")
public class ReduceServiceImpl implements ReduceService {

	@Autowired EntitiesDao entitiesDao;
	@Autowired DbConnectionFactory dbConnectionFactory;
	@Autowired ConstraintBundleFactory constraintBundleFactory;
	@Autowired DatabaseLoaderService databaseLoaderService;
	@Autowired WhereConditionBuilder whereConditionBuilder;

	
	@Override
	public List<ReductionMethodData> loadPersistedReductions() {
		List<ReductionMethodData> reductionMethodDatas = entitiesDao.loadAllReductionMethods(dbConnectionFactory.getDatabaseConfig());
		return reductionMethodDatas;


	}

	
	@Override
	public ReduceTestResult test(DatabaseTableInfo editedTable, String guiWhereCondition,
			ReductionType reductionType, List<RelatedTable> relatedTables) {
		ReduceTestResult res = new ReduceTestResult();

		
		TestResult headTableResult = new TestResult();
		res.setHeadResult(headTableResult);
		headTableResult.setWhereClause(whereConditionBuilder.build(Applicability.APPLY, guiWhereCondition));
		testWhereClause(headTableResult,editedTable.getName(), editedTable.getSchema());
		
		if(relatedTables != null){
			for(RelatedTable relatedTable : relatedTables){
				
				TestResult relatedColumnResult = new TestResult();
				res.addRelatedResult(relatedColumnResult, relatedTable);
				if(relatedTable.getReductionType() == ReductionType.DELETE_WHERE){
					relatedColumnResult.setWhereClause(whereConditionBuilder.build(Applicability.APPLY, relatedTable.getGuiWhereCondition()));
					testWhereClause(relatedColumnResult, relatedTable.getRelatedTableName(), editedTable.getSchema());
				}
				else{
					relatedColumnResult.setRowCount(0);
					relatedColumnResult.setException(null);
				}
			}
		}		

		
		return res;
	}
	
	private void testWhereClause(TestResult columnResult, String tableName, String schemaName) {
		String sql = "select count(*) from " + tableName + " where " + columnResult.getWhereClause();
		try{
			int rowCount = new JdbcTemplate(dbConnectionFactory.getConnectionForSchema(schemaName).getDataSource()).queryForInt(sql);
			columnResult.setRowCount(rowCount);
		}
		catch(Exception e){
			columnResult.setException(e);
		}
		
	}


	@Override
	public ReductionMethod save(DatabaseTableInfo editedTable, String guiWhereCondition, ReductionType reductionType, List<RelatedTable> relatedTables) {
		
		ReductionMethodData reductionMethodData = new ReductionMethodData();
		reductionMethodData.setDatabaseConfigId(dbConnectionFactory.getDatabaseConfig().getId());
		reductionMethodData.setReductionType(reductionType);
		reductionMethodData.setSchemaName(editedTable.getSchema());
		reductionMethodData.setTableName(editedTable.getName());
		reductionMethodData.setWhereCondition(guiWhereCondition);

		if(relatedTables != null){
			for(RelatedTable relatedTable : relatedTables){
				ReductionMethodReferencingTableData data = new ReductionMethodReferencingTableData();
				data.setReductionType(relatedTable.getReductionType());
				data.setSchemaName(editedTable.getSchema());
				data.setTableName(relatedTable.getRelatedTableName());
				data.setWhereCondition(relatedTable.getGuiWhereCondition());
				
				reductionMethodData.add(data);
			}
		}
		
		entitiesDao.save(reductionMethodData);
		
		return reductionMethodData;
		
	}
	
	@Override
	public void delete(ReductionMethod reductionMethodData) {
		entitiesDao.removeReductionMethodData(reductionMethodData);
		
	}
	
	@Override
	public List<RelatedTable> findRelatedTablesForReduce(DatabaseTableInfo editedTable) {
		ForeignKeyConstraintManager foreignKeyConstraintManager = constraintBundleFactory.createForeignKeyConstraintManager(databaseLoaderService.getDatabaseSpecifics(), databaseLoaderService.getDataSource());
		
		List<RelatedTable> res = new ArrayList<>();
		for(DatabaseColumnInfo databaseColumnInfo:editedTable.getColumns()){
			List<ForeignKeyConstraint> foreignKeysFrom = foreignKeyConstraintManager.loadForeignKeysFrom(editedTable.getName(), databaseColumnInfo.getName(), editedTable.getSchema());
			for(ForeignKeyConstraint foreignKeyConstraint:foreignKeysFrom){
				String relatedTableName = foreignKeyConstraint.getTargetTableName();
				String [] cols = foreignKeyConstraint.getTargetColumnNames();
				res.add(new RelatedTable(foreignKeyConstraint, relatedTableName, cols));
			}
			
			List<ForeignKeyConstraint> foreignKeysTo = foreignKeyConstraintManager.loadForeignKeysTo(editedTable.getName(), databaseColumnInfo.getName(), editedTable.getSchema());
			for(ForeignKeyConstraint foreignKeyConstraint:foreignKeysTo){
				String relatedTableName = foreignKeyConstraint.getSourceTableName();
				String [] cols = foreignKeyConstraint.getSourceColumnNames();
				res.add(new RelatedTable(foreignKeyConstraint, relatedTableName, cols));
			}
			
		}
		return res;
	}

	@Override
	public List<RelatedTable> extractRelatedTablesForReduce(DatabaseTableInfo editedTable,
			ReductionMethod reductionMethodData) {
		List<RelatedTable> res = new ArrayList<>();
		for(ReductionMethodReferencingTable reductionMethodReferencingTable:reductionMethodData.getReferencingTableDatas()){
			RelatedTable relatedTable = new RelatedTable(null, reductionMethodReferencingTable.getTableName(), null);
			relatedTable.setGuiWhereCondition(reductionMethodReferencingTable.getWhereCondition());
			relatedTable.setReductionType(reductionMethodReferencingTable.getReductionType());
			res.add(relatedTable);
		}
		 
		return res;

	}


}
