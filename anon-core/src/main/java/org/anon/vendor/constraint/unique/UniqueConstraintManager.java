package org.anon.vendor.constraint.unique;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.anon.vendor.constraint.NamedConstraintManager;

public abstract class UniqueConstraintManager <C extends UniqueConstraint> extends NamedConstraintManager<C>{
	
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


	protected List<C> filterColumnConstraints(String columnName, List<C> tableConstraints){
		List<C> columnConstraints = new ArrayList<>();
		for(C constraint:tableConstraints){
			if(constraint.containsColumn(columnName)){
				columnConstraints.add(constraint);
			}
		}
		return columnConstraints;
	}
	

}
