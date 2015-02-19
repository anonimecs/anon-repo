package org.anon.vendor.constraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.test.context.ContextConfiguration;

@SuppressWarnings({"rawtypes", "unchecked"})
@RunWith(Parameterized.class)
@ContextConfiguration("classpath:spring-test-datasources.xml")
public class ConstraintManagerTest extends BaseParametrisedDbTest{

	
	private ConstraintManager constraintManager;
	private Class<ConstraintManager> constraintManagerClass;
	private String tableName;
	private String columnName;
	private String schema;
	private int incomingConstraints;
	private int outgoingConstraints;
	
	public ConstraintManagerTest(Class<ConstraintManager> constraintManagerClass, String tableName, String columnName, String schema, 
			int incomingConstraints, int outgoingConstraints) {
		super();
		this.constraintManagerClass = constraintManagerClass;
		this.tableName = tableName;
		this.columnName = columnName;
		this.schema = schema;
		this.incomingConstraints = incomingConstraints;
		this.outgoingConstraints = outgoingConstraints;
	}

	@Before
	public void setUpContext() throws Exception {
		setUpContextBase();
		
	    DataSource dataSource = getDataSourceFor(constraintManagerClass);
	    constraintManager = constraintManagerClass.getConstructor(DataSource.class).newInstance(new Object[] {dataSource});
	}
	

	@Parameters(name= "table:{1}, column:{2}, schema:{3}, {0}")
	public static Collection<Object []> generateData() {
		List<Object[]> res = new ArrayList<>();
        if(isDbAvailable(DB.sybase)) res.add(new Object[]{ SybaseConstraintManager.class, "CPTY", "RBE_CodeCpty", "LIMEX_d", 7, 0});
        if(isDbAvailable(DB.sybase)) res.add(new Object[]{ SybaseConstraintManager.class, "CPTY", "SSY_SourceSystem", "LIMEX_d", 0, 1});
        if(isDbAvailable(DB.mysql)) res.add(new Object[]{ MySqlConstraintManager.class , "dept_emp", "dept_no", "employees", 0, 1});
        if(isDbAvailable(DB.mysql)) res.add(new Object[]{ MySqlConstraintManager.class , "departments", "dept_no", "employees", 2, 0});
        if(isDbAvailable(DB.oracle)) res.add(new Object[]{ OracleConstraintManager.class , "RIGHT", "ID", "ECAP", 1, 0});
        if(isDbAvailable(DB.oracle)) res.add(new Object[]{ OracleConstraintManager.class , "ROLE_RIGHT", "ROLE_ID", "ECAP", 0, 1});
		
		return res;
	}
	
	@Test
	public void testLoadConstraintsTo() {
		List<? extends Constraint> incomingFk = constraintManager.loadForeignKeysTo(tableName, columnName, schema);
		System.out.println("In:" + incomingFk);
		Assert.assertEquals("number of constraints", incomingConstraints,  incomingFk.size());
	}

	@Test
	public void testLoadConstraintsFrom() {
		List<? extends Constraint> outFk = constraintManager.loadForeignKeysFrom(tableName, columnName, schema);
		System.out.println("OUT:" + outFk);
		Assert.assertEquals("number of constraints", outgoingConstraints,  outFk.size());
	}

}
