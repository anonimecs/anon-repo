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
import org.anon.persistence.dao.EntitiesDao;
import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.vendor.DatabaseSpecifics;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseLoaderService {
	
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	protected AnonConfig anonConfig;
	
	@Autowired
	protected EntitiesDao entitiesDao;

	@Autowired
	private	DbConnectionFactory dbConnectionFactory;
	
	@Autowired
	private AnonCandidatesParser anonCandidatesParser; 

	private List<String> loadErrors;


	public void fillExampleValues(DatabaseTableInfo editedTable) {
		dbConnectionFactory.getConnection().fillExampleValues(editedTable);
	}

	public List<RelatedTableColumnInfo> findRelatedTables(DatabaseTableInfo editedTable, DatabaseColumnInfo editedColumn) {
		return dbConnectionFactory.getConnection().findRelatedTables(editedTable, editedColumn);
	}
	
	public DatabaseSpecifics getDatabaseSpecifics(){
		return dbConnectionFactory.getDatabaseSpecifics();
	}

	public DataSource getDataSource() {
		return dbConnectionFactory.getConnection().getDataSource();
	}
	
	public void connectDb(DatabaseConfig databaseConfig ){
		
		dbConnectionFactory.setDatabaseConfig(databaseConfig);
		//dbConnectionFactory.createConnection();


	}

	public void disconnectDb() {
		dbConnectionFactory.setDatabaseConfig(null);
		dbConnectionFactory.clearConnection();
		anonConfig.init();
		
	}

	public int getTableCount(String selectedSchema) {
		return dbConnectionFactory.getConnection().getTableCount(selectedSchema);
	}

	public String getDefaultSchema() {
		return dbConnectionFactory.getConnection().getDefaultSchema();
	}

	public List<String> getSchemas() {
		return dbConnectionFactory.getConnection().getSchemas();
	}

	public List<String> getLoadErrors(){
		return loadErrors;
	}
	
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
					AnonymisedColumnInfo anonymisedColumnInfo = new AnonymisedColumnInfo(anonymisedColumnData.getColumnName(), anonymisedColumnData.getColumnType(), dbConnectionFactory.getConnection().getDatabaseSpecifics());
					anonymisationMethod.addColumn(anonymisedColumnInfo);
	
					DatabaseTableInfo table = lookupTable(anonymisedColumnData.getTableName());
					if(table == null){
						loadErrors.add("Failed to find anonymised column " + anonymisedColumnData.getTableName() + "." + anonymisedColumnData.getColumnName());
						logger.error("failed to find table and column " + anonymisedColumnData, new Exception());
					} else {
						anonymisedColumnInfo.setTable(table);
						table.addAnonymisedColumn(anonymisedColumnInfo);
					}
				}
			}
			anonConfig.addAnonMethod(anonymisationMethod);
		}
		
		
	}

	private DatabaseTableInfo lookupTable(String tableName) {
		for(DatabaseTableInfo databaseTableInfo:anonConfig.getTables()){
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

	public void schemaChanged() {
		anonConfig.init();
	}
	
	
}
