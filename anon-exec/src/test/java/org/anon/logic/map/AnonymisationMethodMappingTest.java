package org.anon.logic.map;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.Database;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.exec.AnonExec;
import org.anon.exec.TwoTestTablesCreator;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodMapping;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.constraint.ConstraintBundleFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class AnonymisationMethodMappingTest extends TwoTablesAllDbTest{

	public AnonymisationMethodMappingTest(String schema, Database database, TwoTestTablesCreator testTablesCreator ) {
		super(schema, database, testTablesCreator);
	}

	
	@Test
	public void testMapStringsOnUniqueCol() {

		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();

		AnonymisationMethodMapping anonymisationMethod = new AnonymisationMethodMapping();
		anonymisationMethod.setMappingDefault(new MappingDefault("xxxx"));
		
		anonymisationMethod.addMappingRule(new LessThan("K", "aaa"));
		anonymisationMethod.addMappingRule(new LessThan("O", "lll"));
		AnonymisedColumnInfo anonymisedColumnInfo = getTestAnonimisedColumnInfo("COL1", "VARCHAR2", "TMP_TABLE_A3", anonymisationMethod, database.getDatabaseSpecifics(), anonConfig);
		
		runConfig(anonConfig, anonymisationMethod);
		
		Assert.assertEquals("aaa", anonymisationMethod.anonymise("J", anonymisedColumnInfo));
		Assert.assertEquals("lll", anonymisationMethod.anonymise("K", anonymisedColumnInfo));
		Assert.assertEquals("xxxx", anonymisationMethod.anonymise("X", anonymisedColumnInfo));

	}


	protected void runConfig(AnonConfig anonConfig, AnonymisationMethodMapping anonymisationMethod) {
		anonConfig.addAnonMethod(anonymisationMethod);
		
		anonymisationMethod.setDataSource(getDataSource());
		
		AnonExec anonExec = createBaseExec();
		anonExec.setExecConfig(anonConfig);
		ReflectionTestUtils.setField(anonExec, "constraintBundleFactory", new ConstraintBundleFactory());
		anonExec.runAll();
	}
	
	@Test
	public void testMapStringsOnNoConstraintCol() {

		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();

		AnonymisationMethodMapping anonymisationMethod = new AnonymisationMethodMapping();
		anonymisationMethod.setMappingDefault(new MappingDefault("xxxx"));
		
		anonymisationMethod.addMappingRule(new LessThan("K", "aaa"));
		anonymisationMethod.addMappingRule(new LessThan("O", "lll"));
		AnonymisedColumnInfo anonymisedColumnInfo = getTestAnonimisedColumnInfo("COL2", "VARCHAR2", "TMP_TABLE_A3", anonymisationMethod, database.getDatabaseSpecifics(), anonConfig);
		
		runConfig(anonConfig, anonymisationMethod);

		Assert.assertEquals("aaa", anonymisationMethod.anonymise("J", anonymisedColumnInfo));
		Assert.assertEquals("lll", anonymisationMethod.anonymise("K", anonymisedColumnInfo));
		Assert.assertEquals("xxxx", anonymisationMethod.anonymise("X", anonymisedColumnInfo));

	}
	
	@Test
	public void testMapIntOnNoConstraintCol() {

		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();

		AnonymisationMethodMapping anonymisationMethod = new AnonymisationMethodMapping();
		anonymisationMethod.setMappingDefault(new MappingDefault("1"));
		
		anonymisationMethod.addMappingRule(new LessThan("1", "0"));
		anonymisationMethod.addMappingRule(new LessThan("2", "-1"));
		AnonymisedColumnInfo anonymisedColumnInfo = getTestAnonimisedColumnInfo("COL3", "INT", "TMP_TABLE_A3", anonymisationMethod, database.getDatabaseSpecifics(), anonConfig);
		
		runConfig(anonConfig, anonymisationMethod);
		
		String anonimisedDbValue = loadFromDbObject("select COL3 from TMP_TABLE_A3 where COL1 = 'AAA'", String.class);
		Assert.assertEquals("anonimisedDbValue", -1, Integer.parseInt(anonimisedDbValue));

		Assert.assertEquals(0, (int)anonymisationMethod.anonymise(0, anonymisedColumnInfo));
		Assert.assertEquals(-1, (int)anonymisationMethod.anonymise(1, anonymisedColumnInfo));
		Assert.assertEquals(1, (int)anonymisationMethod.anonymise(10, anonymisedColumnInfo));

	}
	
	@Test
	public void testMapFloatOnNoConstraintCol() {

		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();

		AnonymisationMethodMapping anonymisationMethod = new AnonymisationMethodMapping();
		anonymisationMethod.setMappingDefault(new MappingDefault("1.0"));
		
		anonymisationMethod.addMappingRule(new LessThan("1.0", "0"));
		anonymisationMethod.addMappingRule(new LessThan("2", "0.9"));
		getTestAnonimisedColumnInfo("COL4", "FLOAT", "TMP_TABLE_A3", anonymisationMethod, database.getDatabaseSpecifics(), anonConfig);
		
		runConfig(anonConfig, anonymisationMethod);

		String anonimisedDbValue = loadFromDbObject("select COL4 from TMP_TABLE_A3 where COL1 = 'AAA'", String.class);
		Assert.assertEquals("anonimisedDbValue", 0.9, Double.parseDouble(anonimisedDbValue), 0.0);
		
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
