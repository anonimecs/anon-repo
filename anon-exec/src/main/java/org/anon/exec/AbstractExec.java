package org.anon.exec;

import java.util.Arrays;

import javax.sql.DataSource;

import org.anon.exec.audit.ExecAuditor;
import org.anon.license.LicenseException;
import org.anon.license.LicenseManager;
import org.anon.service.DbConnectionFactory;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.constraint.ConstraintBundleFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Subclasses are always prototypes, and are not multi threaded
 */
public abstract class AbstractExec {
	protected Logger logger = Logger.getLogger(getClass());

	@Autowired
	protected LicenseManager licenseManager;
	
	@Autowired
	protected ExecAuditor execAuditor;
	
	@Autowired
	protected ConstraintBundleFactory constraintBundleFactory;
	
	@Autowired
	protected DbConnectionFactory dbConnectionFactory;


	
	protected DataSource dataSource;
	protected String userName;
	protected int tablesProcessed;

	protected abstract DatabaseSpecifics getDatabaseSpecifics();

	public abstract void runAll();
	
	protected void assertDataSourceSet() {
		if (dataSource == null){
			throw new RuntimeException("Data Source was not set");
		}
	}

	
	protected void assertFreeEditionRunCount() {
		if(licenseManager.reachedMaxTablesAnonimised(tablesProcessed)){
			throw new LicenseException("Reached maximal number of tables " + licenseManager.getMaxTablesAnonimised());
		}
		tablesProcessed++;
	}

	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected 	void execute(String sql) {
		Logger.getLogger(getClass()).log(Level.DEBUG, "Executing " + sql);
		new JdbcTemplate(dataSource).execute(sql);
	}
	
	protected int update(String sql, Object... args){
		Logger.getLogger(getClass()).log(Level.DEBUG, "Updating " + sql + "\n Params: " + Arrays.toString(args));
		return new JdbcTemplate(dataSource).update(sql, args); 
	}



}
