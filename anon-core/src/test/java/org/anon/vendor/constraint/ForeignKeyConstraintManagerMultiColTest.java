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
import org.anon.vendor.constraint.referential.ForeignKeyConstraint;
import org.anon.vendor.constraint.referential.ForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.MySqlForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.OracleForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.SqlServerForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.SybaseForeignKeyConstraintManager;
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
public class ForeignKeyConstraintManagerMultiColTest extends BaseParametrisedDbTest{

	private ForeignKeyConstraintManager constraintManager;
	private Class<ForeignKeyConstraintManager> constraintManagerClass;
	private String schema;
	private DatabaseSpecifics databaseSpecifics;
	
	public ForeignKeyConstraintManagerMultiColTest(Class<ForeignKeyConstraintManager> constraintManagerClass,  String schema, DatabaseSpecifics databaseSpecifics) {
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
	    new TestTableCreatorSupport().runScript(new JdbcTemplate(dataSource), "/MULTI_COL_FK_TABLES.sql", databaseSpecifics.getUseSchemaSql(schema));
	}
	
	
	@After
	public void dropMultiColumnTables() throws IOException{
	    DataSource dataSource = getDataSource();
	    new TestTableDropSupport().runScript(new JdbcTemplate(dataSource), "/MULTI_COL_FK_TABLES_DROP.sql", databaseSpecifics.getUseSchemaSql(schema));
	}

	@Parameters(name= "schema:{1}, {0}")
	public static Collection<Object []> generateData() {
		List<Object[]> res = new ArrayList<>();
        if(isDbAvailable(DB.sybase)) res.add(new Object[]{ SybaseForeignKeyConstraintManager.class, getTestSchema(DB.sybase), DatabaseSpecifics.SybaseSpecific});
        if(isDbAvailable(DB.mysql)) res.add(new Object[]{ MySqlForeignKeyConstraintManager.class , "employees", DatabaseSpecifics.MySqlSpecific});
        if(isDbAvailable(DB.oracle)) res.add(new Object[]{ OracleForeignKeyConstraintManager.class , getTestSchema(DB.oracle), DatabaseSpecifics.OracleSpecific});
        if(isDbAvailable(DB.sqlserver)) res.add(new Object[]{ SqlServerForeignKeyConstraintManager.class , "Northwind", DatabaseSpecifics.SqlServerSpecific});
		
		return res;
	}

	@Test
	public void testFkTo() throws IOException {
		List<? extends ForeignKeyConstraint> fks = constraintManager.loadForeignKeysTo("TMP_TABLE_C", "COL1", schema);
		Assert.assertEquals("number of loadForeignKeysTo", 1,  fks.size());
		List<Constraint> deactivated = constraintManager.deactivateConstraints("TMP_TABLE_C", "COL1", schema);
		constraintManager.activateConstraints(deactivated);
		List<? extends ForeignKeyConstraint> fks2 = constraintManager.loadForeignKeysTo("TMP_TABLE_C", "COL1", schema);
		Assert.assertEquals("number of loadForeignKeysTo after drop and recreate", 1,  fks2.size());
	}
	
	@Test
	public void testFkFrom() throws IOException {
		List<? extends ForeignKeyConstraint> fks = constraintManager.loadForeignKeysFrom("TMP_TABLE_D", "COL1_REF", schema);
		Assert.assertEquals("number of loadForeignKeysFrom", 1,  fks.size());
		List<Constraint> deactivated = constraintManager.deactivateConstraints("TMP_TABLE_D", "COL1_REF", schema);
		constraintManager.activateConstraints(deactivated);
		List<? extends ForeignKeyConstraint> fks2 = constraintManager.loadForeignKeysFrom("TMP_TABLE_D", "COL1_REF", schema);
		Assert.assertEquals("number of loadForeignKeysFrom after drop and recreate", 1,  fks2.size());
	}
	@Test
	public void testNoFk() throws IOException {
		List<? extends ForeignKeyConstraint> fks = constraintManager.loadForeignKeysFrom("TMP_TABLE_E", "COL1", schema);
		Assert.assertEquals("number of loadForeignKeysFrom", 0,  fks.size());
		fks = constraintManager.loadForeignKeysTo("TMP_TABLE_E", "COL1", schema);
		Assert.assertEquals("number of loadForeignKeysTo", 0,  fks.size());

	}
	
	@Test
	public void testMultVolumnLoadConstraints() throws IOException {
		List<? extends ForeignKeyConstraint> fks = constraintManager.loadForeignKeysTo("TMP_TABLE_A", "COL1", schema);
		Assert.assertEquals("number of loadForeignKeysTo", 1,  fks.size());
		fks = constraintManager.loadForeignKeysTo("TMP_TABLE_A", "COL2", schema);
		Assert.assertEquals("number of loadForeignKeysTo", 1,  fks.size());
		fks = constraintManager.loadForeignKeysFrom("TMP_TABLE_B", "COL1_REF", schema);
		Assert.assertEquals("number of loadForeignKeysFrom", 1,  fks.size());
		fks = constraintManager.loadForeignKeysFrom("TMP_TABLE_B", "COL2_REF", schema);
		Assert.assertEquals("number of loadForeignKeysFrom", 1,  fks.size());
	}


}
