package org.anon;

import java.io.IOException;
import java.util.Date;

import org.anon.data.MethodExecution.Status;
import org.anon.data.ReductionType;
import org.anon.persistence.dao.AuditDao;
import org.anon.persistence.data.ReductionMethodData;
import org.anon.persistence.data.ReductionMethodReferencingTableData;
import org.anon.persistence.data.audit.ExecutionData;
import org.anon.persistence.data.audit.ReductionExecutionData;
import org.anon.persistence.data.audit.RefTableReductionExecutionData;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuditDaoReductionTest extends DbEntitiesTest {

		static Long id;
	
		@Autowired
		AuditDao auditDao;

		static ExecutionData executionData;
		static ReductionMethodData reductionMethodData;
		static ReductionExecutionData reductionExecutionData;
		static ReductionMethodReferencingTableData reductionMethodReferencingTableData;
		
		@Test
		public void test00_dbSetup() throws IOException {
			recreateInMemDb();
		}

		
		@Test
		public void test0InsertReductionMethodData() {
			databaseConfig =  createDatabaseConfig();
			databaseConfigDao.addDatabaseConfig(databaseConfig);

			reductionMethodData = new ReductionMethodData();
			reductionMethodData.setDatabaseConfigId(databaseConfig.getId());
			reductionMethodData.setReductionType(ReductionType.DELETE_ALL);
			reductionMethodData.setSchemaName("TESTSCHEMA");
			reductionMethodData.setTableName("TESTTABLE");
			reductionMethodData.setWhereCondition("AAA");
			
			reductionMethodReferencingTableData = new ReductionMethodReferencingTableData();
			reductionMethodReferencingTableData.setReductionType(ReductionType.DELETE_ALL);
			reductionMethodReferencingTableData.setSchemaName("TESTSCHEMA");
			reductionMethodReferencingTableData.setTableName("TESTTABLE2");
			reductionMethodReferencingTableData.setWhereCondition("AAA");
			
			reductionMethodData.add(reductionMethodReferencingTableData);
			
			entitiesDao.save(reductionMethodData);
			methodId = reductionMethodData.getId();
			
			Assert.assertNotNull("id was assigned", methodId);
			
		}


		
		@Test
		public void test1InsertExecutionData() {
			executionData = new ExecutionData();
			executionData.setDescription("test description");
			
			executionData.setStartTime(new Date());
			executionData.setUserName("test user");
			executionData.setStatusEnum(Status.RUNNING);
			executionData.setDatabaseConfig(databaseConfig);
			
			
			auditDao.save(executionData);
			
			Assert.assertNotNull("id was generated", executionData.getId());
			
		}
		
		
		@Test
		public void test2InsertReductionExecution() {
			reductionExecutionData  = new ReductionExecutionData();

			reductionExecutionData.setReductionMethodData(reductionMethodData);
			reductionExecutionData.setExecutionData(executionData);
			reductionExecutionData.setResultText("AAAAA");
			reductionExecutionData.setRuntimeSec(55l);
			reductionExecutionData.setStatusEnum(Status.RUNNING);

			auditDao.save(reductionExecutionData);
			

			Assert.assertNotNull("id was generated", reductionExecutionData.getId());
			
		}
		
		@Test
		public void test3InsertRefTableExecution() {
			RefTableReductionExecutionData data = new RefTableReductionExecutionData();

			data.setReductionExecutionData(reductionExecutionData);
			data.setReductionMethodReferencingTableData(reductionMethodReferencingTableData);
			data.setResultText("AAAAA");
			data.setRuntimeSec(55l);
			data.setStatusEnum(Status.RUNNING);

			auditDao.save(data);
			

			Assert.assertNotNull("id was generated", reductionExecutionData.getId());
			
		}
		

		
}

