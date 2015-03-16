package org.anon;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.anon.data.AnonymizationType;
import org.anon.data.Database;
import org.anon.logic.AnonymisationMethod;
import org.anon.persistence.DerbyInMemCreator;
import org.anon.persistence.dao.DatabaseConfigDao;
import org.anon.persistence.dao.EntitiesDao;
import org.anon.persistence.dao.UserDao;
import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseConnection;
import org.anon.persistence.data.SecurityUser;
import org.anon.test.AnonUnitTest;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;


@ContextConfiguration(locations = { "classpath:spring-persistence-test.xml","classpath:spring-test-base.xml" })
public abstract class DbEntitiesTest extends AbstractJUnit4SpringContextTests implements AnonUnitTest{

	protected static final String GUINAME = "testguiname";
	protected static final String CONFIGNAME = "testconfigname";

		@Autowired
		EntitiesDao entitiesDao;

		@Autowired
		UserDao userDao;
		

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
			SecurityUser user = userDao.loadUserById(1l);
			Assert.assertNotNull(user);

			
			DatabaseConnection databaseConnection = new DatabaseConnection();
			databaseConnection.setUrl("test.anon.org");
			databaseConnection.setLogin("anon");
			databaseConnection.setPassword("anon");
			databaseConnection.setVendor(Database.SYBASE);
			databaseConnection.setVersion("16.0");
			databaseConnection.setGuiName(GUINAME);
			databaseConnection.setSecurityUser(user);
			
			
			DatabaseConfig config = new DatabaseConfig();
			config.setDatabaseConnection(databaseConnection);
			config.setConfigurationName(CONFIGNAME);
			config.setDefaultSchema("test_default_schema");
			config.setSecurityUser(user);
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
		
	    protected List<Map<String, Object>> loadFromDb(String sqlQuery) {
	        return new JdbcTemplate(dataSource).query(sqlQuery, new ColumnMapRowMapper());
	    }


}
