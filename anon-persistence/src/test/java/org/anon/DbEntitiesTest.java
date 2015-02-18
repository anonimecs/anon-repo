package org.anon;

import java.io.IOException;

import javax.sql.DataSource;

import org.anon.data.AnonymizationType;
import org.anon.data.Database;
import org.anon.logic.AnonymisationMethod;
import org.anon.persistence.DerbyInMemCreator;
import org.anon.persistence.dao.DatabaseConfigDao;
import org.anon.persistence.dao.EntitiesDao;
import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.test.AnonUnitTest;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;


@ContextConfiguration(locations = { "classpath:spring-persistence-test.xml","classpath:spring-test-base.xml" })
public abstract class DbEntitiesTest extends AbstractJUnit4SpringContextTests implements AnonUnitTest{

		protected static final String GUINAME = "test";

		@Autowired
		EntitiesDao entitiesDao;
		
		@Autowired
		DatabaseConfigDao databaseConfigDao;
		
		@Autowired
		DataSource dataSource;
		
		@Autowired
		DerbyInMemCreator derbyInMemCreator;
		
		static Long methodId;
		static AnonymisationMethodData anonymisationMethodData;
		static AnonymisedColumnData anonymisedColumnData;
		static DatabaseConfig databaseConfig;

		protected DatabaseConfig createDatabaseConfig() {
			DatabaseConfig config = new DatabaseConfig();
			config.setUrl("test.anon.org");
			config.setLogin("anon");
			config.setPassword("anon");
			config.setVendor(Database.SYBASE);
			config.setVersion("16.0");
			config.setGuiName(GUINAME);
			config.setDefaultSchema("test_default_schema");
			return config;
		}

		protected void do_test1InsertAnonymisationMethodData() {
			databaseConfig =  createDatabaseConfig();
			databaseConfigDao.addDatabaseConfig(databaseConfig);
			
			anonymisationMethodData = new AnonymisationMethodData();
			anonymisationMethodData.setDatabaseConfigId(databaseConfig.getId());
			anonymisationMethodData.setAnonymizationType(AnonymizationType.DESTROY);
			anonymisationMethodData.setAnonMethodClass(AnonymisationMethod.class.getName());
			
			anonymisedColumnData = new AnonymisedColumnData();
			anonymisedColumnData.setColumnName("TESTCOLNAME");
			anonymisedColumnData.setTableName("TestTableName");
			anonymisedColumnData.setSchemaName("TestSchema");
			anonymisedColumnData.setColumnType("VARCHAR");
			
			anonymisationMethodData.addColumn(anonymisedColumnData);
			entitiesDao.save(anonymisationMethodData);
			methodId = anonymisationMethodData.getId();
			
			Assert.assertNotNull("id was assigned", methodId);
			Assert.assertNotNull("id was assigned", anonymisedColumnData.getId());
		}
		
		protected void recreateInMemDb() throws IOException{
			try {
				derbyInMemCreator.dropTables();
			} catch (Exception e) {
				e.printStackTrace();
			}
			derbyInMemCreator.createTables();
		}
		

}
