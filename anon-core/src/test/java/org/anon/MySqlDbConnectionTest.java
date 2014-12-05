package org.anon;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseTableInfo;
import org.anon.exec.BaseDbTest;
import org.anon.exec.TestTableCreatorMySql;
import org.anon.vendor.MySqlDbConnection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

public class MySqlDbConnectionTest extends BaseDbTest{

    @Autowired
    protected DataSource dataSourceMySql;

	@Value("${mysql.test.schema}")
	protected String schema;

	@Test
	public void testGetColumns() {
		MySqlDbConnection mysqlDbConnection = createConn();
		
		DatabaseTableInfo databaseTableInfo = new DatabaseTableInfo();
		databaseTableInfo.setName("TMP_TABLE_A");
		System.out.println(mysqlDbConnection.getColumns(databaseTableInfo));
	}


	private MySqlDbConnection createConn() {
		MySqlDbConnection mysqlDbConnection = new MySqlDbConnection(schema);
		mysqlDbConnection.setDataSource(dataSourceMySql);
		return mysqlDbConnection;
	}
	
	@Before
	public void createTable() throws IOException{
		
		new TestTableCreatorMySql().createTables(new JdbcTemplate(dataSourceMySql));
	}
	
	@After
	public void dropTableA() throws IOException{
		new TestTableCreatorMySql().dropTableA(new JdbcTemplate(dataSourceMySql));
	}
	
	@After
	public void dropTableB() throws IOException{
		new TestTableCreatorMySql().dropTableB(new JdbcTemplate(dataSourceMySql));
	}
	  
	@Test
	public void testTables() {
		MySqlDbConnection mysqlDbConnection = createConn();
		
		List<DatabaseTableInfo> res = mysqlDbConnection.getTableList(schema);
		
		Assert.assertTrue(res.size() == 2);
	}

}