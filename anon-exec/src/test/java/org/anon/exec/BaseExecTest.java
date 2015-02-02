package org.anon.exec;

import java.io.IOException;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:BaseExecTest.xml")
public abstract class BaseExecTest extends BaseDbTest{
	

	
	protected AnonConfig getTestAnonConfig(String colName, String colType, String tableName, AnonymisationMethod anonymisationMethod, DatabaseSpecifics databaseSpecifics) {
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		
		getTestAnonimisedColumnInfo(colName, colType, tableName,
				anonymisationMethod, databaseSpecifics, anonConfig);
		
		anonConfig.addAnonMethod(anonymisationMethod);
		
		return anonConfig;
	}

	protected AnonymisedColumnInfo getTestAnonimisedColumnInfo(String colName, String colType,String tableName, AnonymisationMethod anonymisationMethod, DatabaseSpecifics databaseSpecifics, AnonConfig anonConfig) {
		DatabaseTableInfo table = new DatabaseTableInfo();
		DatabaseColumnInfo databaseColumnInfo = new DatabaseColumnInfo(colName, colType, databaseSpecifics);
		table.addColumn(databaseColumnInfo);
		
		anonConfig.addTable(table);
		table.setName(tableName);
		AnonymisedColumnInfo anonymizedColumn = new AnonymisedColumnInfo(databaseColumnInfo);
		anonymisationMethod.addColumn(anonymizedColumn);
		table.addAnonymisedColumn(anonymizedColumn);
		
		return anonymizedColumn;
	}

	@Before
	public void createTable() throws IOException{
		dropTables();
		getTestTableCreator().createTables(new JdbcTemplate(getDataSource()));
	}
	
	@After
	public void dropTables() {
		try{
			getTestTableCreator().dropTableB(new JdbcTemplate(getDataSource()));
		}
		catch(Exception e){}
		try {
			getTestTableCreator().dropTableA(new JdbcTemplate(getDataSource()));
		} catch (Exception e) {}
	}
	
	@Test
	public void test1_simpleTest() {
		Assert.assertEquals(getTestTableCreator().getRowcountTableB(), new JdbcTemplate(getDataSource()).queryForInt("select count(*) from TMP_TABLE_B"));
		Assert.assertEquals(getTestTableCreator().getRowcountTableA(), new JdbcTemplate(getDataSource()).queryForInt("select count(*) from TMP_TABLE_A"));
	}
	
	protected abstract TestTableCreator getTestTableCreator();
	





}
