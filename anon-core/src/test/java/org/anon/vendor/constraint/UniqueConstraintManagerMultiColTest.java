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
import org.anon.vendor.constraint.unique.SybaseUniqueConstraintManager;
import org.anon.vendor.constraint.unique.UniqueConstraint;
import org.anon.vendor.constraint.unique.UniqueConstraintManager;
import org.junit.After;
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
	    
		dropMultiColumnTables();
		createMultiColumnTables();

	}
	

	@Override
	protected DataSource getDataSource() {
		return getDataSourceFor(constraintManagerClass);
	}
	

	private void createMultiColumnTables() throws IOException {
	    DataSource dataSource = getDataSource();
	    new TestTableCreatorSupport().runScript(new JdbcTemplate(dataSource), "/MULTI_COL_UNIQUE_TABLES.sql", databaseSpecifics.getUseSchemaSql(schema));
	}
	
	@After
	public void dropMultiColumnTables() throws IOException{
	    DataSource dataSource = getDataSource();
	    new TestTableDropSupport().runScript(new JdbcTemplate(dataSource), "/MULTI_COL_UNIQUE_TABLES_DROP.sql", databaseSpecifics.getUseSchemaSql(schema));
	}

	@Parameters(name= "schema:{1}, {0}")
	public static Collection<Object []> generateData() {
		List<Object[]> res = new ArrayList<>();
        if(isDbAvailable(DB.sybase)) res.add(new Object[]{ SybaseUniqueConstraintManager.class, getTestSchema(DB.sybase), DatabaseSpecifics.SybaseSpecific});
//        if(isDbAvailable(DB.mysql)) res.add(new Object[]{ MySqlUniqueConstraintManager.class , getTestSchema(DB.mysql), DatabaseSpecifics.MySqlSpecific});
//        if(isDbAvailable(DB.oracle)) res.add(new Object[]{ OracleUniqueConstraintManager.class , getTestSchema(DB.oracle), DatabaseSpecifics.OracleSpecific});
//        if(isDbAvailable(DB.sqlserver)) res.add(new Object[]{ SqlServerUniqueConstraintManager.class ,  getTestSchema(DB.sqlserver), DatabaseSpecifics.SqlServerSpecific});
		
		return res;
	}
	
	@Test
	public void testMultiColumnUnique() throws IOException {
			List<? extends UniqueConstraint> constraints = constraintManager.loadUniques("TMP_TABLE_A", "COL1", schema);
			Assert.assertEquals("loaded 1 constraint", 1,  constraints.size());
			Assert.assertEquals("has 2 cols", 2, constraints.get(0).getColumNames().size());
			List<UniqueConstraint> deactivated = constraintManager.deactivateConstraints("TMP_TABLE_A", "COL1", schema);
			constraintManager.activateConstraints(deactivated);
		
	}

	@Test
	public void testUnique() throws IOException {
			List<? extends UniqueConstraint> constraints = constraintManager.loadUniques("TMP_TABLE_B", "COL1", schema);
			Assert.assertEquals("loaded 1 constraint", 1,  constraints.size());
			Assert.assertEquals("has 1 cols", false, constraints.get(0).isMultiCol());
			List<UniqueConstraint> deactivated = constraintManager.deactivateConstraints("TMP_TABLE_B", "COL1", schema);
			constraintManager.activateConstraints(deactivated);
	}

	@Test
	public void testNoUnique() throws IOException {
			
			List<? extends UniqueConstraint> constraints = constraintManager.loadUniques("TMP_TABLE_C", "COL1", schema);
			Assert.assertEquals("loaded 0 constraint", 0,  constraints.size());
	}

	
	@Test
	public void testMultiColumnPk() throws IOException {
			List<? extends UniqueConstraint> constraints = constraintManager.loadPrimaryKeys("TMP_TABLE_D", "COL1", schema);
			Assert.assertEquals("loaded 1 constraint", 1,  constraints.size());
			Assert.assertEquals("has 2 cols", 2, constraints.get(0).getColumNames().size());
			List<UniqueConstraint> deactivated = constraintManager.deactivateConstraints("TMP_TABLE_D", "COL1", schema);
			constraintManager.activateConstraints(deactivated);
		
	}

	@Test
	public void testPk() throws IOException {
			List<? extends UniqueConstraint> constraints = constraintManager.loadPrimaryKeys("TMP_TABLE_E", "COL1", schema);
			Assert.assertEquals("loaded 1 constraint", 1,  constraints.size());
			Assert.assertEquals("has 1 cols", false, constraints.get(0).isMultiCol());
			List<UniqueConstraint> deactivated = constraintManager.deactivateConstraints("TMP_TABLE_E", "COL1", schema);
			constraintManager.activateConstraints(deactivated);
	}

	@Test
	public void testNoPk() throws IOException {
			
			List<? extends UniqueConstraint> constraints = constraintManager.loadPrimaryKeys("TMP_TABLE_F", "COL1", schema);
			Assert.assertEquals("loaded 0 constraint", 0,  constraints.size());
	}

	
}
