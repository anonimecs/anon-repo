package org.anon.vendor.constraint.unique;

import java.util.List;

import javax.sql.DataSource;

import org.anon.vendor.constraint.ConstraintManager;

public abstract class UniqueConstraintManager <C extends UniqueConstraint> extends ConstraintManager<C>{
	
	public UniqueConstraintManager(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public List<C> loadConstraints(String tableName, String columnName, String schema){
		List<C> res= loadUniques(tableName, columnName, schema);
		res.addAll(loadPrimaryKeys(tableName, columnName, schema));
		return res;
	}
	abstract public List<C> loadUniques(String tableName, String columnName, String schema);
	abstract public List<C> loadPrimaryKeys(String tableName, String columnName, String schema);




}
