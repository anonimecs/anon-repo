package org.anon.vendor.constraint.notnull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.exec.BaseParametrisedDbTest;
import org.anon.exec.TestTableCreatorSupport;
import org.anon.exec.TestTableDropSupport;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;


@RunWith(Parameterized.class)
@ContextConfiguration("classpath:spring-test-datasources.xml")
public class NotNullConstraintManagerTest extends BaseParametrisedDbTest{

	private NotNullConstraintManager notNullConstraintManager; 
	private String schema;
	private DatabaseSpecifics databaseSpecifics;
	
	DatabaseColumnInfo databaseColumnInfo;
	DatabaseTableInfo databaseTableInfo;
	JdbcTemplate jdbcTemplate ;
	

	
	public NotNullConstraintManagerTest(String schema, DatabaseSpecifics databaseSpecifics) {
		super();
		this.schema = schema;
		this.databaseSpecifics = databaseSpecifics;
	}

	@Before
	public void setUpContext() throws Exception {
		setUpContextBase();

	    DataSource dataSource = getDataSource();
	    notNullConstraintManager = new NotNullConstraintManager(dataSource);
	    
		dropMultiColumnTables();
		createMultiColumnTables();
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		
		databaseTableInfo = new DatabaseTableInfo();
		databaseTableInfo.setName("NEEDS_TO_BE_DEFINED_IN_TEST");
		databaseTableInfo.setSchema(schema);
		databaseColumnInfo = new DatabaseColumnInfo("COL1", "VARCHAR", false, databaseSpecifics);
		databaseTableInfo.addColumn(databaseColumnInfo);


	}
	

	@Override
	protected DataSource getDataSource() {
		return getDataSourceFor(databaseSpecifics.toString().toLowerCase());
	}
	

	private void createMultiColumnTables() throws IOException {
	    DataSource dataSource = getDataSource();
	    new TestTableCreatorSupport().runScript(new JdbcTemplate(dataSource), "/NOT_NULL_TEST_TABLES.sql", databaseSpecifics.getUseSchemaSql(schema));
	}
	
	@After
	public void dropMultiColumnTables() throws IOException{
	    DataSource dataSource = getDataSource();
	    new TestTableDropSupport().runScript(new JdbcTemplate(dataSource), "/NOT_NULL_TEST_TABLES_DROP.sql", databaseSpecifics.getUseSchemaSql(schema));
	}

	@Parameters(name= "schema:{1}, {0}")
	public static Collection<Object []> generateData() {
		List<Object[]> res = new ArrayList<>();
        if(isDbAvailable(DB.sybase)) res.add(new Object[]{  getTestSchema(DB.sybase), DatabaseSpecifics.SybaseSpecific});
        if(isDbAvailable(DB.mysql)) res.add(new Object[]{ getTestSchema(DB.mysql), DatabaseSpecifics.MySqlSpecific});
        if(isDbAvailable(DB.oracle)) res.add(new Object[]{  getTestSchema(DB.oracle), DatabaseSpecifics.OracleSpecific});
        if(isDbAvailable(DB.sqlserver)) res.add(new Object[]{   getTestSchema(DB.sqlserver), DatabaseSpecifics.SqlServerSpecific});
		
		return res;
	}
	
	@Test
	public void testActivateNoPk() {
		databaseTableInfo.setName("TMP_TABLE_A");
		// need to deactivate first
		testDeactivateNoPk();
		notNullConstraintManager.activateConstraints(databaseColumnInfo);

	}

	@Test
	public void testDeactivateNoPk() {
		databaseTableInfo.setName("TMP_TABLE_A");
		notNullConstraintManager.deactivateConstraint(databaseColumnInfo);
	}

	@Test
	public void testActivatePk() {
		Assume.assumeFalse(databaseSpecifics == DatabaseSpecifics.SybaseSpecific);
		databaseTableInfo.setName("TMP_TABLE_B");
		// need to deactivate first
		testDeactivatePk();
		notNullConstraintManager.activateConstraints(databaseColumnInfo);

	}

	@Test
	public void testDeactivatePk() {
		Assume.assumeFalse(databaseSpecifics == DatabaseSpecifics.SybaseSpecific);
		databaseTableInfo.setName("TMP_TABLE_B");
		notNullConstraintManager.deactivateConstraint(databaseColumnInfo);
	}



	
}
