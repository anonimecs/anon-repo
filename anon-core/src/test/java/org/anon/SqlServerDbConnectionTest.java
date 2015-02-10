package org.anon;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.exec.BaseDbTest;
import org.anon.vendor.SqlServerDbConnection;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class SqlServerDbConnectionTest extends BaseDbTest{

	@Autowired
    protected DataSource dataSourceSqlServer;
	
	@Value("${sqlserver.test.schema}")
	protected String schema;
	
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
				if(!sqlServerDbConnection.databaseSpecifics.isSupportedDataType(databaseColumnInfo)){
					System.out.println("Unsupported datatype on column " + databaseColumnInfo + ":"+   databaseColumnInfo.getType());
				}

//				Assert.assertTrue("Unsupported datatype on column " + databaseColumnInfo + ":"+  databaseColumnInfo.getType(), sqlServerDbConnection.databaseSpecifics.isSupportedDataType(databaseColumnInfo));	
			}
			
		}
		
	}

}