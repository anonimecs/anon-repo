package org.anon.persistence.dao;

import java.util.List;

import org.anon.persistence.data.DatabaseConfig;

public interface DatabaseConfigDao {

	List<DatabaseConfig> findAll();
	
	List<DatabaseConfig> findConfigForUser(String username);
	
	void addDatabaseConfig(DatabaseConfig config);
	
	void updateDatabaseConifg(DatabaseConfig config);
	
	void removeDatabaseConfig(DatabaseConfig config);

	DatabaseConfig loadConnectionConfig(String guiName);
	
	boolean isGuiNameUnique(String guiName, Long id);

	void removeDatabaseConfig(String configGuiName);
}
