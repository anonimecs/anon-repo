package org.anon.enterprise.api;

import org.anon.data.Database;
import org.anon.persistence.data.DatabaseConfig;

public class IntegrationMocks {

	public static DatabaseConfig createDatabaseConfigMySql(long id, String url, String login, String passw, String defaultSchema) {
		DatabaseConfig config = new DatabaseConfig();
		config.setUrl("localhost:3306");
		config.setLogin("root");
		config.setPassword("secret");
		config.setVendor(Database.MYSQL);
		config.setVersion(null);
		config.setDefaultSchema("employees");
		config.setGuiName("integration-test-mysql_" + id);
		return config;
	}

}
