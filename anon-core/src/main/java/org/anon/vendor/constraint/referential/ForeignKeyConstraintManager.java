package org.anon.vendor.constraint.referential;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.vendor.constraint.NamedConstraintManager;
import org.apache.log4j.Logger;

public abstract class ForeignKeyConstraintManager <C extends ForeignKeyConstraint> extends NamedConstraintManager<C>{
	protected Logger logger = Logger.getLogger(getClass());
	
	public ForeignKeyConstraintManager(DataSource dataSource) {
		super(dataSource);
	}

	

	@Override
	public List<C> loadConstraints(String tableName, String columnName, String schema){
		List<C> res= loadForeignKeysTo(tableName, columnName, schema);
		res.addAll(loadForeignKeysFrom(tableName, columnName, schema));
		return res;
	}
	abstract public List<C> loadForeignKeysTo(String tableName, String columnName, String schema);
	abstract public List<C> loadForeignKeysFrom(String tableName, String columnName, String schema);



	public List<? extends ForeignKeyConstraint> deactivateConstraints(String tableName, List<DatabaseColumnInfo> columns, String schemaName) {
		List<ForeignKeyConstraint> allConstraints = new ArrayList<>();
		
		for(DatabaseColumnInfo column:columns){
			List<? extends ForeignKeyConstraint> constraints = loadConstraints(tableName, column.getName(),schemaName);
			allConstraints.addAll(constraints);
			
			for(ForeignKeyConstraint constraint:constraints){
				String dropConstraint = constraint.createDeactivateSql();
				logger.debug(dropConstraint);
				try{
					jdbcTemplate.update(dropConstraint);
					constraint.setActive(false);
				} catch (Exception e) {
					handleConstraintDeactivationException(constraint, dropConstraint, e);
				}
				
			}
		}
		
		return allConstraints;

	}




}
