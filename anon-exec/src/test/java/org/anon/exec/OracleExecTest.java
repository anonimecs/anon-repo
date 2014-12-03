package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodDestoryOracle;
import org.anon.logic.AnonymisationMethodEncryptOracle;
import org.anon.vendor.OracleDbConnection;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:BaseExecTest.xml")
public class OracleExecTest extends BaseExecTest{
	
	@Autowired
	DataSource dataSourceOracle;
	
	@Autowired
	AnonConfig anonConfig;
	
	@Override
	DataSource getDataSource() {
		return dataSourceOracle;
	}
	
	TestTableCreatorOracle testTableCreator = new TestTableCreatorOracle();
	
	public TestTableCreatorOracle getTestTableCreator() {
		return testTableCreator;
	}


	@Test
	public void testAnonymisationMethodDestoryOracle() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceOracle);
		oracleExec.setAnonConfig(getTestAnonConfig("COMSIID", "VARCHAR2", "TMP_TABLE_A", new AnonymisationMethodDestoryOracle()));
		oracleExec.runAll();
		
		Assert.assertEquals("x", loadFromDbObject("select distinct COMSIID from TMP_TABLE_A", String.class));
		
	}

	@Test
	public void test_anonymiseLong() {
		AnonymisationMethodEncryptOracle anonymisationMethod = new AnonymisationMethodEncryptOracle();
	
		anonymisationMethod.setDataSource(dataSourceOracle);
		Object value = anonymisationMethod.anonymise(100l, getTestAnonimisedColumnInfo("COMSIID", "NUMBER", "TMP_TABLE_A",anonymisationMethod,OracleDbConnection.databaseSpecifics,anonConfig));
	
		System.out.println("Anonyised: " + value);
	}
	
	@Test
	public void test_anonymiseString() {
		AnonymisationMethodEncryptOracle anonymisationMethod = new AnonymisationMethodEncryptOracle();
	
		anonymisationMethod.setDataSource(dataSourceOracle);
		Object value = anonymisationMethod.anonymise("abax", getTestAnonimisedColumnInfo("COMSIID", "VARCHAR2", "TMP_TABLE_A",anonymisationMethod,OracleDbConnection.databaseSpecifics,anonConfig));
	
		System.out.println("Anonyised: " + value);
	}
	
	@Test
	public void testAnonymisationMethodEncryptString() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceOracle);
		oracleExec.setAnonConfig(getTestAnonConfig("COMSIID", "VARCHAR2", "TMP_TABLE_A",new AnonymisationMethodEncryptOracle()));
		oracleExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select distinct(COMSIID) from TMP_TABLE_A")
				);		
	}

	@Test
	public void testAnonymisationMethodEncryptNumber() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceOracle);
		oracleExec.setAnonConfig(getTestAnonConfig("ROLE_ID", "NUMBER", "TMP_TABLE_A",new AnonymisationMethodEncryptOracle()));
		oracleExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select distinct(ROLE_ID) from TMP_TABLE_A")
				);		
	}

	
	protected AnonConfig getTestAnonConfig(String colName, String colType, String tableName,AnonymisationMethod anonymisationMethod ) {
		return super.getTestAnonConfig(colName, colType, tableName, anonymisationMethod, OracleDbConnection.databaseSpecifics);
	}

	
}
