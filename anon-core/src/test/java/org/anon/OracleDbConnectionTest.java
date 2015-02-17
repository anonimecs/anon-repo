package org.anon;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.exec.BaseDbTest;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.OracleDbConnection;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class OracleDbConnectionTest extends BaseDbTest{

    @Autowired
    protected DataSource dataSourceOracle;

	@Value("${oracle.test.schema}")
	protected String schema;

	@Test
	public void testGetColumns() {
		OracleDbConnection oracleDbConnection = createConn();
		
		DatabaseTableInfo databaseTableInfo = new DatabaseTableInfo();
		databaseTableInfo.setName("ROLE");
		System.out.println(oracleDbConnection.getColumns(databaseTableInfo));
	}


	private OracleDbConnection createConn() {
		OracleDbConnection oracleDbConnection = new OracleDbConnection(schema);
		oracleDbConnection.setDataSource(dataSourceOracle);
		return oracleDbConnection;
	}
	
	  
	@Test
	public void testTables() {
		OracleDbConnection oracleDbConnection = createConn();
		
		List<DatabaseTableInfo> res = oracleDbConnection.getTableList(schema);
		
		Assert.assertTrue(res.size() > 10);
	}

	@Test
	public void testSupportedDatatypes() {
		OracleDbConnection oracleDbConnection = createConn();
		
		List<DatabaseTableInfo> res = oracleDbConnection.getTableList(schema);
		
		for (DatabaseTableInfo databaseTableInfo : res) {
			for(DatabaseColumnInfo databaseColumnInfo:databaseTableInfo.getColumns()){
				String type = databaseColumnInfo.getType();
				if(!type.contains("LOB")){
					Assert.assertTrue("Unsupported datatype on column " + databaseColumnInfo + ":"+  type, DatabaseSpecifics.OracleSpecific.isSupportedDataType(databaseColumnInfo));	
					
				}
			}
			
		}
		
	}
}
