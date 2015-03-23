package org.anon.vendor.constraint;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class ConstraintManager <C extends Constraint>{
	protected Logger logger = Logger.getLogger(getClass());

	protected JdbcTemplate jdbcTemplate;
	
	public ConstraintManager(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<C> deactivateConstraints(String tableName, String columnName, String schemaName) {
		List<C> constraints = loadConstraints(tableName, columnName,schemaName);
		
		for(C constraint:constraints){
			String dropConstraint = constraint.createDeactivateSql();
			logger.debug(dropConstraint);
			jdbcTemplate.update(dropConstraint);
			constraint.setActive(false);
		}
		
		return constraints;
	}

	public void activateConstraints(List<C> constraints) {
		
		for(C constraint:constraints){
			if(!constraint.isActive()){
				String createConstraint = constraint.createActivateSql();
				logger.debug(createConstraint);
				try {
					jdbcTemplate.update(createConstraint);
					constraint.setActive(true);
				} catch (Exception e) {
					String message = "Failed to activate the referential integrity constraint: '" + createConstraint +"'";
					logger.warn(message);
					constraint.setMessage(message);
				}
			}
		}
	}
	
	public abstract List<C> loadConstraints(String tableName, String columnName, String schemaName);

	
}
