package org.anon.logic;

import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.Database;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.exec.AnonExec;
import org.anon.exec.TwoTestTablesCreator;
import org.anon.logic.map.TwoTablesAllDbTest;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Assert;
import org.junit.Test;

public class AnonymisationMethodEncryptTest extends TwoTablesAllDbTest{

	public AnonymisationMethodEncryptTest(String schema, Database database, TwoTestTablesCreator testTablesCreator ) {
		super(schema, database, testTablesCreator);
	}

	
	MethodFactory methodFactory = new MethodFactory();
	
	@Test
	public void testPreview() throws Exception {
		AnonymisationMethodEncrypt anonymisationMethodEncrypt = (AnonymisationMethodEncrypt)methodFactory.createMethod(AnonymizationType.ENCRYPT, database.getDatabaseSpecifics());
		anonymisationMethodEncrypt.setDataSource(getDataSource());
		anonymisationMethodEncrypt.setSchema(schema);
		Assert.assertEquals("89:;<", anonymisationMethodEncrypt.anonymiseString("12345"));
		assertNull(anonymisationMethodEncrypt.anonymiseString(null));
	}

	
	@Test
	public void testUniqueColumn() throws Exception {

		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();

		AnonymisationMethodEncrypt anonymisationMethodEncrypt = (AnonymisationMethodEncrypt)methodFactory.createMethod(AnonymizationType.ENCRYPT, database.getDatabaseSpecifics());
		AnonymisedColumnInfo column = getTestAnonimisedColumnInfo("COL1", "VARCHAR2", "TMP_TABLE_A3", anonymisationMethodEncrypt, database.getDatabaseSpecifics(), anonConfig);
		
		anonymisationMethodEncrypt.addColumn(column);
		
		runConfig(anonConfig, anonymisationMethodEncrypt);
		
		List<Map<String, Object>> res = loadFromDb("select distinct COL1 from TMP_TABLE_A3");
		Assert.assertEquals(3,  res.size());

	}

	@Test
	public void testNotUniqueColumn() throws Exception {

		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();

		AnonymisationMethodEncrypt anonymisationMethodEncrypt = (AnonymisationMethodEncrypt)methodFactory.createMethod(AnonymizationType.ENCRYPT, database.getDatabaseSpecifics());
		AnonymisedColumnInfo column = getTestAnonimisedColumnInfo("COL2", "VARCHAR2", "TMP_TABLE_A3", anonymisationMethodEncrypt, database.getDatabaseSpecifics(), anonConfig);
		
		anonymisationMethodEncrypt.addColumn(column);
		
		runConfig(anonConfig, anonymisationMethodEncrypt);
		
		List<Map<String, Object>> res = loadFromDb("select distinct COL2 from TMP_TABLE_A3");
		Assert.assertEquals(3,  res.size());

	}


	protected void runConfig(AnonConfig anonConfig, AnonymisationMethod anonymisationMethod) {
		anonConfig.addAnonMethod(anonymisationMethod);
		
		anonymisationMethod.setDataSource(getDataSource());
		
		AnonExec anonExec = createBaseExec();
		anonExec.setExecConfig(anonConfig);
		anonExec.runAll();
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
