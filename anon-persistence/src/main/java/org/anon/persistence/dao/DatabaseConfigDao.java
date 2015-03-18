package org.anon.persistence.dao;

import java.util.List;

import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseConnection;
import org.anon.persistence.data.SecurityUser;

public interface DatabaseConfigDao {

	List<DatabaseConfig> findAllDatabaseConfigs();
	
	List<DatabaseConnection> findAllDatabaseConnections();
	
	List<DatabaseConfig> findDatabaseConfigForUser(String username);
	
	List<DatabaseConnection> findDatabaseConnectionsForUser(SecurityUser user);

	void addDatabaseConfig(DatabaseConfig config);
	
	void addDatabaseConnection(DatabaseConnection databaseConnection);

	void updateDatabaseConfig(DatabaseConfig config);
	
	void removeDatabaseConfig(DatabaseConfig config);

	DatabaseConfig loadDatabaseConfig(String configurationName);
	
	DatabaseConnection loadDatabaseConnection(String guiName);

	void removeDatabaseConfig(String configurationName);

	void removeDatabaseConnection(DatabaseConnection databaseConnection);

	void updateDatabaseConnection(DatabaseConnection databaseConnection);
}
