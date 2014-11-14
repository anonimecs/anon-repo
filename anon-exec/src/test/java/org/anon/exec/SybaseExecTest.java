package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodDestorySybase;
import org.anon.logic.AnonymisationMethodEncryptSybase;
import org.anon.vendor.SybaseDbConnection;
import org.junit.After;
import org.junit.Before;
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
	
	@Before
	public void createTable(){
		String CREATE_TABLE = "select * into TMP_CS_SOURCE_SYSTEM from SOURCE_SYSTEM";
		String CREATE_TABLE2 = "select * into TMP_ADB_IMPORT_STATUS from ADB_IMPORT_STATUS";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceSybase);
		jdbcTemplate.execute(CREATE_TABLE);
		jdbcTemplate.execute(CREATE_TABLE2);
		System.out.println("Tables created");
	}
	
	@After
	public void dropTable(){
		String DROP_TABLE = "drop table TMP_CS_SOURCE_SYSTEM";
		String DROP_TABLE2 = "drop table TMP_ADB_IMPORT_STATUS";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceSybase);
		jdbcTemplate.execute(DROP_TABLE);	
		jdbcTemplate.execute(DROP_TABLE2);	
		System.out.println("Table dropped");
	}
	
//	@Test
//	public void simpleTest(){
//		System.out.println(
//				new JdbcTemplate(dataSourceSybase).queryForInt("select count (*) from TMP_CS_POSITION")
//		);
//	}
	
	@Test
	public void testAnonymisationMethodEncryptString() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		sybaseExec.setAnonConfig(getTestAnonConfig("SSY_SourceSystem", "varchar", "TMP_CS_SOURCE_SYSTEM",new AnonymisationMethodEncryptSybase()));
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select distinct(SSY_SourceSystem) from TMP_CS_SOURCE_SYSTEM")
				);		
	}

	@Test
	public void testAnonymisationMethodEncryptNum() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		sybaseExec.setAnonConfig(getTestAnonConfig("AIS_CountPosition", "int", "TMP_ADB_IMPORT_STATUS", new AnonymisationMethodEncryptSybase()));
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_CountPosition, SSY_SourceSystem from TMP_ADB_IMPORT_STATUS")
				);		
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_CountPosition, SSY_SourceSystem from TMP_ADB_IMPORT_STATUS")
				);		
	}


	@Test
	public void testAnonymisationMethodDestroyNum() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		sybaseExec.setAnonConfig(getTestAnonConfig("AIS_CountPosition", "int", "TMP_ADB_IMPORT_STATUS", new AnonymisationMethodDestorySybase()));
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_CountPosition, SSY_SourceSystem from TMP_ADB_IMPORT_STATUS")
				);		
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_CountPosition, SSY_SourceSystem from TMP_ADB_IMPORT_STATUS")
				);		
	}

	@Test
	public void testAnonymisationMethodDestroyDate() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		sybaseExec.setAnonConfig(getTestAnonConfig("AIS_LastInsert", "datetime", "TMP_ADB_IMPORT_STATUS", new AnonymisationMethodDestorySybase()));
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_LastInsert, SSY_SourceSystem from TMP_ADB_IMPORT_STATUS")
				);		
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_LastInsert, SSY_SourceSystem from TMP_ADB_IMPORT_STATUS")
				);		
	}


	@Test
	public void testAnonymisationMethodDestroyString() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		sybaseExec.setAnonConfig(getTestAnonConfig("AIS_ProductGroup", "varchar", "TMP_ADB_IMPORT_STATUS", new AnonymisationMethodDestorySybase()));
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_ProductGroup, SSY_SourceSystem from TMP_ADB_IMPORT_STATUS")
				);		
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_ProductGroup, SSY_SourceSystem from TMP_ADB_IMPORT_STATUS")
				);		
	}

	

	protected AnonConfig getTestAnonConfig(String colName, String colType, String tableName, AnonymisationMethod anonymisationMethod) {
		return getTestAnonConfig(colName, colType, tableName, anonymisationMethod, SybaseDbConnection.databaseSpecifics);
	}
	


}
