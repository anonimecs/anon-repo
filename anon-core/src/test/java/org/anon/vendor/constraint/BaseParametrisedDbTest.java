package org.anon.vendor.constraint;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContextManager;

public abstract class BaseParametrisedDbTest {
	
	enum DB {mysql, sybase, oracle, sqlserver}
	
	@Autowired	DataSource dataSourceSybase;
	@Autowired	DataSource dataSourceOracle;
	@Autowired	DataSource dataSourceSqlServer;
	@Autowired	DataSource dataSourceMySql;

	protected TestContextManager testContextManager;

	static Properties properties = new Properties();
	static {
		try {
			properties.load(BaseParametrisedDbTest.class.getClassLoader().getResourceAsStream("jenkins.test.properties"));
			properties.load(BaseParametrisedDbTest.class.getClassLoader().getResourceAsStream("local.test.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	protected DataSource getDataSourceFor(Class clazz) {
		if(clazz.getSimpleName().contains("Sybase")){
			return dataSourceSybase;
		}
		if(clazz.getSimpleName().contains("Oracle")){
			return dataSourceOracle;
		}
		if(clazz.getSimpleName().contains("SqlServer")){
			return dataSourceSqlServer;
		}
		if(clazz.getSimpleName().contains("MySql")){
			return dataSourceMySql;
		}
		throw new RuntimeException("Unsupported class " + clazz);
	}
	
	protected static boolean isDbAvailable(DB db){
		return Boolean.valueOf(properties.getProperty(db+".available", "false"));
	}
	
	public void setUpContextBase() throws Exception {
	    this.testContextManager = new TestContextManager(getClass());
	    this.testContextManager.prepareTestInstance(this);
	}

}
