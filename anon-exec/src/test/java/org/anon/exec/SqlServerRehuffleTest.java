package org.anon.exec;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodReshuffleSqlServer;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlServerRehuffleTest extends SqlServerExecTestBase{

	

	
	@Test
	public void testAnonymisationReshuffleString() {
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		AnonymisationMethodReshuffleSqlServer anonymisationMethod = new AnonymisationMethodReshuffleSqlServer();
		anonymisationMethod.setPassword("testpassword2");
		anonConfig.addAnonMethod(anonymisationMethod);
		
		addTable("SSY_SourceSystem", "varchar", "TMP_TABLE_B", anonymisationMethod,anonConfig);
		addTable("SSY_SourceSystem", "varchar", "TMP_TABLE_A", anonymisationMethod,anonConfig);
//		addTable("SSY_SourceSystem", "varchar", "TMP_POSITION", anonymisationMethod,anonConfig);
		

		SqlServerExec sqlServerExec = createSqlServerExec();
		sqlServerExec.setExecConfig(anonConfig);
		
		System.out.println(
				new JdbcTemplate(dataSourceSqlServer).queryForList("select AIS_Target, SSY_SourceSystem from TMP_TABLE_B")
				);		
		System.out.println(
				new JdbcTemplate(dataSourceSqlServer).queryForList("select * from TMP_TABLE_A")
				);		
		sqlServerExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSqlServer).queryForList("select AIS_Target, SSY_SourceSystem from TMP_TABLE_B")
				);		
		System.out.println(
				new JdbcTemplate(dataSourceSqlServer).queryForList("select * from TMP_TABLE_A")
				);		
	}
	
	@Test
	public void testAnonymisationReshuffleDate() {
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		AnonymisationMethodReshuffleSqlServer anonymisationMethod = new AnonymisationMethodReshuffleSqlServer();
		anonymisationMethod.setPassword("testpassword2");
		anonConfig.addAnonMethod(anonymisationMethod);
		
		addTable("SSY_TopLink", "varchar", "TMP_TABLE_A", anonymisationMethod,anonConfig);
		
		SqlServerExec sqlServerExec = createSqlServerExec();
		sqlServerExec.setExecConfig(anonConfig);
		
		System.out.println(
				new JdbcTemplate(dataSourceSqlServer).queryForList("select * from TMP_TABLE_A")
				);		
		sqlServerExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSqlServer).queryForList("select * from TMP_TABLE_A")
				);		
	}


	
	private void addTable(String colName, String colType, String tableName, AnonymisationMethod anonymisationMethod, AnonConfig anonConfig) {
		DatabaseTableInfo table = new DatabaseTableInfo();
		DatabaseColumnInfo databaseColumnInfo = new DatabaseColumnInfo(colName, colType,true,  DatabaseSpecifics.SqlServerSpecific);
		table.addColumn(databaseColumnInfo);
		
		anonConfig.addTable(table);
		table.setName(tableName);
		AnonymisedColumnInfo anonymizedColumn = new AnonymisedColumnInfo(databaseColumnInfo);
		anonymisationMethod.addColumn(anonymizedColumn);
		table.addAnonymisedColumn(anonymizedColumn);
		
		

	}

}
