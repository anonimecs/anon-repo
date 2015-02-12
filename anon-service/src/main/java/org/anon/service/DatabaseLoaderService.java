package org.anon.service;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.vendor.DatabaseSpecifics;

public interface DatabaseLoaderService {
	
	void fillExampleValues(DatabaseTableInfo editedTable);

	List<RelatedTableColumnInfo> findRelatedTables(DatabaseTableInfo editedTable, DatabaseColumnInfo editedColumn);
	
	DatabaseSpecifics getDatabaseSpecifics();

	DataSource getDataSource();
	
	void connectDb(DatabaseConfig databaseConfig );

	void disconnectDb();

	int getTableCount(String selectedSchema);
	
	String getDefaultSchema();
	
	List<String> getSchemas();

	List<DatabaseTableInfo> loadTableListFromTargetDb(String selectedSchema);

	void loadExecConfig();

	void schemaChanged() ;

	ServiceResult testSufficientPermissions(String selectedSchema);

	List<String> getLoadErrors();


}
