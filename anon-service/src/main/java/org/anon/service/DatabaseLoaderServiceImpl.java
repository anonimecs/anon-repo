package org.anon.service;

import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodMapping;
import org.anon.logic.map.EqualsTo;
import org.anon.logic.map.LessThan;
import org.anon.logic.map.MappingDefault;
import org.anon.logic.map.MappingRule;
import org.anon.logic.map.MappingRule.MappingRuleType;
import org.anon.logic.map.MoreThan;
import org.anon.persistence.dao.EntitiesDao;
import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisationMethodMappingData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.MappingRuleData;
import org.anon.vendor.DatabaseSpecifics;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="databaseLoaderService")
public class DatabaseLoaderServiceImpl implements DatabaseLoaderService{
	
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	protected AnonConfig anonConfig;
	
	@Autowired
	protected AnonConfig execConfig;
	
	@Autowired
	protected EntitiesDao entitiesDao;

	@Autowired
	private	DbConnectionFactory dbConnectionFactory;
	
	@Autowired
	private AnonCandidatesParser anonCandidatesParser; 

	private List<String> loadErrors;


	@Override
	public void fillExampleValues(DatabaseTableInfo editedTable) {
		dbConnectionFactory.getConnection().fillExampleValues(editedTable);
	}

	@Override
	public List<RelatedTableColumnInfo> findRelatedTables(DatabaseTableInfo editedTable, DatabaseColumnInfo editedColumn) {
		return dbConnectionFactory.getConnection().findRelatedTables(editedTable, editedColumn);
	}
	
	@Override
	public DatabaseSpecifics getDatabaseSpecifics(){
		return dbConnectionFactory.getDatabaseSpecifics();
	}

	@Override
	public DataSource getDataSource() {
		return dbConnectionFactory.getConnection().getDataSource();
	}
	
	@Override
	public void connectDb(DatabaseConfig databaseConfig ){	
		dbConnectionFactory.setDatabaseConfig(databaseConfig);
	}

	@Override
	public void disconnectDb() {
		dbConnectionFactory.setDatabaseConfig(null);
		dbConnectionFactory.clearConnection();
		anonConfig.init();
		execConfig.init();
		
	}

	@Override
	public int getTableCount(String selectedSchema) {
		return dbConnectionFactory.getConnection().getTableCount(selectedSchema);
	}

	@Override
	public String getDefaultSchema() {
		return dbConnectionFactory.getConnection().getDefaultSchema();
	}

	@Override
	public List<String> getSchemas() {
		return dbConnectionFactory.getConnection().getSchemas();
	}

	@Override
	public List<String> getLoadErrors(){
		return loadErrors;
	}
	
	@Override
	public List<DatabaseTableInfo> loadTableListFromTargetDb(String selectedSchema) {
		loadErrors = new LinkedList<String>();
		if(anonConfig.emptyTables()){
			anonConfig.setTables(dbConnectionFactory.getConnection().getTableList(selectedSchema));
			loadPersistedAnonymisations(selectedSchema);
			anonCandidatesParser.setDatabaseSpefifics(dbConnectionFactory.getConnection().getDatabaseSpecifics());
			anonCandidatesParser.run(anonConfig);
		}
		
		return anonConfig.getTables();
	}

	protected void loadPersistedAnonymisations(String selectedSchema) {
		List<AnonymisationMethodData> anonymisationMethodDatas = entitiesDao.loadAllAnonMethods(dbConnectionFactory.getDatabaseConfig());
		
		for(AnonymisationMethodData anonymisationMethodData: anonymisationMethodDatas){
			AnonymisationMethod anonymisationMethod = instantiate(anonymisationMethodData);
			for(AnonymisedColumnData anonymisedColumnData: anonymisationMethodData.getApplyedToColumns()){
				if(selectedSchema.equalsIgnoreCase(anonymisedColumnData.getSchemaName())){
					addColumn(anonymisationMethod, anonymisedColumnData);
				}
			}
			handleAnonMethodMapping(anonymisationMethodData, anonymisationMethod);
			anonConfig.addAnonMethod(anonymisationMethod);
		}
	}

	@Override
	public void loadExecConfig() {
		
		List<AnonymisationMethodData> anonymisationMethodDatas = entitiesDao.loadAllAnonMethods(dbConnectionFactory.getDatabaseConfig());	
		for(AnonymisationMethodData anonymisationMethodData: anonymisationMethodDatas){
			
			if(!execConfigHasAnonMethod(anonymisationMethodData)) {
				AnonymisationMethod anonymisationMethod = instantiate(anonymisationMethodData);
				for(AnonymisedColumnData anonymisedColumnData: anonymisationMethodData.getApplyedToColumns()){
					addColumn(anonymisationMethod, anonymisedColumnData);
				}
				handleAnonMethodMapping(anonymisationMethodData, anonymisationMethod);
				execConfig.addAnonMethod(anonymisationMethod);
			}
		}
	}
	
	private void handleAnonMethodMapping(
			AnonymisationMethodData anonymisationMethodData,
			AnonymisationMethod anonymisationMethod) {
		if(anonymisationMethod instanceof AnonymisationMethodMapping){
			AnonymisationMethodMappingData anonymisationMethodMappingData = (AnonymisationMethodMappingData)anonymisationMethodData;
			AnonymisationMethodMapping anonymisationMethodMapping = (AnonymisationMethodMapping)anonymisationMethod;
			anonymisationMethodMapping.setMappingDefault(new MappingDefault(anonymisationMethodMappingData.getMappingDefaultData().getDefaultValue()));
			for(MappingRuleData mappingRuleData:anonymisationMethodMappingData.getMappingRules()){
				MappingRule mappingRule = null; 
				if(mappingRuleData.getMappingRuleType() == MappingRuleType.LessThan){
					mappingRule = new LessThan(mappingRuleData.getBoundary(), mappingRuleData.getMappedValue());
				}
				else if(mappingRuleData.getMappingRuleType() == MappingRuleType.MoreThan){
					mappingRule = new MoreThan(mappingRuleData.getBoundary(), mappingRuleData.getMappedValue());
				}
				else if(mappingRuleData.getMappingRuleType() == MappingRuleType.EqualsTo){
					mappingRule = new EqualsTo(mappingRuleData.getBoundary(), mappingRuleData.getMappedValue());
				}
				else {
					throw new RuntimeException("Unmapped " + mappingRuleData);
				}
				anonymisationMethodMapping.addMappingRule(mappingRule);
			}
		}
	}

	private void addColumn(AnonymisationMethod anonymisationMethod, AnonymisedColumnData anonymisedColumnData) {

		DatabaseTableInfo table = lookupTable(anonymisedColumnData.getTableName());
		if(table == null){
			loadErrors.add("Failed to find anonymised column " + anonymisedColumnData.getTableName() + "." + anonymisedColumnData.getColumnName());
			logger.error("failed to find table and column " + anonymisedColumnData, new Exception());
		} else {
			DatabaseColumnInfo databaseColumnInfo = table.findColumn(anonymisedColumnData.getColumnName());
			
			AnonymisedColumnInfo anonymisedColumnInfo = new AnonymisedColumnInfo(anonymisedColumnData.getColumnName(), anonymisedColumnData.getColumnType(), databaseColumnInfo.isNullable(), dbConnectionFactory.getConnection().getDatabaseSpecifics());
			anonymisationMethod.addColumn(anonymisedColumnInfo);

			anonymisedColumnInfo.setTable(table);
			table.addAnonymisedColumn(anonymisedColumnInfo);
		}
	}
	

	
	private boolean execConfigHasAnonMethod(AnonymisationMethodData anonymisationMethodData) {
		for(AnonymisationMethod anonymisationMethod : execConfig.getAnonMethods()) {
			if(anonymisationMethod.getId().equals(anonymisationMethodData.getId())) {
				return true;
			}
		}
		return false;
	}

	private DatabaseTableInfo lookupTable(String tableName) {
		for(DatabaseTableInfo databaseTableInfo : anonConfig.getTables()){
			if(databaseTableInfo.getName().equals(tableName)){
				return databaseTableInfo;
			}
		}
		return null;
	}
	
	private DatabaseTableInfo lookupTableBySchema(String tableName, String schema) {
		for(DatabaseTableInfo databaseTableInfo : dbConnectionFactory.getConnection().getTableList(schema)){
			if(databaseTableInfo.getName().equals(tableName)){
				return databaseTableInfo;
			}
		}
		return null;
	}

	private AnonymisationMethod instantiate(AnonymisationMethodData anonymisationMethodData) {
		try {
			@SuppressWarnings("unchecked")
			Class<AnonymisationMethod> clazz = (Class<AnonymisationMethod>) Class.forName(anonymisationMethodData.getAnonMethodClass());
			AnonymisationMethod anonymisationMethod = clazz.newInstance();
			anonymisationMethod.setId(anonymisationMethodData.getId());
			anonymisationMethod.setPassword(anonymisationMethodData.getPassword());
			return anonymisationMethod;
		} catch (Exception e) {
			throw new AnonymisationMethodCreateException(anonymisationMethodData, e);
		}
	}

	public class AnonymisationMethodCreateException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		AnonymisationMethodData anonymisationMethodData;
		
		public AnonymisationMethodCreateException(AnonymisationMethodData anonymisationMethodData, Exception cause) {
			super(cause);
			this.anonymisationMethodData = anonymisationMethodData;
		}
		
		@Override
		public String getMessage() {
			return "Failed to instantiate " + anonymisationMethodData + ". Error: " + getCause().getMessage();
		}
	}

	@Override
	public void schemaChanged() {
		anonConfig.init();
	}

	@Override
	public void testSufficientPermissions(String selectedSchema) throws ServiceException{
		try{
			dbConnectionFactory.getConnection().testSufficientPermissions(selectedSchema);
		}
		catch(Exception e){
			throw new ServiceException("User has unsufficient perimissions to anonymise. Grant permissions to run : ", e.getMessage(), e);
		}
	}


}
