package org.anon;

import java.io.IOException;
import java.util.Date;

import org.anon.data.MethodExecution.Status;
import org.anon.persistence.dao.AuditDao;
import org.anon.persistence.data.audit.ExecutionColumnData;
import org.anon.persistence.data.audit.ExecutionData;
import org.anon.persistence.data.audit.ExecutionMessageData;
import org.anon.persistence.data.audit.ExecutionMethodData;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuditDaoTest extends DbEntitiesTest {

		static Long id;
	
		@Autowired
		AuditDao auditDao;

		static ExecutionData executionData;
		static ExecutionMethodData executionMethodData;
		static ExecutionColumnData executionColumnData;
		
		@Test
		public void test00_dbSetup() throws IOException {
			recreateInMemDb();
		}

		
		@Test
		public void test0InsertAnonymisationMethodData() {
			do_test1InsertAnonymisationMethodData();
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
		public void test2InsertExecutionMethod() {
			executionMethodData  = new ExecutionMethodData();
			executionMethodData.setAnonymisationMethodData(anonymisationMethodData);
			executionMethodData.setExecutionData(executionData);
			executionMethodData.setResultText("AAAAA");
			executionMethodData.setRuntimeSec(55l);
			executionMethodData.setStatusEnum(Status.RUNNING);
			
			auditDao.save(executionMethodData);

			Assert.assertNotNull("id was generated", executionMethodData.getId());
			
		}
		

		@Test
		public void test3InsertExecutionColumnData() {
			executionColumnData = new ExecutionColumnData();
			
			executionColumnData.setAnonymisedColumnData(anonymisedColumnData);
			executionColumnData.setExecutionMethodData(executionMethodData);
			executionColumnData.setResultText("BBBB");
			executionColumnData.setRuntimeSec(66l);
			
			executionColumnData.addMessage(new ExecutionMessageData("message 1"));
			executionColumnData.addMessage(new ExecutionMessageData("message 2"));
			executionColumnData.setStatusEnum(Status.RUNNING);
			
			auditDao.save(executionColumnData);

			Assert.assertNotNull("id was generated", executionMethodData.getId());		
			for (ExecutionMessageData executionMessageData: executionColumnData.getMessages()) {
				Assert.assertNotNull("id was generated", executionMessageData.getId());
			}
			
		}
		
		@Test
		public void test4UpdateExecutionData() {
			executionData.setEndTime(new Date());
			executionData.setStatusEnum(Status.ANONYMISED);
		}
		
		@Test
		public void test5loadExecutionData() {
			Assert.assertEquals("one record was saved", 1,  auditDao.loadExecutionDatas().size());
		}
		
		@Test
		public void test6loadExecutionMethodData() {
			Assert.assertEquals("one record was saved", 1,  auditDao.loadExecutionMethodDatas(executionData).size());
		}
		
		@Test
		public void test7loadExecutionColumnData() {
			Assert.assertEquals("one record was saved", 1,  auditDao.loadExecutionColumnDatas(executionMethodData).size());
		}
		
}

