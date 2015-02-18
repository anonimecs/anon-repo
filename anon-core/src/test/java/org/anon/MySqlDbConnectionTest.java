package org.anon;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.RelatedTableColumnInfo;
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
		databaseTableInfo.setSchema(schema);
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
	public void dropTables() throws IOException{
		new TestTableCreatorMySql().dropTableB(new JdbcTemplate(dataSourceMySql));
		new TestTableCreatorMySql().dropTableA(new JdbcTemplate(dataSourceMySql));
	}
	  
	@Test
	public void testTables() {
		MySqlDbConnection mysqlDbConnection = createConn();
		
		List<DatabaseTableInfo> res = mysqlDbConnection.getTableList(schema);
		
		Assert.assertTrue("at least two tables exist", res.size() >= 2);
	}
	
	@Test
	public void testEmployeesSchemaRelatedTables(){
		MySqlDbConnection mysqlDbConnection = new MySqlDbConnection("employees");
		mysqlDbConnection.setDataSource(dataSourceMySql);
		DatabaseTableInfo editedTable = new DatabaseTableInfo();
		editedTable.setName("departments");
		editedTable.setSchema("employees");
		DatabaseColumnInfo editedColumn = new DatabaseColumnInfo();
		editedColumn.setName("dept_no");
		List<RelatedTableColumnInfo> res  = mysqlDbConnection.findRelatedTables(editedTable, editedColumn);
		Assert.assertEquals("2 records expected", 2, res.size());
		
	}

}
