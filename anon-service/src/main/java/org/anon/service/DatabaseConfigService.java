package org.anon.service;

import java.util.List;

import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseConnection;

public interface DatabaseConfigService {

	public List<DatabaseConfig> loadDatabaseConfigs();
	
	public List<DatabaseConnection> findAllDatabaseConnections();
	
	public DatabaseConfig loadDatabaseConfig(String configName);
	
	public DatabaseConnection loadDatabaseConnection(String guiName);
	
	public void deleteDatabaseConfig(DatabaseConfig config) throws ServiceException;

	public void deleteDatabaseConnection(DatabaseConnection databaseConnection);

	public void deleteDatabaseConfig(String configGuiName) throws ServiceException;
	
	public void addDatabaseConfig(DatabaseConfig config) throws ServiceException;
	
	public void addDatabaseConnection(DatabaseConnection databaseConnection) throws ServiceException;
	
	public void validateDatabaseConnection(DatabaseConnection databaseConnection) throws ServiceException;
	
	public void updateDatabaseConfig(DatabaseConfig config) throws ServiceException;

	public void updateDatabaseConnection(DatabaseConnection selectedDatabaseConnection);

}


