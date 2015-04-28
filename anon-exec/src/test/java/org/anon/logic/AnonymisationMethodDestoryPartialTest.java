package org.anon.logic;

import java.util.List;
import java.util.Map;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.Database;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.exec.BaseExec;
import org.anon.exec.TwoTestTablesCreator;
import org.anon.logic.map.TwoTablesAllDbTest;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Assert;
import org.junit.Test;

public class AnonymisationMethodDestoryPartialTest extends TwoTablesAllDbTest{

	public AnonymisationMethodDestoryPartialTest(String schema, Database database, TwoTestTablesCreator testTablesCreator ) {
		super(schema, database, testTablesCreator);
	}

	
	MethodFactory methodFactory = new MethodFactory();
	
	@Test
	public void testSimpleWhere() throws Exception {

		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();

		AnonymisationMethodDestory anonymisationMethodDestory = (AnonymisationMethodDestory)methodFactory.createMethod(AnonymizationType.DESTROY, database.getDatabaseSpecifics());
		AnonymisedColumnInfo column = getTestAnonimisedColumnInfo("COL1", "VARCHAR2", "TMP_TABLE_A3", anonymisationMethodDestory, database.getDatabaseSpecifics(), anonConfig);
		column.setWhereCondition("COL1 = 'AAA'");
		
		anonymisationMethodDestory.addColumn(column);
		
		runConfig(anonConfig, anonymisationMethodDestory);
		
		List<Map<String, Object>> res = loadFromDb("select distinct COL1 from TMP_TABLE_A3");
		Assert.assertEquals(3,  res.size());
	}

	@Test
	public void testDualWhere() throws Exception {

		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();

		AnonymisationMethodDestory anonymisationMethodDestory = (AnonymisationMethodDestory)methodFactory.createMethod(AnonymizationType.DESTROY, database.getDatabaseSpecifics());
		AnonymisedColumnInfo column = getTestAnonimisedColumnInfo("COL2", "VARCHAR2", "TMP_TABLE_A3", anonymisationMethodDestory, database.getDatabaseSpecifics(), anonConfig);
		column.setWhereCondition("COL2 = 'AAA' or COL2='BBB'");
		
		anonymisationMethodDestory.addColumn(column);
		
		runConfig(anonConfig, anonymisationMethodDestory);
		
		List<Map<String, Object>> res = loadFromDb("select distinct COL2 from TMP_TABLE_A3");
		Assert.assertEquals(2,  res.size());

	}


	protected void runConfig(AnonConfig anonConfig, AnonymisationMethod anonymisationMethod) {
		anonConfig.addAnonMethod(anonymisationMethod);
		
		anonymisationMethod.setDataSource(getDataSource());
		
		BaseExec baseExec = createBaseExec();
		baseExec.setExecConfig(anonConfig);
		baseExec.runAll();
	}
	

	protected AnonymisedColumnInfo getTestAnonimisedColumnInfo(String colName, String colType,String tableName, AnonymisationMethod anonymisationMethod, DatabaseSpecifics databaseSpecifics, AnonConfig anonConfig) {
		DatabaseTableInfo table = new DatabaseTableInfo();
		DatabaseColumnInfo databaseColumnInfo = new DatabaseColumnInfo(colName, colType, true, databaseSpecifics);
		table.addColumn(databaseColumnInfo);
		
		anonConfig.addTable(table);
		table.setName(tableName);
		AnonymisedColumnInfo anonymizedColumn = new AnonymisedColumnInfo(databaseColumnInfo);
		anonymisationMethod.addColumn(anonymizedColumn);
		table.addAnonymisedColumn(anonymizedColumn);
		table.setSchema(schema);
		
		return anonymizedColumn;
	}


}
