package org.anon.vendor.constraint;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;

public abstract class NamedConstraintManager <C extends NamedConstraint> extends ConstraintManager<C>{
	
	public NamedConstraintManager(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<C> deactivateConstraints(DatabaseColumnInfo databaseColumnInfo) {
		return deactivateConstraints(databaseColumnInfo.getTable().getName(), databaseColumnInfo.getName(), databaseColumnInfo.getTable().getSchema());
	}

	public List<C> deactivateConstraints(String tableName, String columnName, String schemaName) {
		List<C> constraints = loadConstraints(tableName, columnName,schemaName);
		
		for(C constraint:constraints){
			String dropConstraint = constraint.createDeactivateSql();
			logger.debug(dropConstraint);
			try{
				jdbcTemplate.update(dropConstraint);
				constraint.setActive(false);
			} catch (Exception e) {
				handleConstraintDeactivationException(constraint, dropConstraint, e);
			}
			
		}
		
		return constraints;
	}


	@Override
	public void activateConstraints(DatabaseColumnInfo databaseColumnInfo, List<C> deactivatedConstraints) {
		activateConstraints(deactivatedConstraints);
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
					handleConstraintActivationException( constraint, createConstraint, e);
				}
			}
		}
	}

	
	public abstract List<C> loadConstraints(String tableName, String columnName, String schemaName);

	
}
