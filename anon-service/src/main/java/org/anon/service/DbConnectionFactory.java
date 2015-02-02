package org.anon.service;

import java.util.Properties;

import javax.sql.DataSource;

import org.anon.AbstractDbConnection;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.vendor.DatabaseSpecifics;

public interface DbConnectionFactory {

	public abstract DatabaseSpecifics getDatabaseSpecifics();

	public abstract AbstractDbConnection getConnection();

	public abstract AbstractDbConnection getConnectionForSchema(String schema);

	public abstract DataSource getDatasource(Properties props);

	public abstract DatabaseConfig getDatabaseConfig();

	public abstract void setDatabaseConfig(DatabaseConfig databaseConfig);

	public abstract void clearConnection();

}