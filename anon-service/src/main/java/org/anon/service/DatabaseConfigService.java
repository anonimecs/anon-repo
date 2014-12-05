package org.anon.service;

import java.util.List;

import org.anon.persistence.data.DatabaseConfig;

public interface DatabaseConfigService {

	public List<DatabaseConfig> loadConnectionConfigs();
	
	public DatabaseConfig loadConnectionConfig(String guiName);
	
	public ServiceResult deleteDatabaseConfig(DatabaseConfig config);

	public ServiceResult addDatabaseConfig(DatabaseConfig config);

}


