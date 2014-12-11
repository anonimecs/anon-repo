package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodReshuffleOracle;
import org.anon.vendor.OracleDbConnection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class OracleRehuffleTest extends BaseExecTest{

	
	@Autowired
	DataSource dataSourceOracle;
	
	@Override
	DataSource getDataSource() {
		return dataSourceOracle;
	}
	
	TestTableCreatorOracle testTableCreator = new TestTableCreatorOracle();
	
	public TestTableCreatorOracle getTestTableCreator() {
		return testTableCreator;
	}

	
	@Test
	public void testAnonymisationReshuffleNumber() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceOracle);
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		AnonymisationMethodReshuffleOracle anonymisationMethod = new AnonymisationMethodReshuffleOracle();
		anonymisationMethod.setPassword("testpassword2");
		anonConfig.addAnonMethod(anonymisationMethod);
		
		addTable("ID", "number", "TMP_TABLE_B", anonymisationMethod,anonConfig);
		addTable("ROLE_ID", "number", "TMP_TABLE_A", anonymisationMethod,anonConfig);
		
		oracleExec.setAnonConfig(anonConfig);
		
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select* from TMP_TABLE_B")
				);		
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select distinct ROLE_ID from TMP_TABLE_A")
				);		
		oracleExec.runAll();

		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select * from TMP_TABLE_B")
				);		
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select distinct ROLE_ID from TMP_TABLE_A")
				);		
	}
	
	@Test
	public void testAnonymisationReshuffleDate() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceOracle);
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		AnonymisationMethodReshuffleOracle anonymisationMethod = new AnonymisationMethodReshuffleOracle();
		anonymisationMethod.setPassword("testpassword2");
		anonConfig.addAnonMethod(anonymisationMethod);
		
		addTable("MODIFIEDAT", "DATE", "TMP_TABLE_A", anonymisationMethod,anonConfig);
		
		oracleExec.setAnonConfig(anonConfig);
		
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select * from TMP_TABLE_A")
				);		
		oracleExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceOracle).queryForList("select * from TMP_TABLE_A")
				);		
	}


	
	private void addTable(String colName, String colType, String tableName, AnonymisationMethod anonymisationMethod, AnonConfig anonConfig) {
		DatabaseTableInfo table = new DatabaseTableInfo();
		DatabaseColumnInfo databaseColumnInfo = new DatabaseColumnInfo(colName, colType, OracleDbConnection.databaseSpecifics);
		table.addColumn(databaseColumnInfo);
		
		anonConfig.addTable(table);
		table.setName(tableName);
		AnonymisedColumnInfo anonymizedColumn = new AnonymisedColumnInfo(databaseColumnInfo);
		anonymisationMethod.addColumn(anonymizedColumn);
		table.addAnonymisedColumn(anonymizedColumn);
		
		

	}

}
