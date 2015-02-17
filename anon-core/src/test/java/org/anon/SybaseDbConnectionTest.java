package org.anon;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.exec.BaseDbTest;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.SybaseDbConnection;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class SybaseDbConnectionTest extends BaseDbTest{

	@Autowired
    protected DataSource dataSourceSybase;
	
	@Value("${sybase.test.schema}")
	protected String schema;
	
    @Test
    public void testSchemas(){

		SybaseDbConnection sybaseDbConnection = createConn();
		System.out.println(sybaseDbConnection.getSchemas());
		System.out.println(sybaseDbConnection.getDefaultSchema());
   	
    }

	private SybaseDbConnection createConn() {
		SybaseDbConnection sybaseDbConnection = new SybaseDbConnection(schema);
		sybaseDbConnection.setDataSource(dataSourceSybase);
		return sybaseDbConnection;
	}
    
	@Test
	public void testTables() {
		SybaseDbConnection sybaseDbConnection = createConn();
		
		List<DatabaseTableInfo> res = sybaseDbConnection.getTableList(schema);
		
		Assert.assertTrue(res.size() >= 1);
		Assert.assertTrue(res.get(0).getRowCount() > 0);
		
	}


    
	@Test
	public void testGetColumns() {
		SybaseDbConnection sybaseDbConnection = createConn();

		List<DatabaseTableInfo> res = sybaseDbConnection.getTableList(schema);

		String tableName = res.get(0).getName();
		
		DatabaseTableInfo databaseTableInfo = new DatabaseTableInfo();
		databaseTableInfo.setName(tableName);
		databaseTableInfo.setSchema(schema);
		System.out.println(sybaseDbConnection.getColumns(databaseTableInfo));
	}

	@Test
	public void testSupportedDatatypes() {
		SybaseDbConnection sybaseDbConnection = createConn();
		
		List<DatabaseTableInfo> res = sybaseDbConnection.getTableList(schema);
		
		for (DatabaseTableInfo databaseTableInfo : res) {
			for(DatabaseColumnInfo databaseColumnInfo:databaseTableInfo.getColumns()){
				if(!DatabaseSpecifics.SybaseSpecific.isSupportedDataType(databaseColumnInfo)){
					System.out.println("Unsupported datatype on column " + databaseColumnInfo + ":"+   databaseColumnInfo.getType());
				}

//				Assert.assertTrue("Unsupported datatype on column " + databaseColumnInfo + ":"+  databaseColumnInfo.getType(), DatabaseSpecifics.SybaseSpecific.isSupportedDataType(databaseColumnInfo));	
			}
			
		}
		
	}

}
