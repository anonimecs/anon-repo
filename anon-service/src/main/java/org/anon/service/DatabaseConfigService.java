package org.anon.service;

import java.util.List;

import org.anon.persistence.data.DatabaseConfig;

public interface DatabaseConfigService {

	public List<DatabaseConfig> loadConnectionConfigs();
	
	public DatabaseConfig loadConnectionConfig(String guiName);
	
	public void deleteDatabaseConfig(DatabaseConfig config) throws ServiceException;

	public void deleteDatabaseConfig(String configGuiName) throws ServiceException;
	
	public void addDatabaseConfig(DatabaseConfig config) throws ServiceException;
	
	public void testDatabaseConfig(DatabaseConfig config) throws ServiceException;
	
	public void updateDatabaseConfig(DatabaseConfig config) throws ServiceException;

}


