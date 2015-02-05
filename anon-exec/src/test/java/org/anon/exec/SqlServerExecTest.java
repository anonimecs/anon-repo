package org.anon.exec;

import org.anon.data.AnonConfig;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodDestorySqlServer;
import org.anon.vendor.SqlServerDbConnection;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlServerExecTest extends SqlServerExecTestBase{

	


	@Test
	public void test0_FilesExists() {
		   Assert.assertNotNull("Test file missing", 
	               getClass().getResource("/TMP_TABLE_A_SYBASE.sql"));
		   Assert.assertNotNull("Test file missing", 
	               getClass().getResource("/TMP_TABLE_B_SYBASE.sql"));
	}
	
//	@Test
//	public void testAnonymisationMethodEncryptString() {
//		SqlServerExec sqlServerExec = createSqlServerExec();
//		sqlServerExec.setExecConfig(getTestAnonConfig("SSY_SourceSystem", "varchar", "TMP_TABLE_A",new AnonymisationMethodEncryptSqlServer()));
//		sqlServerExec.runAll();
//		System.out.println(
//				new JdbcTemplate(dataSourceSqlServer).queryForList("select distinct(SSY_SourceSystem) from TMP_TABLE_A")
//				);		
//	}
//
//
//	@Test
//	public void testAnonymisationMethodEncryptNum() {
//		SqlServerExec sqlServerExec = createSqlServerExec();
//		sqlServerExec.setExecConfig(getTestAnonConfig("AIS_CountPosition", "int", "TMP_TABLE_B", new AnonymisationMethodEncryptSqlServer()));
//		System.out.println(
//				new JdbcTemplate(dataSourceSqlServer).queryForList("select AIS_CountPosition, SSY_SourceSystem from TMP_TABLE_B")
//				);		
//		sqlServerExec.runAll();
//		System.out.println(
//				new JdbcTemplate(dataSourceSqlServer).queryForList("select AIS_CountPosition, SSY_SourceSystem from TMP_TABLE_B")
//				);		
//	}


	@Test
	public void testAnonymisationMethodDestroyNum() {
		SqlServerExec sqlServerExec = createSqlServerExec();
		sqlServerExec.setExecConfig(getTestAnonConfig("AIS_CountPosition", "int", "TMP_TABLE_B", new AnonymisationMethodDestorySqlServer()));
		System.out.println(
				new JdbcTemplate(dataSourceSqlServer).queryForList("select AIS_CountPosition, SSY_SourceSystem from TMP_TABLE_B")
				);		
		sqlServerExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSqlServer).queryForList("select AIS_CountPosition, SSY_SourceSystem from TMP_TABLE_B")
				);		
	}

	@Test
	public void testAnonymisationMethodDestroyDate() {
		SqlServerExec sqlServerExec = createSqlServerExec();
		sqlServerExec.setExecConfig(getTestAnonConfig("AIS_LastInsert", "datetime", "TMP_TABLE_B", new AnonymisationMethodDestorySqlServer()));
		System.out.println(
				new JdbcTemplate(dataSourceSqlServer).queryForList("select AIS_LastInsert, SSY_SourceSystem from TMP_TABLE_B")
				);		
		sqlServerExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSqlServer).queryForList("select AIS_LastInsert, SSY_SourceSystem from TMP_TABLE_B")
				);		
	}


	@Test
	public void testAnonymisationMethodDestroyString() {
		SqlServerExec sqlServerExec = createSqlServerExec();
		sqlServerExec.setExecConfig(getTestAnonConfig("AIS_ProductGroup", "varchar", "TMP_TABLE_B", new AnonymisationMethodDestorySqlServer()));
		System.out.println(
				new JdbcTemplate(dataSourceSqlServer).queryForList("select AIS_ProductGroup, SSY_SourceSystem from TMP_TABLE_B")
				);		
		sqlServerExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSqlServer).queryForList("select AIS_ProductGroup, SSY_SourceSystem from TMP_TABLE_B")
				);		
	}

	

	protected AnonConfig getTestAnonConfig(String colName, String colType, String tableName, AnonymisationMethod anonymisationMethod) {
		return getTestAnonConfig(colName, colType, tableName, anonymisationMethod, SqlServerDbConnection.databaseSpecifics);
	}

	


}
