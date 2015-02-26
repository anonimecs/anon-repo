package org.anon.exec;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContextManager;

public abstract class BaseParametrisedDbTest {
	
	public enum DB {mysql, sybase, oracle, sqlserver}
	
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

	protected DataSource getDataSourceFor(String name) {
		if(name.toLowerCase().contains("sybase")){
			return dataSourceSybase;
		}
		if(name.toLowerCase().contains("oracle")){
			return dataSourceOracle;
		}
		if(name.toLowerCase().contains("sqlserver")){
			return dataSourceSqlServer;
		}
		if(name.toLowerCase().contains("mysql")){
			return dataSourceMySql;
		}
		throw new RuntimeException("Unsupported " + name);
	
	}
	
	protected DataSource getDataSourceFor(Class clazz) {
		return getDataSourceFor(clazz.getSimpleName());
	}
	
	protected static boolean isDbAvailable(DB db){
		return Boolean.valueOf(properties.getProperty(db+".available", "false"));
	}
	
	public void setUpContextBase() throws Exception {
	    this.testContextManager = new TestContextManager(getClass());
	    this.testContextManager.prepareTestInstance(this);
	}

}
