package org.anon.exec;

import org.anon.data.AnonConfig;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodDestoryOracle;
import org.anon.logic.AnonymisationMethodEncryptOracle;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;


public class OracleExecTest extends OracleExecTestBase{
	


	@Test
	public void testAnonymisationMethodDestoryOracle() {
		OracleExec oracleExec = createExec();
		oracleExec.setExecConfig(getTestAnonConfig("COMSIID", "VARCHAR2", "TMP_TABLE_B", new AnonymisationMethodDestoryOracle()));
		oracleExec.runAll();
		
		Assert.assertEquals("x", loadFromDbObject("select distinct COMSIID from TMP_TABLE_B", String.class));
		
	}

	@Test
	public void test_anonymiseLong() {
		AnonymisationMethodEncryptOracle anonymisationMethod = new AnonymisationMethodEncryptOracle();
	
		anonymisationMethod.setDataSource(dataSourceOracle);
		Object value = anonymisationMethod.anonymise(100l, getTestAnonimisedColumnInfo("COMSIID", "NUMBER", "TMP_TABLE_B",anonymisationMethod,DatabaseSpecifics.OracleSpecific,anonConfig));
	
		System.out.println("Anonyised: " + value);
	}
	
	@Test
	public void test_anonymiseString() {
		AnonymisationMethodEncryptOracle anonymisationMethod = new AnonymisationMethodEncryptOracle();
	
		anonymisationMethod.setDataSource(dataSourceOracle);
		Object value = anonymisationMethod.anonymise("abax", getTestAnonimisedColumnInfo("COMSIID", "VARCHAR2", "TMP_TABLE_B",anonymisationMethod,DatabaseSpecifics.OracleSpecific,anonConfig));
	
		System.out.println("Anonyised: " + value);
	}
	
	@Test
	public void testAnonymisationMethodEncryptString() {
		OracleExec oracleExec = createExec();
		oracleExec.setExecConfig(getTestAnonConfig("COMSIID", "VARCHAR2", "TMP_TABLE_B",new AnonymisationMethodEncryptOracle()));
		oracleExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select distinct(COMSIID) from TMP_TABLE_B")
				);		
	}

	@Test
	public void testAnonymisationMethodEncryptNumber() {
		OracleExec oracleExec =  createExec();
		oracleExec.setExecConfig(getTestAnonConfig("ROLE_ID", "NUMBER", "TMP_TABLE_B",new AnonymisationMethodEncryptOracle()));
		oracleExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select distinct(ROLE_ID) from TMP_TABLE_B")
				);		
	}

	
	protected AnonConfig getTestAnonConfig(String colName, String colType, String tableName,AnonymisationMethod anonymisationMethod ) {
		return super.getTestAnonConfig(colName, colType, tableName, anonymisationMethod, DatabaseSpecifics.OracleSpecific);
	}

	
}
