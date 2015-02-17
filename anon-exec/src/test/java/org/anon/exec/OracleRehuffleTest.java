package org.anon.exec;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodReshuffleOracle;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class OracleRehuffleTest extends OracleExecTestBase{

	
	@Test
	public void testAnonymisationReshuffleNumber() {
		OracleExec oracleExec = createExec();
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		AnonymisationMethodReshuffleOracle anonymisationMethod = new AnonymisationMethodReshuffleOracle();
		anonymisationMethod.setPassword("testpassword2");
		anonConfig.addAnonMethod(anonymisationMethod);
		
		addTable("ID", "number", "TMP_TABLE_A", anonymisationMethod,anonConfig);
		addTable("ROLE_ID", "number", "TMP_TABLE_B", anonymisationMethod,anonConfig);
		
		oracleExec.setExecConfig(anonConfig);
		
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select* from TMP_TABLE_A")
				);		
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select distinct ROLE_ID from TMP_TABLE_B")
				);		
		oracleExec.runAll();

		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select * from TMP_TABLE_A")
				);		
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select distinct ROLE_ID from TMP_TABLE_B")
				);		
	}


	
	@Test
	public void testAnonymisationReshuffleDate() {
		OracleExec oracleExec = createExec();
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		AnonymisationMethodReshuffleOracle anonymisationMethod = new AnonymisationMethodReshuffleOracle();
		anonymisationMethod.setPassword("testpassword2");
		anonConfig.addAnonMethod(anonymisationMethod);
		
		addTable("MODIFIEDAT", "DATE", "TMP_TABLE_B", anonymisationMethod,anonConfig);
		
		oracleExec.setExecConfig(anonConfig);
		
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select * from TMP_TABLE_B")
				);		
		oracleExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select * from TMP_TABLE_B")
				);		
	}


	
	private void addTable(String colName, String colType, String tableName, AnonymisationMethod anonymisationMethod, AnonConfig anonConfig) {
		DatabaseTableInfo table = new DatabaseTableInfo();
		DatabaseColumnInfo databaseColumnInfo = new DatabaseColumnInfo(colName, colType, DatabaseSpecifics.OracleSpecific);
		table.addColumn(databaseColumnInfo);
		
		anonConfig.addTable(table);
		table.setName(tableName);
		AnonymisedColumnInfo anonymizedColumn = new AnonymisedColumnInfo(databaseColumnInfo);
		anonymisationMethod.addColumn(anonymizedColumn);
		table.addAnonymisedColumn(anonymizedColumn);
		
		

	}

}
