package org.anon;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.anon.data.AnonymizationType;
import org.anon.logic.AnonymisationMethod;
import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.DatabaseConfig;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AnonMethodDataTest extends DbEntitiesTest {

		static Long id;
		
		@Test
		public void test1InsertAnonymisationMethodData() {
			AnonymisationMethodData anonymisationMethodData = new AnonymisationMethodData();
			anonymisationMethodData.setDatabaseConfigId(-1l);
			anonymisationMethodData.setAnonymizationType(AnonymizationType.DESTROY);
			anonymisationMethodData.setAnonMethodClass(AnonymisationMethod.class.getName());
			AnonymisedColumnData column = new AnonymisedColumnData();
			column.setColumnName("TESTCOLNAME");
			column.setTableName("TestTableName");
			column.setSchemaName("TestSchema");
			column.setColumnType("VARCHAR");
			
			anonymisationMethodData.addColumn(column);
			entitiesDao.save(anonymisationMethodData);
			id = anonymisationMethodData.getId();
			
		}
		
		@Test
		public void test2LoadAnonymisationMethodData() {
			DatabaseConfig databaseConfig = new DatabaseConfig(); 
			databaseConfig.setId(-1l);
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
			assertEquals(1, entitiesDao.removeAnonymisationMethodData(id));
			
		}
		
	//	@Test
		public void test9_cleanup() {
			new JdbcTemplate(dataSource).execute("delete from  AnonymisedColumnData where COLUMNNAME = 'TESTCOLNAME'");
			new JdbcTemplate(dataSource).execute("delete from AnonymisationMethodData where ANONMETHODCLASS='org.anon.logic.AnonymisationMethod'");
			System.out.println("Cleaned up");
		}
}

