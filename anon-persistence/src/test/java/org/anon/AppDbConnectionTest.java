package org.anon;

import java.io.IOException;
import java.util.List;

import org.anon.persistence.data.DatabaseConfig;
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
		public void test0_insertDbConfig() {
			config = createDatabaseConfig();
		}


		
		@Test
		public void test1InsertConnectionConfig() {
			databaseConfigDao.addDatabaseConfig(config);
		}
		
		@Test
		public void test2LoadAllConnectionConfig() {
			List<DatabaseConfig> result = databaseConfigDao.findAll();
			Assert.assertTrue(result.size() > 0);
		}
		
		@Test
		public void test3LoadConnectionConfig() {
			DatabaseConfig databaseConfig= databaseConfigDao.loadConnectionConfig(GUINAME);
			Assert.assertTrue(databaseConfig != null);
		}
		
		@Test
		public void test4RemoveConnectionConfig() {
			databaseConfigDao.removeDatabaseConfig(config);
		}
}
