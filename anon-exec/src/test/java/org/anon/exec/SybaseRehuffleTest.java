package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodReshuffleSybase;
import org.anon.vendor.SybaseDbConnection;
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

	TestTableCreatorSybase testTableCreator = new TestTableCreatorSybase();

	public TestTableCreatorSybase getTestTableCreator() {
		return testTableCreator;
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
		
		addTable("SSY_SourceSystem", "varchar", "TMP_TABLE_B", anonymisationMethod,anonConfig);
		addTable("SSY_SourceSystem", "varchar", "TMP_TABLE_A", anonymisationMethod,anonConfig);
//		addTable("SSY_SourceSystem", "varchar", "TMP_POSITION", anonymisationMethod,anonConfig);
		
		sybaseExec.setAnonConfig(anonConfig);
		
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_Target, SSY_SourceSystem from TMP_TABLE_B")
				);		
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select * from TMP_TABLE_A")
				);		
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select AIS_Target, SSY_SourceSystem from TMP_TABLE_B")
				);		
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select * from TMP_TABLE_A")
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
		
		addTable("SSY_TopLink", "varchar", "TMP_TABLE_A", anonymisationMethod,anonConfig);
		
		sybaseExec.setAnonConfig(anonConfig);
		
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select * from TMP_TABLE_A")
				);		
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select * from TMP_TABLE_A")
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
