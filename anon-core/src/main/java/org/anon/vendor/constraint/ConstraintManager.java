package org.anon.vendor.constraint;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class ConstraintManager <C extends Constraint>{
	protected Logger logger = Logger.getLogger(getClass());

	protected JdbcTemplate jdbcTemplate;
	
	public ConstraintManager(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public abstract  List<C> deactivateConstraints(DatabaseColumnInfo databaseColumnInfo);
	public abstract void activateConstraints(DatabaseColumnInfo databaseColumnInfo, List<C> deactivatedConstraints);

	protected void handleConstraintDeactivationException(Constraint constraint, String dropConstraint, Exception e) {
		String message = "Failed to deactivate the constraint: '" + dropConstraint +"'";
		logger.warn(message);
		logger.error(e.getMessage());
		constraint.setMessage(message);
	}
	
	protected void handleConstraintActivationException(Constraint constraint, String createConstraint, Exception e) {
		String message = "Failed to activate the constraint: '" + createConstraint +"'";
		logger.warn(message);
		logger.error(e.getMessage());
		constraint.setMessage(message);
	}


	
}
