package org.anon.logic;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.Database;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.exec.AnonExec;
import org.anon.exec.TwoTestTablesCreator;
import org.anon.logic.map.TwoTablesAllDbTest;
import org.anon.service.where.WhereConditionBuilder;
import org.anon.service.where.WhereConditionBuilder.Applicability;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class AnonymisationMethodEncryptRowFilterTest extends TwoTablesAllDbTest{

	public AnonymisationMethodEncryptRowFilterTest(String schema, Database database, TwoTestTablesCreator testTablesCreator ) {
		super(schema, database, testTablesCreator);
	}

	WhereConditionBuilder whereConditionBuilder = new WhereConditionBuilder();
	
	
	MethodFactory methodFactory = new MethodFactory();
	
	@Before
	public void deleteProcs() throws Exception {
		try{updateDB("drop procedure an_meth_enc_func");}catch(Exception i){}
		try{updateDB("drop function an_meth_enc_func");}catch(Exception i){}
	}

	
	@Test 
	public void testSimpleWhere() throws Exception {

		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();

		AnonymisationMethodEncrypt anonymisationMethodEncrypt = (AnonymisationMethodEncrypt)methodFactory.createMethod(AnonymizationType.ENCRYPT, database.getDatabaseSpecifics());
		AnonymisedColumnInfo column = getTestAnonimisedColumnInfo("COL1", "VARCHAR2", "TMP_TABLE_A3", anonymisationMethodEncrypt, database.getDatabaseSpecifics(), anonConfig);
		String whereCondition = whereConditionBuilder.build(Applicability.APPLY, "COL2 = 'AAA'");
		column.setWhereCondition(whereCondition);
		
		anonymisationMethodEncrypt.addColumn(column);
		
		runConfig(anonConfig, anonymisationMethodEncrypt);
		
		Assert.assertEquals("OOO", loadFromDbObject("select COL1 from TMP_TABLE_A3 where COL2 = 'AAA'", String.class));
		Assert.assertEquals("BBB", loadFromDbObject("select COL1 from TMP_TABLE_A3 where COL2 = 'BBB'", String.class));
		Assert.assertEquals("CCC", loadFromDbObject("select COL1 from TMP_TABLE_A3 where COL2 = 'CCC'", String.class));
	}

	@Test
	public void testSimpleWhereWithReferencingTable() throws Exception {

		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();

		AnonymisationMethodEncrypt anonymisationMethodEncrypt = (AnonymisationMethodEncrypt)methodFactory.createMethod(AnonymizationType.ENCRYPT, database.getDatabaseSpecifics());
		AnonymisedColumnInfo column = getTestAnonimisedColumnInfo("COL1", "VARCHAR2", "TMP_TABLE_A3", anonymisationMethodEncrypt, database.getDatabaseSpecifics(), anonConfig);
		String whereCondition = whereConditionBuilder.build(Applicability.APPLY, "COL2 = 'AAA'");
		column.setWhereCondition(whereCondition);
		anonymisationMethodEncrypt.addColumn(column);
		
		AnonymisedColumnInfo referencingColumn = getTestAnonimisedColumnInfo("COL1_REF", "VARCHAR2", "TMP_TABLE_B3", anonymisationMethodEncrypt, database.getDatabaseSpecifics(), anonConfig);
		String whereCondition2 = whereConditionBuilder.buildForRelatedTable(Applicability.APPLY, "COL2='AAA' ", referencingColumn, column);
		referencingColumn.setWhereCondition(whereCondition2);
		anonymisationMethodEncrypt.addColumn(referencingColumn);
		
		runConfig(anonConfig, anonymisationMethodEncrypt);
		
		Assert.assertEquals("OOO", loadFromDbObject("select COL1 from TMP_TABLE_A3 where COL2 = 'AAA'", String.class));
		Assert.assertEquals("BBB", loadFromDbObject("select COL1 from TMP_TABLE_A3 where COL2 = 'BBB'", String.class));

		Assert.assertEquals("HHH", loadFromDbObject("select COL1_REF from TMP_TABLE_B3 where COL2_REF = 'AAA'", String.class));
		Assert.assertEquals("BBB", loadFromDbObject("select COL1_REF from TMP_TABLE_B3 where COL2_REF = 'BBB'", String.class));

	}

	@Test
	public void testDualWhere() throws Exception {

		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();

		AnonymisationMethodEncrypt anonymisationMethodEncrypt = (AnonymisationMethodEncrypt)methodFactory.createMethod(AnonymizationType.ENCRYPT, database.getDatabaseSpecifics());
		AnonymisedColumnInfo column = getTestAnonimisedColumnInfo("COL1", "VARCHAR2", "TMP_TABLE_A3", anonymisationMethodEncrypt, database.getDatabaseSpecifics(), anonConfig);
		column.setWhereCondition("COL2 = 'AAA' or COL2='BBB'");
		
		anonymisationMethodEncrypt.addColumn(column);
		
		runConfig(anonConfig, anonymisationMethodEncrypt);
		
		Assert.assertEquals("OOO", loadFromDbObject("select COL1 from TMP_TABLE_A3 where COL2 = 'AAA'", String.class));
		Assert.assertEquals("PPP", loadFromDbObject("select COL1 from TMP_TABLE_A3 where COL2 = 'BBB'", String.class));
		Assert.assertEquals("CCC", loadFromDbObject("select COL1 from TMP_TABLE_A3 where COL2 = 'CCC'", String.class));

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
