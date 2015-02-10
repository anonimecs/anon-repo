package org.anon.persistence.dao;

import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseTableData;

public interface EntitiesDao {

	void insert(DatabaseTableData databaseTableData);

	List<DatabaseTableData> loadAllTables();

	void save(AnonymisationMethodData anonymisationMethodData);

	List<AnonymisationMethodData> loadAllAnonMethods(DatabaseConfig databaseConfig);

	int removeAnonymizedColumnData(String tableName, String columnName, String schemaName);

	int removeAnonymisationMethodData(Long id);

	AnonymisationMethodData loadAnonymisationMethodData(Long id);

	AnonymisedColumnData loadAnonymisedColumnData(AnonymisedColumnInfo anonymisedColumnInfo);

}
