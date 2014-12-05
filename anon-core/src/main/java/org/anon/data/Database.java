package org.anon.data;

public enum Database {

	MYSQL	("MySql",	"com.mysql.jdbc.Driver",			"jdbc:mysql://",		new String[]{"5.5"}),
	ORACLE	("Oracle",	"oracle.jdbc.driver.OracleDriver",	"jdbc:oracle:thin:@",	new String[]{"11g", "10x", "9y"}), 
	SYBASE	("Sybase", 	"com.sybase.jdbc4.jdbc.SybDriver",	"jdbc:sybase:Tds:",		new String[]{"15.7", "16.0"});

	private String name;
	private String driver;
	private String jdbcPrefix;
	private String[] versions;
	
	private Database(String name, String driver, String jdbcPrefix, String[] versions) {
		this.name = name;
		this.driver = driver;
		this.jdbcPrefix = jdbcPrefix;
		this.versions = versions;
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
}
