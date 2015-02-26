package org.anon.data;

import org.anon.vendor.DatabaseSpecifics;

public enum Database {

	SQLSERVER	("SqlServer",	"com.microsoft.sqlserver.jdbc.SQLServerDriver",			"jdbc:sqlserver://",		new String[]{"2008", "2012"}, DatabaseSpecifics.SqlServerSpecific),
	MYSQL	("MySql",	"com.mysql.jdbc.Driver",			"jdbc:mysql://",		new String[]{"5.5"}, DatabaseSpecifics.MySqlSpecific),
	ORACLE	("Oracle",	"oracle.jdbc.driver.OracleDriver",	"jdbc:oracle:thin:@",	new String[]{"11g", "10x", "9y"}, DatabaseSpecifics.OracleSpecific), 
	SYBASE	("Sybase", 	"com.sybase.jdbc4.jdbc.SybDriver",	"jdbc:sybase:Tds:",		new String[]{"15.7", "16.0"}, DatabaseSpecifics.SybaseSpecific);

	private String name;
	private String driver;
	private String jdbcPrefix;
	private String[] versions;
	private DatabaseSpecifics databaseSpecifics;
	
	private Database(String name, String driver, String jdbcPrefix, String[] versions, DatabaseSpecifics databaseSpecifics) {
		this.name = name;
		this.driver = driver;
		this.jdbcPrefix = jdbcPrefix;
		this.versions = versions;
		this.databaseSpecifics = databaseSpecifics;
	}

	public String getName() {
		return name;
	}
	
	public String getDriver() {
		return driver;
	}

	public String getJdbcPrefix() {
		return jdbcPrefix;
	}

	public String[] getVersions() {
		return versions;
	}
	public String getSchemaAppendix(String schema){
		if(this == SQLSERVER){
			return ";databaseName=" + schema;
		}
		return "/"+ schema;
	}
	
	public DatabaseSpecifics getDatabaseSpecifics() {
		return databaseSpecifics;
	}
}
