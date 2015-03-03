package org.anon.logic.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.Database;
import org.anon.exec.BaseExec;
import org.anon.exec.BaseParametrisedDbTest;
import org.anon.exec.DummyConnectionFactory;
import org.anon.exec.MySqlExec;
import org.anon.exec.NullGuiNotifier;
import org.anon.exec.OracleExec;
import org.anon.exec.SqlServerExec;
import org.anon.exec.SybaseExec;
import org.anon.exec.TestTableCreatorMySql;
import org.anon.exec.TestTableCreatorOracle;
import org.anon.exec.TestTableCreatorSqlServer;
import org.anon.exec.TestTableCreatorSupport;
import org.anon.exec.TestTableCreatorSybase;
import org.anon.exec.TestTableDropSupport;
import org.anon.exec.TwoTestTablesCreator;
import org.anon.exec.audit.ExecAuditor;
import org.anon.exec.mock.LicenseManagerMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Parameterized.class)
@ContextConfiguration({"classpath:spring-test-datasources.xml", "classpath:BaseExecTest.xml"})
public abstract class TwoTablesAllDbTest extends BaseParametrisedDbTest{

	protected String schema;
	protected Database database;
	protected TwoTestTablesCreator twoTestTablesCreator;

	
	@Autowired
	protected ExecAuditor execAuditor;

	
	protected JdbcTemplate jdbcTemplate;
	
	protected JdbcTemplate getJdbcTemplate() {
		if(jdbcTemplate == null){
			jdbcTemplate = new JdbcTemplate(getDataSource());
			jdbcTemplate.execute(database.getDatabaseSpecifics().getUseSchemaSql(schema));
		}
		return jdbcTemplate;
	}

	
	public TwoTablesAllDbTest(String schema, Database database, TwoTestTablesCreator testTablesCreator ) {
		super();
		this.schema = schema;
		this.database = database;
		this.twoTestTablesCreator = testTablesCreator;
	}


	
	
	@Before
	public void setUpContext() throws Exception {
		setUpContextBase();
		
		dropTables();
		
		new TestTableCreatorSupport().runScript(getJdbcTemplate(), "/SINGLE_COL_FK_TABLES.sql", database.getDatabaseSpecifics().getUseSchemaSql(schema));
	}
	
	@After
	public void dropTables() throws IOException{
		try{
			new TestTableDropSupport().runScript(getJdbcTemplate(), "/SINGLE_COL_FK_TABLES_DROP.sql", database.getDatabaseSpecifics().getUseSchemaSql(schema));
		}catch(Exception ignore){
			ignore.printStackTrace();
		}
	}

	@Parameters(name= "schema:{1}, {0}")
	public static Collection<Object []> generateData() {
		List<Object[]> res = new ArrayList<>();
        if(isDbAvailable(DB.sybase)) res.add(new Object[]{ getTestSchema(DB.sybase), Database.SYBASE, new TestTableCreatorSybase()});
        if(isDbAvailable(DB.mysql)) res.add(new Object[]{ "employees", Database.MYSQL, new TestTableCreatorMySql()});
        if(isDbAvailable(DB.oracle)) res.add(new Object[]{  getTestSchema(DB.oracle), Database.ORACLE, new TestTableCreatorOracle() });
        if(isDbAvailable(DB.sqlserver)) res.add(new Object[]{  "Northwind", Database.SQLSERVER, new TestTableCreatorSqlServer()});
		
		return res;
	}

	
	protected BaseExec createBaseExec() {
		BaseExec baseExec = null;
		if(database == Database.SYBASE){
			baseExec = new SybaseExec();
		} else if(database == Database.ORACLE){
			baseExec = new OracleExec();
		} else if(database == Database.MYSQL){
			baseExec = new MySqlExec();
		} else if(database == Database.SQLSERVER){
			baseExec = new SqlServerExec();
		}
		DataSource dataSource = getDataSource();
		baseExec.setDataSource(dataSource);
		baseExec.setUserName("junit");
		baseExec.setLicenseManager(new LicenseManagerMock());
		baseExec.setExecAuditor(execAuditor);
		baseExec.setGuiNotifier(new NullGuiNotifier());
		baseExec.setDbConnectionFactory(new DummyConnectionFactory(dataSource, database));
		return baseExec;
	}


	@Override
	protected DataSource getDataSource() {
		return getDataSourceFor(database.toString());
	}
	
	
	@Test
	public void basicSetupTest() {

		System.out.println("OK");
	}

}
