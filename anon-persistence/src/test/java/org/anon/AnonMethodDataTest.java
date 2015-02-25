package org.anon;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AnonMethodDataTest extends DbEntitiesTest {

		@Test
		public void test0_0_dbSetup() throws IOException {
			recreateInMemDb();
		}

		
		@Test
		public void test1InsertAnonymisationMethodData() {
			do_test1InsertAnonymisationMethodData();
			
		}

		@Test
		public void test2LoadAnonymisationMethodData() {
			List<AnonymisationMethodData> list = entitiesDao.loadAllAnonMethods(databaseConfig);
			System.out.println(list);
			AnonymisationMethodData data = list.get(0);
			System.out.println(data.getAnonymizationType());
			System.out.println(data.getApplyedToColumns());
			AnonymisedColumnData col = data.getApplyedToColumns().get(0);
			System.out.println(col.getColumnName());
			
		}
		
		@Test
		public void test3RemoveAnonymizedColumnData(){
			assertEquals(1, entitiesDao.removeAnonymizedColumnData("TestTableName", "TESTCOLNAME", "TestSchema"));
			
		}
		
		@Test
		public void test4RemoveAnonymizedMethodData(){
			assertEquals(1, entitiesDao.removeAnonymisationMethodData(methodId));
			
		}
		
	//	@Test
		public void test9_cleanup() {
			new JdbcTemplate(dataSource).execute("delete from  AnonymisedColumnData where COLUMNNAME = 'TESTCOLNAME'");
			new JdbcTemplate(dataSource).execute("delete from AnonymisationMethodData where ANONMETHODCLASS='org.anon.logic.AnonymisationMethod'");
			System.out.println("Cleaned up");
		}
}

