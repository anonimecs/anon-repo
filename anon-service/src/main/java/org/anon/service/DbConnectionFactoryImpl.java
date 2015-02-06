package org.anon.service;

import java.util.Properties;

import javax.sql.DataSource;

import org.anon.AbstractDbConnection;
import org.anon.AnonStatic;
import org.anon.data.Database;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.MySqlDbConnection;
import org.anon.vendor.OracleDbConnection;
import org.anon.vendor.SybaseDbConnection;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.springframework.stereotype.Service;

@Service("dbConnectionFactory")
public class DbConnectionFactoryImpl extends AnonStatic implements DbConnectionFactory{
	
	private DatabaseConfig databaseConfig;
	private AbstractDbConnection dbConnection;	
	

	@Override
	public DatabaseSpecifics getDatabaseSpecifics(){
		return getConnection().getDatabaseSpecifics();
	}


	@Override
	public AbstractDbConnection getConnection() {
		if(dbConnection == null){
			dbConnection = getConnectionForSchema(null);
		}
		return dbConnection;
	}
	
	@Override
	public AbstractDbConnection getConnectionForSchema(String schema) {
		
		AbstractDbConnection execConnection;
		
		switch(databaseConfig.getVendor()) {
			case MYSQL: execConnection = createMySqlConnection(schema);
				break;
			case ORACLE: execConnection = createOracleConnection(schema);
				break;
			case SYBASE: execConnection = createSybaseConnection(schema);
			break;
			default: throw new RuntimeException(databaseConfig.getVendor() + " not supported.");
		}
		return execConnection;
	}
	
	private AbstractDbConnection createSybaseConnection(String schema) {	
		AbstractDbConnection dbConnection = null;
		Properties props = createDbProps(Database.SYBASE);
		
		if(schema!=null) {
			props.setProperty("schema", schema);
		}
				
		SybaseDbConnection sybaseDbConnection = new SybaseDbConnection(parseSybSchemaFromUrl(databaseConfig.getUrl()));
		dbConnection = sybaseDbConnection;
		dbConnection.setProperties(props);
		dbConnection.setDataSource(getDatasource(props));
		return dbConnection;
	}
	
	private String parseSybSchemaFromUrl(String url) {
		int index = url.lastIndexOf('/');
		if(index != -1){
			return url.substring(index + 1);
		}
		else {
			return "tempdb";
		}
	}

	private AbstractDbConnection createOracleConnection(String schema) {	
		AbstractDbConnection dbConnection = null;
		Properties props = createDbProps(Database.ORACLE);
		
		if(schema!=null) {
			props.setProperty("schema", schema);
		}
		
		OracleDbConnection oracleDbConnection = new OracleDbConnection(databaseConfig.getLogin());
		dbConnection = oracleDbConnection;
		dbConnection.setProperties(props);
		dbConnection.setDataSource(getDatasource(props));	
		return dbConnection;
	}
	
	private AbstractDbConnection createMySqlConnection(String schema) {	
		AbstractDbConnection dbConnection = null;
		Properties props = createDbProps(Database.MYSQL);
		
		if(schema!=null) {
			props.setProperty("schema", schema);
		}
		
		MySqlDbConnection mysqlDbConnection = new MySqlDbConnection(databaseConfig.getLogin());
		dbConnection = mysqlDbConnection;
		dbConnection.setProperties(props);
		dbConnection.setDataSource(getDatasource(props));	
		return dbConnection;
	}
	
	private Properties createDbProps(Database db) {
		Properties props = new Properties();
		
		props.setProperty("driverClassName", db.getDriver());
		props.setProperty("url", db.getJdbcPrefix());
		props.setProperty("maxActive", JDBC_MAX_ACTIVE);
		props.setProperty("maxWait", JDBC_MAX_WAIT);
		
		return props;
	}
	
	@Override
	public DataSource getDatasource(Properties props) {
		DataSource ds = null;
		
		StringBuilder url = new StringBuilder(props.getProperty("url"))
			.append(databaseConfig.getUrl());
			
		if(props.containsKey("schema")) {
			url.append("/").append(props.get("schema"));
		}
			
		props.setProperty("url", url.toString());
		props.setProperty("username", databaseConfig.getLogin());
		props.setProperty("password", databaseConfig.getPassword());
		
		try {
			ds = BasicDataSourceFactory.createDataSource(props);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ds;
	}

	@Override
	public DatabaseConfig getDatabaseConfig() {
		return databaseConfig;
	}

	@Override
	public void setDatabaseConfig(DatabaseConfig databaseConfig) {
		this.databaseConfig = databaseConfig;
	}

	@Override
	public void clearConnection() {
		dbConnection = null;
		
	}
}
