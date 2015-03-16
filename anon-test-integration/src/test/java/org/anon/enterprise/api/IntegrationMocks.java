package org.anon.enterprise.api;

import org.anon.data.Database;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseConnection;
import org.anon.persistence.data.SecurityUser;

public class IntegrationMocks {

	public static DatabaseConfig createDatabaseConfigMySql(long id, String defaultSchema, DatabaseConnection databaseConnection,SecurityUser user) {
		DatabaseConfig config = new DatabaseConfig();
		config.setDatabaseConnection(databaseConnection);
		config.setConfigurationName("integration-test-mysql_" + id);
		config.setDefaultSchema(defaultSchema);
		config.setSecurityUser(user);
		return config;

	}

	
	public static DatabaseConnection createConnection(long id, String url, String login, String passw, SecurityUser user){
		DatabaseConnection databaseConnection = new DatabaseConnection();
		databaseConnection.setUrl(url);
		databaseConnection.setLogin(login);
		databaseConnection.setPassword(passw);
		databaseConnection.setVendor(Database.MYSQL);
		databaseConnection.setVersion(null);
		databaseConnection.setGuiName("integration-test-mysql_" + id);
		databaseConnection.setSecurityUser(user);

		return databaseConnection;
	
	}
}
