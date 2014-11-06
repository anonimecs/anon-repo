package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodReshuffleSybase;
import org.anon.vendor.SybaseDbConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class SybaseRehuffleTest extends BaseExecTest{

	
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
//		String CREATE_TABLE3 = "select * into TMP_POSITION from POSITION";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceSybase);
		jdbcTemplate.execute(CREATE_TABLE);
		jdbcTemplate.execute(CREATE_TABLE2);
//		jdbcTemplate.execute(CREATE_TABLE3);
		System.out.println("Tables created");
	}
	
	@After
	public void dropTable(){
		String DROP_TABLE = "drop table TMP_CS_SOURCE_SYSTEM";
		String DROP_TABLE2 = "drop table TMP_ADB_IMPORT_STATUS";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceSybase);
		jdbcTemplate.execute(DROP_TABLE);	
		jdbcTemplate.execute(DROP_TABLE2);	
		System.out.println("2 Table dropped. ");
//		System.out.println("!!!!!!!! drop table TMP_POSITION manually");
	}


	
	@Test
	public void testAnonymisationReshuffleString() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		AnonymisationMethodReshuffleSybase anonymisationMethod = new AnonymisationMethodReshuffleSybase();
		anonymisationMethod.setPassword("testpassword2");
		anonConfig.addAnonMethod(anonymisationMethod);
		
		addTable("SSY_SourceSystem", "varchar", "TMP_ADB_IMPORT_STATUS", anonymisationMethod,anonConfig);
		addTable("SSY_SourceSystem", "varchar", "TMP_CS_SOURCE_SYSTEM", anonymisationMethod,anonConfig);
//		addTable("SSY_SourceSystem", "varchar", "TMP_POSITION", anonymisationMethod,anonConfig);
		
		sybaseExec.setAnonConfig(anonConfig);
		
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_Target, SSY_SourceSystem from TMP_ADB_IMPORT_STATUS")
				);		
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select * from TMP_CS_SOURCE_SYSTEM")
				);		
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_Target, SSY_SourceSystem from TMP_ADB_IMPORT_STATUS")
				);		
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select * from TMP_CS_SOURCE_SYSTEM")
				);		
	}
	
	@Test
	public void testAnonymisationReshuffleDate() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		AnonymisationMethodReshuffleSybase anonymisationMethod = new AnonymisationMethodReshuffleSybase();
		anonymisationMethod.setPassword("testpassword2");
		anonConfig.addAnonMethod(anonymisationMethod);
		
		addTable("SSY_TopLink", "varchar", "TMP_CS_SOURCE_SYSTEM", anonymisationMethod,anonConfig);
		
		sybaseExec.setAnonConfig(anonConfig);
		
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select * from TMP_CS_SOURCE_SYSTEM")
				);		
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select * from TMP_CS_SOURCE_SYSTEM")
				);		
	}


	
	private void addTable(String colName, String colType, String tableName, AnonymisationMethod anonymisationMethod, AnonConfig anonConfig) {
		DatabaseTableInfo table = new DatabaseTableInfo();
		DatabaseColumnInfo databaseColumnInfo = new DatabaseColumnInfo(colName, colType, SybaseDbConnection.databaseSpecifics);
		table.addColumn(databaseColumnInfo);
		
		anonConfig.addTable(table);
		table.setName(tableName);
		AnonymisedColumnInfo anonymizedColumn = new AnonymisedColumnInfo(databaseColumnInfo);
		anonymisationMethod.addColumn(anonymizedColumn);
		table.addAnonymisedColumn(anonymizedColumn);
		
		

	}

}
