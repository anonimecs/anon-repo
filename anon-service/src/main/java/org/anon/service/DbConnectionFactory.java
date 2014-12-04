package org.anon.service;

import java.util.Properties;

import javax.sql.DataSource;

import org.anon.AbstractDbConnection;
import org.anon.AnonStatic;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.MySqlDbConnection;
import org.anon.vendor.OracleDbConnection;
import org.anon.vendor.SybaseDbConnection;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.springframework.stereotype.Service;

@Service
public class DbConnectionFactory extends AnonStatic{

	
	private DatabaseConfig databaseConfig;
	private AbstractDbConnection dbConnection;
	
	
	public DatabaseSpecifics getDatabaseSpecifics(){
		return getConnection().getDatabaseSpecifics();
	}

	public AbstractDbConnection getConnection() {
		
		if(dbConnection == null){
	
			if(databaseConfig.getVendor().equals(SYBASE)) {
				dbConnection = createSybaseConnection();
			}
			else if(databaseConfig.getVendor().equals(ORACLE)) {
				dbConnection = createOracleConnection();
			}
			else if(databaseConfig.getVendor().equals(MYSQL)) {
				dbConnection = createMySqlConnection();
			}
		}
		return dbConnection;
	}
	
	private AbstractDbConnection createSybaseConnection() {	
		AbstractDbConnection dbConnection = null;
		Properties props = new Properties();
		
		props.setProperty("driverClassName", SYBASE_DRIVER_CLASS);
		props.setProperty("url", SYBASE_JDBC_PREFIX);
		props.setProperty("maxActive", JDBC_MAX_ACTIVE);
		props.setProperty("maxWait", JDBC_MAX_WAIT);
				
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

	private AbstractDbConnection createOracleConnection() {	
		AbstractDbConnection dbConnection = null;
		Properties props = new Properties();
		
		props.setProperty("driverClassName", ORACLE_DRIVER_CLASS);
		props.setProperty("url", ORACLE_JDBC_PREFIX);
		props.setProperty("maxActive", JDBC_MAX_ACTIVE);
		props.setProperty("maxWait", JDBC_MAX_WAIT);
		
		OracleDbConnection oracleDbConnection = new OracleDbConnection(databaseConfig.getLogin());
		dbConnection = oracleDbConnection;
		dbConnection.setProperties(props);
		dbConnection.setDataSource(getDatasource(props));	
		return dbConnection;
	}
	
	private AbstractDbConnection createMySqlConnection() {	
		AbstractDbConnection dbConnection = null;
		Properties props = new Properties();
		
		props.setProperty("driverClassName", MYSQL_DRIVER_CLASS);
		props.setProperty("url", MYSQL_JDBC_PREFIX);
		props.setProperty("maxActive", JDBC_MAX_ACTIVE);
		props.setProperty("maxWait", JDBC_MAX_WAIT);
		
		MySqlDbConnection mysqlDbConnection = new MySqlDbConnection(databaseConfig.getLogin());
		dbConnection = mysqlDbConnection;
		dbConnection.setProperties(props);
		dbConnection.setDataSource(getDatasource(props));	
		return dbConnection;
	}
	
	public DataSource getDatasource(Properties props) {
		DataSource ds = null;

		props.setProperty("url", props.getProperty("url") + databaseConfig.getUrl());
		props.setProperty("username", databaseConfig.getLogin());
		props.setProperty("password", databaseConfig.getPassword());
		
		try {
			ds = BasicDataSourceFactory.createDataSource(props);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ds;
	}

	public DatabaseConfig getDatabaseConfig() {
		return databaseConfig;
	}

	public void setDatabaseConfig(DatabaseConfig databaseConfig) {
		this.databaseConfig = databaseConfig;
	}

	public void clearConnection() {
		dbConnection = null;
		
	}
}
