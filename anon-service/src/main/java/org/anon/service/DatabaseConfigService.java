package org.anon.service;

import java.util.List;

import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseConnection;

public interface DatabaseConfigService {

	public List<DatabaseConfig> loadDatabaseConfigsForUser();
	
	public List<DatabaseConnection> loadDatabaseConnectionsForUser();
	
	public DatabaseConfig loadDatabaseConfig(String configName);
	
	public DatabaseConnection loadDatabaseConnection(String guiName);
	
	public void deleteDatabaseConfig(DatabaseConfig config) ;

	public void deleteDatabaseConnection(DatabaseConnection databaseConnection);

	public void deleteDatabaseConfig(String configGuiName);
	
	public void addDatabaseConfig(DatabaseConfig config) ;
	
	public void addDatabaseConnection(DatabaseConnection databaseConnection) throws ServiceException;
	
	public void validateDatabaseConnection(DatabaseConnection databaseConnection) throws ServiceException;
	
	public void updateDatabaseConfig(DatabaseConfig config) ;

	public void updateDatabaseConnection(DatabaseConnection selectedDatabaseConnection);

}


