package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodDestorySybase;
import org.anon.logic.AnonymisationMethodEncryptSybase;
import org.anon.vendor.SybaseDbConnection;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class SybaseExecTest extends BaseExecTest{

	
	@Autowired
	DataSource dataSourceSybase;
	
	@Override
	DataSource getDataSource() {
		return dataSourceSybase;
	}
	
	TestTableCreatorSybase testTableCreator = new TestTableCreatorSybase();

	public TestTableCreatorSybase getTestTableCreator() {
		return testTableCreator;
	}


	@Test
	public void test0_FilesExists() {
		   Assert.assertNotNull("Test file missing", 
	               getClass().getResource("/TMP_TABLE_A_SYBASE.sql"));
		   Assert.assertNotNull("Test file missing", 
	               getClass().getResource("/TMP_TABLE_B_SYBASE.sql"));
	}
	
	@Test
	public void testAnonymisationMethodEncryptString() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		sybaseExec.setAnonConfig(getTestAnonConfig("SSY_SourceSystem", "varchar", "TMP_TABLE_A",new AnonymisationMethodEncryptSybase()));
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select distinct(SSY_SourceSystem) from TMP_TABLE_A")
				);		
	}

	@Test
	public void testAnonymisationMethodEncryptNum() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		sybaseExec.setAnonConfig(getTestAnonConfig("AIS_CountPosition", "int", "TMP_TABLE_B", new AnonymisationMethodEncryptSybase()));
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_CountPosition, SSY_SourceSystem from TMP_TABLE_B")
				);		
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_CountPosition, SSY_SourceSystem from TMP_TABLE_B")
				);		
	}


	@Test
	public void testAnonymisationMethodDestroyNum() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		sybaseExec.setAnonConfig(getTestAnonConfig("AIS_CountPosition", "int", "TMP_TABLE_B", new AnonymisationMethodDestorySybase()));
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_CountPosition, SSY_SourceSystem from TMP_TABLE_B")
				);		
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_CountPosition, SSY_SourceSystem from TMP_TABLE_B")
				);		
	}

	@Test
	public void testAnonymisationMethodDestroyDate() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		sybaseExec.setAnonConfig(getTestAnonConfig("AIS_LastInsert", "datetime", "TMP_TABLE_B", new AnonymisationMethodDestorySybase()));
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_LastInsert, SSY_SourceSystem from TMP_TABLE_B")
				);		
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_LastInsert, SSY_SourceSystem from TMP_TABLE_B")
				);		
	}


	@Test
	public void testAnonymisationMethodDestroyString() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		sybaseExec.setAnonConfig(getTestAnonConfig("AIS_ProductGroup", "varchar", "TMP_TABLE_B", new AnonymisationMethodDestorySybase()));
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_ProductGroup, SSY_SourceSystem from TMP_TABLE_B")
				);		
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_ProductGroup, SSY_SourceSystem from TMP_TABLE_B")
				);		
	}

	

	protected AnonConfig getTestAnonConfig(String colName, String colType, String tableName, AnonymisationMethod anonymisationMethod) {
		return getTestAnonConfig(colName, colType, tableName, anonymisationMethod, SybaseDbConnection.databaseSpecifics);
	}

	


}
