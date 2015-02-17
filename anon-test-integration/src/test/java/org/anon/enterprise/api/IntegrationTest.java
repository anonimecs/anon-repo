package org.anon.enterprise.api;

import java.util.Collections;
import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodDestoryMySql;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.service.DatabaseConfigService;
import org.anon.service.EditedTableService;
import org.anon.service.ServiceException;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration(locations = { "classpath:spring-anon-rmi-client.xml" })
public class IntegrationTest extends AbstractJUnit4SpringContextTests{

	@Autowired
	DatabaseConfigService databaseConfigService;
	
	@Autowired
	EditedTableService editedTableService;
	
	@Autowired
	AnonServer anonServer;
	
	protected static DatabaseConfig savedDatabaseConfig;

	protected static long id = 0;

	@Test
	public void test0_loadConnectionConfigs() {
		List<DatabaseConfig> list = databaseConfigService.loadConnectionConfigs();
		// parse the highest ID
		for (DatabaseConfig databaseConfig : list) {
			if(id < databaseConfig.getId()){
				id = databaseConfig.getId();
			}
		}
	}

	
	@Test
	public void test1_CreateDatabaseConfigration() throws ServiceException {
		DatabaseConfig databaseConfig = IntegrationMocks.createDatabaseConfigMySql(id);
		databaseConfigService.addDatabaseConfig(databaseConfig);
		savedDatabaseConfig = databaseConfig;
	}
	
	
	@Test
	public void test2_addAnonymisationMethod(){
		DatabaseTableInfo editedTable= new DatabaseTableInfo();
		editedTable.setName("dept_manager");
		editedTable.setSchema("employees");
		DatabaseColumnInfo editedColumn = new DatabaseColumnInfo("to_date", "date", DatabaseSpecifics.MySqlSpecific);
		editedTable.addColumn(editedColumn);

		AnonymisedColumnInfo anonymizedColumn = new AnonymisedColumnInfo(editedColumn);
		
		AnonymisationMethod anonymisationMethod = new AnonymisationMethodDestoryMySql();
		
		
		DatabaseConfig databaseConfig = databaseConfigService.loadConnectionConfig(savedDatabaseConfig.getGuiName());
		
		anonServer.useDatabaseConfig(databaseConfig);
		
		editedTableService.addAnonymisation(editedTable,anonymizedColumn,Collections.EMPTY_LIST, anonymisationMethod);
	}
	

	
	@Test
	public void test3_DeleteNotExecutedDatabaseConfigration()  throws ServiceException{
			databaseConfigService.deleteDatabaseConfig(savedDatabaseConfig.getGuiName());
	}
	
	@Test
	public void test4_createAndExecuteConfig() throws ServiceException{
		test1_CreateDatabaseConfigration();
		test2_addAnonymisationMethod();
		anonServer.runAll(savedDatabaseConfig.getGuiName());
	}
		
	@Test
	public void test5_DeleteExecutedDatabaseConfigration() {
		try{
			databaseConfigService.deleteDatabaseConfig(savedDatabaseConfig.getGuiName());
			Assert.fail("Exectuted config should not be deletable: " + savedDatabaseConfig);
		}
		catch(ServiceException e){
			System.out.println(e.getResultMessages());
		}
	}


}