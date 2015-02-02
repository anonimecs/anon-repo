package org.anon.exec;

import java.util.Properties;

import javax.sql.DataSource;

import org.anon.AbstractDbConnection;
import org.anon.data.Database;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.service.DbConnectionFactory;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.MySqlDbConnection;
import org.anon.vendor.OracleDbConnection;
import org.anon.vendor.SybaseDbConnection;

public class DummyConnectionFactory implements DbConnectionFactory {

	private DataSource datasource;
	private Database vendor;
	
	public DummyConnectionFactory(DataSource datasource, Database vendor) {
		this.datasource = datasource;
		this.vendor = vendor;
	}
	
	@Override
	public DatabaseSpecifics getDatabaseSpecifics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractDbConnection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractDbConnection getConnectionForSchema(String schema) {
		AbstractDbConnection connection = null;
		
		switch(vendor) {
			case MYSQL: connection = new MySqlDbConnection(schema);
				break;
			case ORACLE: connection = new OracleDbConnection(schema);
				break;
			case SYBASE: connection = new SybaseDbConnection(schema);
				break;
			default: throw new RuntimeException(vendor + " not supported.");
		}
		connection.setDataSource(datasource);
		return connection;
	}

	@Override
	public DataSource getDatasource(Properties props) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatabaseConfig getDatabaseConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDatabaseConfig(DatabaseConfig databaseConfig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearConnection() {
		// TODO Auto-generated method stub
		
	}

}
