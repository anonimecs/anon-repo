package org.anon.exec;

import org.anon.data.AnonConfig;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodDestoryMySql;
import org.anon.logic.AnonymisationMethodEncryptMySql;
import org.anon.vendor.MySqlDbConnection;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class MySqlExecTest extends MySqlExecTestBase{

	@Test
	public void testAnonymisationMethodDestory() {
		MySqlExec mysqlExec = createExec();
		mysqlExec.setExecConfig(getTestAnonConfig("COMSIID", "VARCHAR2", "TMP_TABLE_B", new AnonymisationMethodDestoryMySql()));
		mysqlExec.runAll();
		Assert.assertEquals("x", loadFromDbObject("select distinct COMSIID from TMP_TABLE_B", String.class));
	}

	@Test
	public void test_anonymiseLong() {
		AnonymisationMethodEncryptMySql anonymisationMethod = new AnonymisationMethodEncryptMySql();
		Long input = 100l;
		anonymisationMethod.setDataSource(dataSourceMySql);
		Object value = anonymisationMethod.anonymise(input, getTestAnonimisedColumnInfo("COMSIID", "TINYINT", "TMP_TABLE_B",anonymisationMethod,MySqlDbConnection.databaseSpecifics,anonConfig));
		System.out.println("Anonyised: " + value);
		Assert.assertNotEquals(input, value);
	}
	
	@Test
	public void test_anonymiseString() {
		AnonymisationMethodEncryptMySql anonymisationMethod = new AnonymisationMethodEncryptMySql();
		String input = "abax";
		anonymisationMethod.setDataSource(dataSourceMySql);
		Object value = anonymisationMethod.anonymise(input, getTestAnonimisedColumnInfo("COMSIID", "VARCHAR", "TMP_TABLE_B",anonymisationMethod,MySqlDbConnection.databaseSpecifics,anonConfig));
		System.out.println("Anonyised: " + value);
		Assert.assertNotEquals(input, value);
	}
	
	@Test
	public void testAnonymisationMethodEncryptString() {
		MySqlExec mysqlExec = createExec();
		mysqlExec.setExecConfig(getTestAnonConfig("COMSIID", "VARCHAR", "TMP_TABLE_B",new AnonymisationMethodEncryptMySql()));
		mysqlExec.runAll();
		System.out.println(
			new JdbcTemplate(dataSourceMySql).queryForList("select distinct(COMSIID) from TMP_TABLE_B")
		);		
	}

	@Test
	public void testAnonymisationMethodEncryptNumber() {
		MySqlExec mysqlExec = createExec();
		mysqlExec.setExecConfig(getTestAnonConfig("ROLE_ID", "NUMERIC", "TMP_TABLE_B",new AnonymisationMethodEncryptMySql()));
		mysqlExec.runAll();
		System.out.println(
			new JdbcTemplate(dataSourceMySql).queryForList("select distinct(ROLE_ID) from TMP_TABLE_B")
		);		
	}

	protected AnonConfig getTestAnonConfig(String colName, String colType, String tableName,AnonymisationMethod anonymisationMethod ) {
		return super.getTestAnonConfig(colName, colType, tableName, anonymisationMethod, MySqlDbConnection.databaseSpecifics);
	}
}
