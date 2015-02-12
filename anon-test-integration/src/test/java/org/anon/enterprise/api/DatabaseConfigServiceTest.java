package org.anon.enterprise.api;

import org.anon.persistence.data.DatabaseConfig;
import org.anon.service.DatabaseConfigService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration(locations = { "classpath:spring-anon-rmi-client.xml" })
public class DatabaseConfigServiceTest extends AbstractJUnit4SpringContextTests{

	@Autowired
	DatabaseConfigService databaseConfigService;
	
	protected static DatabaseConfig savedDatabaseConfig;
	
	@Test
	public void test1_CreateDatabaseConfigration() {
		DatabaseConfig databaseConfig = IntegrationMocks.createDatabaseConfigMySql();
		databaseConfigService.addDatabaseConfig(databaseConfig);
		savedDatabaseConfig = databaseConfig;
	}
	
	
	@Test
	public void test9_DeleteDatabaseConfigration() {
		databaseConfigService.deleteDatabaseConfig(savedDatabaseConfig.getGuiName());
	}



}
