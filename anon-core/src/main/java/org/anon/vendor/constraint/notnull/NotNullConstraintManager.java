package org.anon.vendor.constraint.notnull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.vendor.constraint.ConstraintManager;

public class NotNullConstraintManager extends ConstraintManager<NotNullConstraint>{

	public NotNullConstraintManager(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<NotNullConstraint> deactivateConstraints(DatabaseColumnInfo databaseColumnInfo) {
		NotNullConstraint notNullConstraint = deactivateConstraint(databaseColumnInfo);
		if(notNullConstraint != null){
			return Arrays.asList(notNullConstraint);
		}
		else {
			return Collections.EMPTY_LIST;
		}
	}

	public NotNullConstraint deactivateConstraint(DatabaseColumnInfo databaseColumnInfo) {
		if(databaseColumnInfo.isNullable()){
			return null;
		}
		
		NotNullConstraint notNullConstraint = getNotNullConstraint(databaseColumnInfo);
		String dropConstraint = notNullConstraint.createDeactivateSql(databaseColumnInfo);
		
		logger.debug(dropConstraint);
		try{
			jdbcTemplate.update(dropConstraint);
			notNullConstraint.setActive(false);
		}catch(Exception e){
			handleConstraintDeactivationException(notNullConstraint, dropConstraint, e);
		}
		return notNullConstraint;
	}

	@Override
	public void activateConstraints(DatabaseColumnInfo databaseColumnInfo,List<NotNullConstraint> deactivatedConstraints) {
		if(deactivatedConstraints.size() != 1){
			throw new RuntimeException("There must be exactly one constraint");
		}
		NotNullConstraint notNullConstraint = deactivatedConstraints.get(0);
		try{
			activateConstraints(databaseColumnInfo);
			notNullConstraint.setActive(true);
		} catch (Exception e){
			handleConstraintActivationException(notNullConstraint, "not null constraint", e);
		}
	}

	public void activateConstraints(DatabaseColumnInfo databaseColumnInfo) {
		if(databaseColumnInfo.isNullable()){
			return;
		}
		
		String activateConstraint = getNotNullConstraint(databaseColumnInfo).createActivateSql(databaseColumnInfo);
		
		logger.debug(activateConstraint);
		jdbcTemplate.update(activateConstraint);
	
	}


	public NotNullConstraint getNotNullConstraint(DatabaseColumnInfo databaseColumnInfo ){
		if(databaseColumnInfo.isNullable()){
			return null;
		}
		
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
