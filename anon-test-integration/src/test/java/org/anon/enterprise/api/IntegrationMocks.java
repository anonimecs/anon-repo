<<<<<<< HEAD
package org.anon.enterprise.api;

import org.anon.data.Database;
import org.anon.persistence.data.DatabaseConfig;

public class IntegrationMocks {

	public static DatabaseConfig createDatabaseConfigMySql(long id) {
		DatabaseConfig config = new DatabaseConfig();
		config.setUrl("localhost:3306");
		config.setLogin("root");
		config.setPassword("secret");
		config.setVendor(Database.MYSQL);
		config.setVersion(null);
		config.setGuiName("integration-test-mysql_" + id);
		config.setDefaultSchema("employees");
		return config;
	}

}
=======
package org.anon.enterprise.api;

import org.anon.data.Database;
import org.anon.persistence.data.DatabaseConfig;

public class IntegrationMocks {

	public static DatabaseConfig createDatabaseConfigMySql(long id) {
		DatabaseConfig config = new DatabaseConfig();
		config.setUrl("localhost:3306");
		config.setLogin("root");
		config.setPassword("secret");
		config.setVendor(Database.MYSQL);
		config.setVersion(null);
		config.setGuiName("integration-test-mysql_" + id);
		config.setDefaultSchema("employees");
		return config;
	}

}
>>>>>>> f433e150161bc23d087604a6480c2ef237bf6713
