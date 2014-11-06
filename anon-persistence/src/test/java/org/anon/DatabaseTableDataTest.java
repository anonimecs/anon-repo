package org.anon;

import java.util.List;

import org.anon.persistence.data.DatabaseColumnData;
import org.anon.persistence.data.DatabaseTableData;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseTableDataTest extends DbEntitiesTest {

		
		//@Test test suspended as DatabaseColumnData and DatabaseTableData not used
		public void testInsertTableAndCol() {
			DatabaseTableData databaseTable = new DatabaseTableData();
			databaseTable.setDatabaseConfigId(-1l);
			databaseTable.setName("TESTTABLENAME");
			databaseTable.setSchema("TESTSCHEMANAME");
			
			DatabaseColumnData databaseColumn = new DatabaseColumnData("TESTCOLNAME", "VARCHAR");
			databaseTable.addColumn(databaseColumn);
			
			entitiesDao.insert(databaseTable);
		}
		
		//@Test test suspended as DatabaseColumnData and DatabaseTableData not used
		public void testLoadAllTables() {
			
			List<DatabaseTableData> databaseTableList = entitiesDao.loadAllTables();
			System.out.println(databaseTableList);
			DatabaseTableData databaseTable = databaseTableList.get(0);
			System.out.println(databaseTable.getColumns());
			DatabaseColumnData databaseColumn = databaseTable.getColumns().get(0);
			System.out.println(databaseColumn.getType());
			
			cleanup();
		}

		
		public void cleanup(){
			new JdbcTemplate(dataSource).execute("delete from  DatabaseColumnData where COLUMNNAME = 'TESTCOLNAME'");
			new JdbcTemplate(dataSource).execute("delete from DatabaseTableData where TABLENAME = 'TESTTABLENAME'");
			System.out.println("Cleaned up");
		}
}
