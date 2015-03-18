package org.anon.enterprise.api;

import java.util.Collections;
import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodDestoryMySql;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseConnection;
import org.anon.persistence.data.SecurityUser;
import org.anon.service.DatabaseConfigService;
import org.anon.service.EditedTableService;
import org.anon.service.ServiceException;
import org.anon.service.admin.UserService;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration(locations = { "classpath:IntegrationTest.xml" })
public class IntegrationTest extends AbstractJUnit4SpringContextTests{


	@Autowired
	DatabaseConfigService databaseConfigService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	EditedTableService editedTableService;
	
	@Autowired
	AnonServer anonServer;
	
	@Value("${mysql.test.url.partial}") String url;
	@Value("${mysql.test.user}") String login;
	@Value("${mysql.test.passwd}") String passw; 
	@Value("${mysql.inttest.schema}") String defaultSchema; 
	@Value("${mysql.inttest.table}") String tableName;
	@Value("${mysql.inttest.col}") String columnName;
	@Value("${mysql.inttest.col.type}") String columnType;
	
	protected static DatabaseConfig savedDatabaseConfig;
	protected static DatabaseConnection savedDatabaseConnection;
	protected static long configId = 0;
	protected static long connectionId = 0;

	@Test
	public void test0_loadConfigId() {
		List<DatabaseConfig> list = databaseConfigService.loadDatabaseConfigsForUser();
		// parse the highest ID
		for (DatabaseConfig databaseConfig : list) {
			if(configId < databaseConfig.getId()){
				configId = databaseConfig.getId();
			}
		}
	}

	@Test
	public void test0_loadconnectionId() {
		List<DatabaseConnection> list = databaseConfigService.loadDatabaseConnectionsForUser();
		// parse the highest ID
		for (DatabaseConnection databaseConnection : list) {
			if(connectionId < databaseConnection.getId()){
				connectionId = databaseConnection.getId();
			}
		}
	}

	@Test
	public void test1_CreateDatabaseConnection() throws ServiceException {
		SecurityUser securityUser = userService.loadUser("admin");
		DatabaseConnection databaseConnection  = IntegrationMocks.createConnection(connectionId, url, login, passw, securityUser);
		databaseConfigService.addDatabaseConnection(databaseConnection);
		savedDatabaseConnection = databaseConfigService.loadDatabaseConnection(databaseConnection.getGuiName());
	}
	

	
	@Test
	public void test2_CreateDatabaseConfigration() throws ServiceException {
		SecurityUser securityUser = userService.loadUser("admin");
		DatabaseConfig databaseConfig = IntegrationMocks.createDatabaseConfigMySql(configId, defaultSchema, savedDatabaseConnection, securityUser);
		databaseConfigService.addDatabaseConfig(databaseConfig);
		savedDatabaseConfig = databaseConfigService.loadDatabaseConfig(databaseConfig.getConfigurationName());
	}
	
	
	@Test
	public void test3_addAnonymisationMethod(){
		DatabaseTableInfo editedTable= new DatabaseTableInfo();
		editedTable.setName(tableName);
		editedTable.setSchema(defaultSchema);
		DatabaseColumnInfo editedColumn = new DatabaseColumnInfo(columnName, columnType, DatabaseSpecifics.MySqlSpecific);
		editedTable.addColumn(editedColumn);

		AnonymisedColumnInfo anonymizedColumn = new AnonymisedColumnInfo(editedColumn);
		
		AnonymisationMethod anonymisationMethod = new AnonymisationMethodDestoryMySql();
		
		
		DatabaseConfig databaseConfig = databaseConfigService.loadDatabaseConfig(savedDatabaseConfig.getConfigurationName());
		
		anonServer.useDatabaseConfig(databaseConfig);
		
		editedTableService.addAnonymisation(editedTable,anonymizedColumn,Collections.EMPTY_LIST, anonymisationMethod);
	}
	

	
	@Test
	public void test4_DeleteNotExecutedDatabaseConfigration()  throws ServiceException{
			databaseConfigService.deleteDatabaseConfig(savedDatabaseConfig.getConfigurationName());
	}
	
	@Test
	public void test5_createAndExecuteConfig() throws ServiceException{
		test2_CreateDatabaseConfigration();
		test3_addAnonymisationMethod();
		anonServer.runAll(savedDatabaseConfig.getConfigurationName());
	}
		
	@Test
	public void test6_DeleteExecutedDatabaseConfigration() {
		try{
			databaseConfigService.deleteDatabaseConfig(savedDatabaseConfig.getConfigurationName());
			Assert.fail("Exectuted config should not be deletable: " + savedDatabaseConfig);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


}
