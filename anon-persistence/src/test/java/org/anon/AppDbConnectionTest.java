package org.anon;

import java.util.List;

import org.anon.data.Database;
import org.anon.persistence.dao.DatabaseConfigDao;
import org.anon.persistence.data.DatabaseConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class AppDbConnectionTest extends DbEntitiesTest {

		private static final String GUINAME = "test";

		@Autowired
		DatabaseConfigDao databaseConfigDao;
		
		private DatabaseConfig config;
		
		@Before
		public void init() {
			config = new DatabaseConfig();
			config.setUrl("test.anon.org");
			config.setLogin("anon");
			config.setPassword("anon");
			config.setVendor(Database.SYBASE);
			config.setVersion("16.0");
			config.setGuiName(GUINAME);
		}
		
		@Test
		public void testInsertConnectionConfig() {
			databaseConfigDao.addDatabaseConfig(config);
		}
		
		@Test
		public void testLoadAllConnectionConfig() {
			List<DatabaseConfig> result = databaseConfigDao.findAll();
			Assert.assertTrue(result.size() > 0);
		}
		
		@Test
		public void testLoadConnectionConfig() {
			DatabaseConfig databaseConfig= databaseConfigDao.loadConnectionConfig(GUINAME);
			Assert.assertTrue(databaseConfig != null);
		}
		
		@Test
		public void testRemoveConnectionConfig() {
			databaseConfigDao.removeDatabaseConfig(config);
		}
}
