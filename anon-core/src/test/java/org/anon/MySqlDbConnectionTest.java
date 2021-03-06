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
import org.junit.Assume;
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
		Assume.assumeTrue(mysqlAvailable);
		
		
		dropTables();
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceMySql);
		jdbcTemplate.execute("use "+schema);
		new TestTableCreatorMySql().createTables(jdbcTemplate);
	}
	
	@After
	public void dropTables() throws IOException{
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceMySql);
		jdbcTemplate.execute("use "+schema);
		try{new TestTableCreatorMySql().dropTableB(jdbcTemplate);}catch(Exception ignore){}
		try{new TestTableCreatorMySql().dropTableA(jdbcTemplate);}catch(Exception ignore){}
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
		if(mysqlDbConnection.getSchemas().contains("employees")){
			DatabaseTableInfo editedTable = new DatabaseTableInfo();
			editedTable.setName("departments");
			editedTable.setSchema("employees");
			DatabaseColumnInfo editedColumn = new DatabaseColumnInfo();
			editedColumn.setName("dept_no");
			List<RelatedTableColumnInfo> res  = mysqlDbConnection.findRelatedTables(editedTable, editedColumn);
			Assert.assertEquals("2 records expected", 2, res.size());
		}
		else {
			Assert.fail("employees schema not available in the test database");
		}
	}

}
