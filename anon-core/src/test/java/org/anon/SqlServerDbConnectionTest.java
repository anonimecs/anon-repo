package org.anon;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.exec.BaseDbTest;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.SqlServerDbConnection;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class SqlServerDbConnectionTest extends BaseDbTest{

	@Autowired
    protected DataSource dataSourceSqlServer;
	
	@Value("${sqlserver.test.schema}")
	protected String schema;
	
	@Value("${sqlserver.available}")
	protected Boolean sqlserverAvailable;
	
	@Before
	public void assumeDbAvailable(){
		Assume.assumeTrue("database exists check", sqlserverAvailable);
	}
	
	
    @Test
    public void testSchemas(){

		SqlServerDbConnection sqlServerDbConnection = createConn();
		System.out.println(sqlServerDbConnection.getSchemas());
		System.out.println(sqlServerDbConnection.getDefaultSchema());
   	
    }

	private SqlServerDbConnection createConn() {
		SqlServerDbConnection sqlServerDbConnection = new SqlServerDbConnection(schema);
		sqlServerDbConnection.setDataSource(dataSourceSqlServer);
		return sqlServerDbConnection;
	}
    
	@Test
	public void testTables() {
		SqlServerDbConnection sqlServerDbConnection = createConn();
		
		List<DatabaseTableInfo> res = sqlServerDbConnection.getTableList(schema);
		
		Assert.assertTrue(res.size() >= 1);
		Assert.assertTrue(res.get(0).getRowCount() > 0);
		
	}


    
	@Test
	public void testGetColumns() {
		SqlServerDbConnection sqlServerDbConnection = createConn();

		List<DatabaseTableInfo> res = sqlServerDbConnection.getTableList(schema);

		String tableName = res.get(0).getName();
		
		DatabaseTableInfo databaseTableInfo = new DatabaseTableInfo();
		databaseTableInfo.setName(tableName);
		databaseTableInfo.setSchema(schema);
		System.out.println(sqlServerDbConnection.getColumns(databaseTableInfo));
	}

	@Test
	public void testSupportedDatatypes() {
		SqlServerDbConnection sqlServerDbConnection = createConn();
		
		List<DatabaseTableInfo> res = sqlServerDbConnection.getTableList(schema);
		
		for (DatabaseTableInfo databaseTableInfo : res) {
			for(DatabaseColumnInfo databaseColumnInfo:databaseTableInfo.getColumns()){
				if(!DatabaseSpecifics.SqlServerSpecific.isSupportedDataType(databaseColumnInfo)){
					System.out.println("Unsupported datatype on column " + databaseColumnInfo + ":"+   databaseColumnInfo.getType());
				}

//				Assert.assertTrue("Unsupported datatype on column " + databaseColumnInfo + ":"+  databaseColumnInfo.getType(), DatabaseSpecifics.SqlServerSpecific.isSupportedDataType(databaseColumnInfo));	
			}
			
		}
		
	}
	
	@Test
	public void testNorthwindSchemaRelatedTables(){
		SqlServerDbConnection dbConnection = new SqlServerDbConnection("Northwind");
		dbConnection.setDataSource(dataSourceSqlServer);
		if(dbConnection.getSchemas().contains("Northwind")){
			DatabaseTableInfo editedTable = new DatabaseTableInfo();
			editedTable.setName("EmployeeTerritories");
			editedTable.setSchema("Northwind");
			DatabaseColumnInfo editedColumn = new DatabaseColumnInfo();
			editedColumn.setName("EmployeeID");
			List<RelatedTableColumnInfo> res  = dbConnection.findRelatedTables(editedTable, editedColumn);
			Assert.assertEquals("2 records expected", 2, res.size());
		}
		else {
			Assert.fail("Northwind schema not available in the test database");
		}
	}


}
