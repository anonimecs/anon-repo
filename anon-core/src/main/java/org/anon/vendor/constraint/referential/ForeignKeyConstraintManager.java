package org.anon.vendor.constraint.referential;

import java.util.List;

import javax.sql.DataSource;

import org.anon.vendor.constraint.ConstraintManager;
import org.apache.log4j.Logger;

public abstract class ForeignKeyConstraintManager <C extends ForeignKeyConstraint> extends ConstraintManager<C>{
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




}
