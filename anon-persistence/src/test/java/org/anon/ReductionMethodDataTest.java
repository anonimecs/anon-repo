package org.anon;

import java.io.IOException;
import java.util.List;

import org.anon.data.ReductionType;
import org.anon.persistence.data.ReductionMethodData;
import org.anon.persistence.data.ReductionMethodReferencingTableData;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReductionMethodDataTest extends DbEntitiesTest {
		static ReductionMethodData reductionMethodData;
	
	
		@Test
		public void test0_0_dbSetup() throws IOException {
			recreateInMemDb();
		}

		
		@Test
		public void test1InsertReductionMethodData() {
			databaseConfig =  createDatabaseConfig();
			databaseConfigDao.addDatabaseConfig(databaseConfig);

			reductionMethodData = new ReductionMethodData();
			reductionMethodData.setDatabaseConfigId(databaseConfig.getId());
			reductionMethodData.setReductionType(ReductionType.DELETE_ALL);
			reductionMethodData.setSchemaName("TESTSCHEMA");
			reductionMethodData.setTableName("TESTTABLE");
			reductionMethodData.setWhereCondition("AAA");
			
			ReductionMethodReferencingTableData data = new ReductionMethodReferencingTableData();
			data.setReductionType(ReductionType.DELETE_ALL);
			data.setSchemaName("TESTSCHEMA");
			data.setTableName("TESTTABLE2");
			data.setWhereCondition("AAA");
			
			reductionMethodData.add(data);
			
			entitiesDao.save(reductionMethodData);
			methodId = reductionMethodData.getId();
			
			Assert.assertNotNull("id was assigned", methodId);
			
		}

		@Test
		public void test2LoadReductionMethodData() {
			List<ReductionMethodData> list = entitiesDao.loadAllReductionMethods(databaseConfig);
			Assert.assertEquals(1, list.size());

			ReductionMethodData data = list.get(0);
			Assert.assertEquals("TESTTABLE", data.getTableName());
			Assert.assertEquals("TESTSCHEMA", data.getSchemaName());
			
			Assert.assertEquals("AAA", data.getReferencingTableDatas().get(0).getWhereCondition());
			
		}
		
		@Test
		public void test3RemoveReductionMethodData(){
			entitiesDao.removeReductionMethodData(reductionMethodData);
			Assert.assertEquals(0, loadFromDb("select 1 from ReductionMethodData where DATABASECONFIG_ID=" + databaseConfig.getId()).size());
		}
		
}

