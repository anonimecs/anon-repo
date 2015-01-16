package org.anon.exec.constraint;

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
		List<? extends Constraint> referentialConstraints = loadConstraints(anonymisedColumnInfo);
		
		for(Constraint constraint:referentialConstraints){
			String dropConstraint = createDeactivateSql(constraint);
			logger.debug(dropConstraint);
			jdbcTemplate.update(dropConstraint);
			constraint.setActive(false);
		}
		
		return referentialConstraints;
	}

	public void activateConstraints(AnonymisedColumnInfo anonymisedColumnInfo, List<? extends Constraint> referentialConstraints) {
		
		for(Constraint constraint:referentialConstraints){
			if(!constraint.isActive()){
				String createConstraint = createActivateSql(constraint);
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
	

	abstract protected List<? extends Constraint> loadConstraints(AnonymisedColumnInfo anonymisedColumnInfo);
	abstract protected String createDeactivateSql(Constraint constraint);
	abstract protected String createActivateSql(Constraint constraint);

}
