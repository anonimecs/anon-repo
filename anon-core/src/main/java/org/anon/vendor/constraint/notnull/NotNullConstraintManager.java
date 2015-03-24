package org.anon.vendor.constraint.notnull;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class NotNullConstraintManager{
	protected Logger logger = Logger.getLogger(getClass());
	
	protected JdbcTemplate jdbcTemplate;

	public NotNullConstraintManager(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public boolean deactivateConstraint(DatabaseColumnInfo databaseColumnInfo) {
		if(databaseColumnInfo.isNullable()){
			return false;
		}
		
		String dropConstraint = getNotNullConstraint(databaseColumnInfo).createDeactivateSql(databaseColumnInfo);
		
		logger.debug(dropConstraint);
		jdbcTemplate.update(dropConstraint);
		return true;
	}

	public boolean activateConstraints(DatabaseColumnInfo databaseColumnInfo) {
		if(databaseColumnInfo.isNullable()){
			return false;
		}
		
		String activateConstraint = getNotNullConstraint(databaseColumnInfo).createActivateSql(databaseColumnInfo);
		
		logger.debug(activateConstraint);
		jdbcTemplate.update(activateConstraint);
		return true;
	
	}


	public NotNullConstraint getNotNullConstraint(DatabaseColumnInfo databaseColumnInfo ){
		switch (databaseColumnInfo.getDatabaseSpecifics()) {
		case MySqlSpecific:
			return new MySqlNotNullConstraint(jdbcTemplate);
		case SybaseSpecific:
			return new SybaseNotNullConstraint();
		case OracleSpecific:
			return new OracleNotNullConstraint();
		case SqlServerSpecific:
			return new SqlServerNotNullConstraint();

		default:
			throw new RuntimeException("Unsupported " + databaseColumnInfo);
		}
	}
	

}
