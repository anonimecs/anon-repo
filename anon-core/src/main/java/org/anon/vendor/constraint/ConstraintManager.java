package org.anon.vendor.constraint;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class ConstraintManager <C extends Constraint>{
	protected Logger logger = Logger.getLogger(getClass());

	protected JdbcTemplate jdbcTemplate;
	
	public ConstraintManager(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<C> deactivateConstraints(AnonymisedColumnInfo anonymisedColumnInfo) {
		List<C> referentialConstraints = loadConstraints(anonymisedColumnInfo.getTable().getName(),
																			anonymisedColumnInfo.getName(),
																			anonymisedColumnInfo.getTable().getSchema());
		
		for(Constraint constraint:referentialConstraints){
			String dropConstraint = constraint.createDeactivateSql();
			logger.debug(dropConstraint);
			jdbcTemplate.update(dropConstraint);
			constraint.setActive(false);
		}
		
		return referentialConstraints;
	}

	public void activateConstraints(AnonymisedColumnInfo anonymisedColumnInfo, List<C> referentialConstraints) {
		
		for(C constraint:referentialConstraints){
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
	

	public List<C> loadConstraints(String tableName, String columnName, String schema){
		List<C> res= loadForeignKeysTo(tableName, columnName, schema);
		res.addAll(loadForeignKeysFrom(tableName, columnName, schema));
		return res;
	}
	abstract protected List<C> loadForeignKeysTo(String tableName, String columnName, String schema);
	abstract protected List<C> loadForeignKeysFrom(String tableName, String columnName, String schema);


}
