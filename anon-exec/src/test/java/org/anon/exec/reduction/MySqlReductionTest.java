package org.anon.exec.reduction;

import java.util.Arrays;

import org.anon.data.ReductionType;
import org.anon.persistence.data.ReductionMethodData;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Test;

public class MySqlReductionTest extends MySqlReductionTestBase{

	
	
	@Test
	public void testDeleteall() {
		ReductionMethodData reductionMethodData = new ReductionMethodData();
		reductionMethodData.setDatabaseConfigId(0l);
		reductionMethodData.setReductionType(ReductionType.DELETE_ALL);
		reductionMethodData.setSchemaName("anon");
		reductionMethodData.setTableName("slavetable");

		
		ReductionExec reductionExec = execFactory.createReductionExec(DatabaseSpecifics.MySqlSpecific, "test");
		reductionExec.setReductionMethods(Arrays.asList(reductionMethodData));
		reductionExec.setDataSource(getDataSource());
		reductionExec.runAll();
		
	}

}
