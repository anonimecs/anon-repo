package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymizationType;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodDestoryOracle;
import org.anon.logic.AnonymisationMethodEncryptOracle;
import org.anon.vendor.OracleDbConnection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class OracleExecTest extends BaseExecTest{
	
	@Autowired
	DataSource dataSourceOracle;
	
	@Autowired
	AnonConfig anonConfig;
	
	@Override
	DataSource getDataSource() {
		return dataSourceOracle;
	}

	@Before
	public void createTable(){
		String CREATE_TABLE = "create table USERS_TMP as select * from USERS";
		String CREATE_TABLE2 = "create table ROLE_TMP as select * from ROLE";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceOracle);
		jdbcTemplate.execute(CREATE_TABLE);
		jdbcTemplate.execute(CREATE_TABLE2);
		System.out.println("Tables created");
	}
	
	@After
	public void dropTable(){
		String DROP_TABLE = "drop table USERS_TMP";
		String DROP_TABLE2 = "drop table ROLE_TMP";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceOracle);
		jdbcTemplate.execute(DROP_TABLE);	
		jdbcTemplate.execute(DROP_TABLE2);	
		System.out.println("Table dropped");
	}
	
	@Test
	public void testAnonymisationMethodDestoryOracle() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceOracle);
		oracleExec.setAnonConfig(getTestAnonConfig("COMSIID", "VARCHAR2", "USERS_TMP", new AnonymisationMethodDestoryOracle()));
		oracleExec.runAll();
		
		Assert.assertEquals("x", loadFromDbObject("select distinct COMSIID from USERS_TMP", String.class));
		
	}

	@Test
	public void test_anonymiseLong() {
		AnonymisationMethodEncryptOracle anonymisationMethod = new AnonymisationMethodEncryptOracle();
	
		anonymisationMethod.setDataSource(dataSourceOracle);
		Object value = anonymisationMethod.anonymise(100l, getTestAnonimisedColumnInfo("COMSIID", "NUMBER", "USERS_TMP",anonymisationMethod,OracleDbConnection.databaseSpecifics,anonConfig));
	
		System.out.println("Anonyised: " + value);
	}
	
	@Test
	public void test_anonymiseString() {
		AnonymisationMethodEncryptOracle anonymisationMethod = new AnonymisationMethodEncryptOracle();
	
		anonymisationMethod.setDataSource(dataSourceOracle);
		Object value = anonymisationMethod.anonymise("abax", getTestAnonimisedColumnInfo("COMSIID", "VARCHAR2", "USERS_TMP",anonymisationMethod,OracleDbConnection.databaseSpecifics,anonConfig));
	
		System.out.println("Anonyised: " + value);
	}
	
	@Test
	public void testAnonymisationMethodEncryptString() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceOracle);
		oracleExec.setAnonConfig(getTestAnonConfig("COMSIID", "VARCHAR2", "USERS_TMP",new AnonymisationMethodEncryptOracle()));
		oracleExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select distinct(COMSIID) from USERS_TMP")
				);		
	}

	@Test
	public void testAnonymisationMethodEncryptNumber() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceOracle);
		oracleExec.setAnonConfig(getTestAnonConfig("ROLE_ID", "NUMBER", "USERS_TMP",new AnonymisationMethodEncryptOracle()));
		oracleExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select distinct(ROLE_ID) from USERS_TMP")
				);		
	}

	
	protected AnonConfig getTestAnonConfig(String colName, String colType, String tableName,AnonymisationMethod anonymisationMethod ) {
		return super.getTestAnonConfig(colName, colType, tableName, anonymisationMethod, OracleDbConnection.databaseSpecifics);
	}
	
}
