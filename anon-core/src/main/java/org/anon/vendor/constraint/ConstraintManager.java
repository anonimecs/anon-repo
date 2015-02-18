package org.anon.vendor.constraint;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class ConstraintManager {
	protected Logger logger = Logger.getLogger(getClass());

	protected JdbcTemplate jdbcTemplate;
	
	public ConstraintManager(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<? extends Constraint> deactivateConstraints(AnonymisedColumnInfo anonymisedColumnInfo) {
		List<? extends Constraint> referentialConstraints = loadConstraints(anonymisedColumnInfo.getTable().getName());
		
		for(Constraint constraint:referentialConstraints){
			String dropConstraint = constraint.createDeactivateSql();
			logger.debug(dropConstraint);
			jdbcTemplate.update(dropConstraint);
			constraint.setActive(false);
		}
		
		return referentialConstraints;
	}

	public void activateConstraints(AnonymisedColumnInfo anonymisedColumnInfo, List<? extends Constraint> referentialConstraints) {
		
		for(Constraint constraint:referentialConstraints){
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
	

	abstract protected List<? extends Constraint> loadConstraints(String tableName);


}
