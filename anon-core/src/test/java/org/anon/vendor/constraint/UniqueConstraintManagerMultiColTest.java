package org.anon.vendor.constraint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.anon.exec.BaseParametrisedDbTest;
import org.anon.exec.TestTableCreatorSupport;
import org.anon.exec.TestTableDropSupport;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.constraint.unique.MySqlUniqueConstraintManager;
import org.anon.vendor.constraint.unique.UniqueConstraint;
import org.anon.vendor.constraint.unique.UniqueConstraintManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@SuppressWarnings({"rawtypes", "unchecked"})
@RunWith(Parameterized.class)
@ContextConfiguration("classpath:spring-test-datasources.xml")
public class UniqueConstraintManagerMultiColTest extends BaseParametrisedDbTest{

	private UniqueConstraintManager constraintManager;
	private Class<UniqueConstraintManager> constraintManagerClass;
	private String schema;
	private DatabaseSpecifics databaseSpecifics;
	
	public UniqueConstraintManagerMultiColTest(Class<UniqueConstraintManager> constraintManagerClass,  String schema, DatabaseSpecifics databaseSpecifics) {
		super();
		this.constraintManagerClass = constraintManagerClass;
		this.schema = schema;
		this.databaseSpecifics = databaseSpecifics;
	}

	@Before
	public void setUpContext() throws Exception {
		setUpContextBase();

	    DataSource dataSource = getDataSource();
	    constraintManager = constraintManagerClass.getConstructor(DataSource.class).newInstance(new Object[] {dataSource});
	}

	@Override
	protected DataSource getDataSource() {
		return getDataSourceFor(constraintManagerClass);
	}
	

	private void createMultiColumnTables() throws IOException {
	    DataSource dataSource = getDataSource();
	    new TestTableCreatorSupport().runScript(new JdbcTemplate(dataSource), "/MULTI_COL_UNIQUE_TABLES.sql", databaseSpecifics.getUseSchemaSql(schema));
	}
	
	private void dropMultiColumnTables() throws IOException{
	    DataSource dataSource = getDataSource();
	    new TestTableDropSupport().runScript(new JdbcTemplate(dataSource), "/MULTI_COL_UNIQUE_TABLES_DROP.sql", databaseSpecifics.getUseSchemaSql(schema));
	}

	@Parameters(name= "schema:{1}, {0}")
	public static Collection<Object []> generateData() {
		List<Object[]> res = new ArrayList<>();
//        if(isDbAvailable(DB.sybase)) res.add(new Object[]{ SybaseUniqueConstraintManager.class, getTestSchema(DB.sybase), DatabaseSpecifics.SybaseSpecific});
        if(isDbAvailable(DB.mysql)) res.add(new Object[]{ MySqlUniqueConstraintManager.class , getTestSchema(DB.mysql), DatabaseSpecifics.MySqlSpecific});
//        if(isDbAvailable(DB.oracle)) res.add(new Object[]{ OracleUniqueConstraintManager.class , getTestSchema(DB.oracle), DatabaseSpecifics.OracleSpecific});
//        if(isDbAvailable(DB.sqlserver)) res.add(new Object[]{ SqlServerUniqueConstraintManager.class ,  getTestSchema(DB.sqlserver), DatabaseSpecifics.SqlServerSpecific});
		
		return res;
	}
	
	@Test
	public void testMultiColumn() throws IOException {
		try{
			dropMultiColumnTables();
			createMultiColumnTables();
			
			List<? extends UniqueConstraint> constraints = constraintManager.loadConstraints("TMP_TABLE_A", "COL1", schema);
			Assert.assertEquals("loaded 1 constraint", 1,  constraints.size());
			Assert.assertEquals("has 2 cols", 2, constraints.get(0).getColumNames().size());
		}
		finally{
			dropMultiColumnTables();
		}
		
	}

	@Test
	public void test() throws IOException {
		try{
			dropMultiColumnTables();
			createMultiColumnTables();
			
			List<? extends UniqueConstraint> constraints = constraintManager.loadConstraints("TMP_TABLE_B", "COL1_REF", schema);
			Assert.assertEquals("loaded 1 constraint", 1,  constraints.size());
			Assert.assertEquals("has 1 cols", false, constraints.get(0).isMultiCol());
		}
		finally{
			dropMultiColumnTables();
		}
		
	}

	
}
