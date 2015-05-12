package org.anon.exec.reduction;

import java.util.Arrays;

import org.anon.data.Database;
import org.anon.data.ReductionType;
import org.anon.exec.ExecFactory;
import org.anon.exec.TwoTestTablesCreator;
import org.anon.logic.map.TwoTablesAllDbTest;
import org.anon.persistence.data.ReductionMethodData;
import org.anon.persistence.data.ReductionMethodReferencingTableData;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ReductionSuperTest extends TwoTablesAllDbTest{

	@Autowired ExecFactory execFactory;
	
	public ReductionSuperTest(String schema, Database database, TwoTestTablesCreator testTablesCreator ) {
		super(schema, database, testTablesCreator);
	}

	@Test
	public void testDereference1() {
		ReductionMethodData reductionMethodData = new ReductionMethodData();
		reductionMethodData.setDatabaseConfigId(0l);
		reductionMethodData.setReductionType(ReductionType.DELETE_ALL);
		reductionMethodData.setSchemaName(schema);
		reductionMethodData.setTableName("TMP_TABLE_A3");

		ReductionMethodReferencingTableData data = new ReductionMethodReferencingTableData();
		data.setReductionType(ReductionType.DEREFERENCE);
		data.setSchemaName(schema);
		data.setTableName("TMP_TABLE_B3");
		reductionMethodData.add(data);

		
		runReduction(reductionMethodData);

	}

	@Test
	public void testDereference2() {
		ReductionMethodData reductionMethodData = new ReductionMethodData();
		reductionMethodData.setDatabaseConfigId(0l);
		reductionMethodData.setReductionType(ReductionType.DELETE_WHERE);
		reductionMethodData.setSchemaName(schema);
		reductionMethodData.setTableName("TMP_TABLE_A3");
		reductionMethodData.setWhereCondition("1=1");

		ReductionMethodReferencingTableData data = new ReductionMethodReferencingTableData();
		data.setReductionType(ReductionType.DEREFERENCE);
		data.setSchemaName(schema);
		data.setTableName("TMP_TABLE_B3");
		reductionMethodData.add(data);

		
		runReduction(reductionMethodData);

	}

	@Test
	public void testDeleteAll1() {
		ReductionMethodData reductionMethodData = new ReductionMethodData();
		reductionMethodData.setDatabaseConfigId(0l);
		reductionMethodData.setReductionType(ReductionType.DELETE_ALL);
		reductionMethodData.setSchemaName(schema);
		reductionMethodData.setTableName("TMP_TABLE_A3");

		ReductionMethodReferencingTableData data = new ReductionMethodReferencingTableData();
		data.setReductionType(ReductionType.DEREFERENCE);
		data.setSchemaName(schema);
		data.setTableName("TMP_TABLE_B3");
		reductionMethodData.add(data);

		
		runReduction(reductionMethodData);

	}

	@Test
	public void testDeleteAll2() {
		ReductionMethodData reductionMethodData = new ReductionMethodData();
		reductionMethodData.setDatabaseConfigId(0l);
		reductionMethodData.setReductionType(ReductionType.DELETE_ALL);
		reductionMethodData.setSchemaName(schema);
		reductionMethodData.setTableName("TMP_TABLE_A3");

		ReductionMethodReferencingTableData data = new ReductionMethodReferencingTableData();
		data.setReductionType(ReductionType.DELETE_ALL);
		data.setSchemaName(schema);
		data.setTableName("TMP_TABLE_B3");
		reductionMethodData.add(data);

		
		runReduction(reductionMethodData);

	}

	@Test
	public void testTruncate() {
		ReductionMethodData reductionMethodData = new ReductionMethodData();
		reductionMethodData.setDatabaseConfigId(0l);
		reductionMethodData.setReductionType(ReductionType.TRUNCATE);
		reductionMethodData.setSchemaName(schema);
		reductionMethodData.setTableName("TMP_TABLE_A3");

		ReductionMethodReferencingTableData data = new ReductionMethodReferencingTableData();
		data.setReductionType(ReductionType.TRUNCATE);
		data.setSchemaName(schema);
		data.setTableName("TMP_TABLE_B3");
		reductionMethodData.add(data);

		
		runReduction(reductionMethodData);

	}



	protected void runReduction(ReductionMethodData reductionMethodData) {
		ReductionExec reductionExec = execFactory.createReductionExec(database.getDatabaseSpecifics(), "testuser");
		reductionExec.setReductionMethods(Arrays.asList(reductionMethodData));
		reductionExec.setDataSource(getDataSource());
		reductionExec.runAll();
	}



}
