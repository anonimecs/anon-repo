package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodDestoryMySql;
import org.anon.logic.AnonymisationMethodEncryptOracle;
import org.anon.vendor.MySqlDbConnection;
import org.anon.vendor.OracleDbConnection;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:BaseExecTest.xml")
public class MySqlExecTest extends BaseExecTest{
	
	@Autowired
	DataSource dataSourceMySql;
	
	@Autowired
	AnonConfig anonConfig;
	
	@Autowired
	ExecFactory execFactory;
	
	@Override
	DataSource getDataSource() {
		return dataSourceMySql;
	}
	
	TestTableCreatorMySql testTableCreator = new TestTableCreatorMySql();
	
	public TestTableCreatorMySql getTestTableCreator() {
		return testTableCreator;
	}


	@Test
	public void testAnonymisationMethodDestory() {
		MySqlExec mysqlExec = (MySqlExec)execFactory.createExec(MySqlDbConnection.databaseSpecifics);
		mysqlExec.setDataSource(dataSourceMySql);
		mysqlExec.setAnonConfig(getTestAnonConfig("COMSIID", "VARCHAR2", "TMP_TABLE_A", new AnonymisationMethodDestoryMySql()));
		mysqlExec.runAll();
		
		Assert.assertEquals("x", loadFromDbObject("select distinct COMSIID from TMP_TABLE_A", String.class));
		
	}

	@Ignore // until method is created
	@Test
	public void test_anonymiseLong() {
		AnonymisationMethodEncryptOracle anonymisationMethod = new AnonymisationMethodEncryptOracle();
	
		anonymisationMethod.setDataSource(dataSourceMySql);
		Object value = anonymisationMethod.anonymise(100l, getTestAnonimisedColumnInfo("COMSIID", "NUMBER", "TMP_TABLE_A",anonymisationMethod,OracleDbConnection.databaseSpecifics,anonConfig));
	
		System.out.println("Anonyised: " + value);
	}
	
	@Ignore // until method is created
	@Test
	public void test_anonymiseString() {
		AnonymisationMethodEncryptOracle anonymisationMethod = new AnonymisationMethodEncryptOracle();
	
		anonymisationMethod.setDataSource(dataSourceMySql);
		Object value = anonymisationMethod.anonymise("abax", getTestAnonimisedColumnInfo("COMSIID", "VARCHAR2", "TMP_TABLE_A",anonymisationMethod,OracleDbConnection.databaseSpecifics,anonConfig));
	
		System.out.println("Anonyised: " + value);
	}
	
	@Ignore // until method is created
	@Test
	public void testAnonymisationMethodEncryptString() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceMySql);
		oracleExec.setAnonConfig(getTestAnonConfig("COMSIID", "VARCHAR2", "TMP_TABLE_A",new AnonymisationMethodEncryptOracle()));
		oracleExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceMySql).queryForList("select distinct(COMSIID) from TMP_TABLE_A")
				);		
	}

	@Ignore // until method is created
	@Test
	public void testAnonymisationMethodEncryptNumber() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceMySql);
		oracleExec.setAnonConfig(getTestAnonConfig("ROLE_ID", "NUMBER", "TMP_TABLE_A",new AnonymisationMethodEncryptOracle()));
		oracleExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceMySql).queryForList("select distinct(ROLE_ID) from TMP_TABLE_A")
				);		
	}

	
	protected AnonConfig getTestAnonConfig(String colName, String colType, String tableName,AnonymisationMethod anonymisationMethod ) {
		return super.getTestAnonConfig(colName, colType, tableName, anonymisationMethod, OracleDbConnection.databaseSpecifics);
	}

	
}
