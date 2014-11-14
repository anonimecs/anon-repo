package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodReshuffleOracle;
import org.anon.vendor.OracleDbConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class OracleRehuffleTest extends BaseExecTest{

	
	@Autowired
	DataSource dataSourceOracle;
	
	@Override
	DataSource getDataSource() {
		return dataSourceOracle;
	}
	
	@Before
	public void createTable(){
		String CREATE_TABLE = "create table TEMP_ROLE as select * from ROLE";
		String CREATE_TABLE2 = "create table TEMP_USERS as select * from USERS";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceOracle);
		jdbcTemplate.execute(CREATE_TABLE);
		jdbcTemplate.execute(CREATE_TABLE2);
		System.out.println("Tables created");
	}
	
	@After
	public void dropTable(){
		String DROP_TABLE = "drop table TEMP_USERS";
		String DROP_TABLE2 = "drop table TEMP_ROLE";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceOracle);
		jdbcTemplate.execute(DROP_TABLE);	
		jdbcTemplate.execute(DROP_TABLE2);	
		System.out.println("2 Tables dropped. ");
	}


	
	@Test
	public void testAnonymisationReshuffleNumber() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceOracle);
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		AnonymisationMethodReshuffleOracle anonymisationMethod = new AnonymisationMethodReshuffleOracle();
		anonymisationMethod.setPassword("testpassword2");
		anonConfig.addAnonMethod(anonymisationMethod);
		
		addTable("ID", "number", "TEMP_ROLE", anonymisationMethod,anonConfig);
		addTable("ROLE_ID", "number", "TEMP_USERS", anonymisationMethod,anonConfig);
		
		oracleExec.setAnonConfig(anonConfig);
		
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select* from TEMP_ROLE")
				);		
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select distinct ROLE_ID from TEMP_USERS")
				);		
		oracleExec.runAll();

		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select * from TEMP_ROLE")
				);		
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select distinct ROLE_ID from TEMP_USERS")
				);		
	}
	
	@Test
	public void testAnonymisationReshuffleDate() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceOracle);
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		AnonymisationMethodReshuffleOracle anonymisationMethod = new AnonymisationMethodReshuffleOracle();
		anonymisationMethod.setPassword("testpassword2");
		anonConfig.addAnonMethod(anonymisationMethod);
		
		addTable("MODIFIEDAT", "DATE", "TEMP_USERS", anonymisationMethod,anonConfig);
		
		oracleExec.setAnonConfig(anonConfig);
		
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select * from TEMP_USERS")
				);		
		oracleExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select * from TEMP_USERS")
				);		
	}


	
	private void addTable(String colName, String colType, String tableName, AnonymisationMethod anonymisationMethod, AnonConfig anonConfig) {
		DatabaseTableInfo table = new DatabaseTableInfo();
		DatabaseColumnInfo databaseColumnInfo = new DatabaseColumnInfo(colName, colType, OracleDbConnection.databaseSpecifics);
		table.addColumn(databaseColumnInfo);
		
		anonConfig.addTable(table);
		table.setName(tableName);
		AnonymisedColumnInfo anonymizedColumn = new AnonymisedColumnInfo(databaseColumnInfo);
		anonymisationMethod.addColumn(anonymizedColumn);
		table.addAnonymisedColumn(anonymizedColumn);
		
		

	}

}
