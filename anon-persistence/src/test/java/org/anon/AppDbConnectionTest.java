package org.anon;

import java.io.IOException;
import java.util.List;

import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseConnection;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppDbConnectionTest extends DbEntitiesTest {


		
		private static DatabaseConfig config;
	

		
		@Test
		public void test0_0_dbSetup() throws IOException {
			recreateInMemDb();
		}



		
		@Test
		public void test1InsertConnectionConfig() {
			config = createDatabaseConfig();
			databaseConfigDao.addDatabaseConfig(config);
		}
		
		@Test
		public void test2LoadAllConnectionConfig() {
			List<DatabaseConfig> result = databaseConfigDao.findAllDatabaseConfigs();
			Assert.assertTrue(result.size() > 0);
		}
		
		@Test
		public void test3LoadConnectionConfig() {
			DatabaseConfig databaseConfig= databaseConfigDao.loadDatabaseConfig(CONFIGNAME,userDao.loadUserById(1l));
			Assert.assertTrue(databaseConfig != null);
		}
		
		@Test
		public void test4RemoveConnectionConfig() {
			DatabaseConnection databaseConnection = config.getDatabaseConnection();
			Long connectionId = databaseConnection.getId(); 

			// remove the old config
			databaseConfigDao.removeDatabaseConfig(config);

			// create new config with the same connection
			DatabaseConfig config2 = new DatabaseConfig();
			config2.setDatabaseConnection(databaseConnection);
			config2.setConfigurationName(CONFIGNAME+2);
			config2.setDefaultSchema("test_default_schema");
			config2.setSecurityUser(userDao.loadUserById(1l));
			
			databaseConfigDao.addDatabaseConfig(config2);
			
			DatabaseConfig databaseConfig2= databaseConfigDao.loadDatabaseConfig(CONFIGNAME+2, userDao.loadUserById(1l));
			
			Assert.assertEquals("same connection id", connectionId,databaseConfig2.getDatabaseConnection().getId());

			// remove the new config
			databaseConfigDao.removeDatabaseConfig(config2);
		

		}
		
	
		
		@Test
		public void test6LoadUserConnections(){
			
			List<DatabaseConnection> result = databaseConfigDao.findDatabaseConnectionsForUser(userDao.loadUserById(1l));
			Assert.assertEquals("one connection", 1, result.size());
			
		}
		
		@Test
		public void test9RemoveConnection(){
			List<DatabaseConnection> result = databaseConfigDao.findAllDatabaseConnections();
			Assert.assertEquals("one connection", 1, result.size());
			
			databaseConfigDao.removeDatabaseConnection(result.get(0));
		}
	
}
