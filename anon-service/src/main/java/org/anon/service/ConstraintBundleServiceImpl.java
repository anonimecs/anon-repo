package org.anon.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.constraint.ColumnConstraintBundle;
import org.anon.vendor.constraint.ConstraintBundleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="constraintBundleService")
public class ConstraintBundleServiceImpl implements ConstraintBundleService {

	@Autowired
	private ConstraintBundleFactory constraintBundleFactory;
	
	@Autowired
	private	DbConnectionFactory dbConnectionFactory;
	
	private Map<DatabaseColumnInfo, Boolean> pkConstraintMap;
	private Map<DatabaseColumnInfo, Boolean> fkConstraintMap;
	private Map<DatabaseColumnInfo, Boolean> uniqueConstraintMap;
	private Map<DatabaseColumnInfo, Boolean> nullConstraintMap;
	
	@PostConstruct
	public void init() {
		pkConstraintMap = new HashMap<DatabaseColumnInfo, Boolean>();
		fkConstraintMap = new HashMap<DatabaseColumnInfo, Boolean>();
		uniqueConstraintMap = new HashMap<DatabaseColumnInfo, Boolean>();
		nullConstraintMap = new HashMap<DatabaseColumnInfo, Boolean>();
		
	}
	
	@Override
	public ColumnConstraintBundle loadColumnConstraintBundle(DatabaseColumnInfo column) {
		
		DataSource dataSource = dbConnectionFactory.getConnection().getDataSource();
		DatabaseSpecifics databaseSpecifics = column.getDatabaseSpecifics();
		
		return constraintBundleFactory.createConstraintBundle(databaseSpecifics, column, dataSource);
	}
	
	@Override
	public boolean isColumnPK(DatabaseColumnInfo column) {
		
		if(pkConstraintMap.containsKey(column)) {
			return pkConstraintMap.get(column);
		}
		
		ColumnConstraintBundle bundle = loadColumnConstraintBundle(column);
		boolean result = false;
		
		if(bundle.getPrimaryKey() != null) {
			result = true;
		}
		pkConstraintMap.put(column, result);
		return result;
	}

	@Override
	public boolean isColumnFK(DatabaseColumnInfo column) {
		
		if(fkConstraintMap.containsKey(column)) {
			return fkConstraintMap.get(column);
		}
		
		ColumnConstraintBundle bundle = loadColumnConstraintBundle(column);
		boolean result = false;
		
		if(!bundle.getForeignKeyConstraintsFrom().isEmpty() 
				|| !bundle.getForeignKeyConstraintsTo().isEmpty()) {
			result = true;
		}
		fkConstraintMap.put(column, result);
		return result;
	}

	@Override
	public boolean isColumnUnique(DatabaseColumnInfo column) {
		
		if(uniqueConstraintMap.containsKey(column)) {
			return uniqueConstraintMap.get(column);
		}
		
		ColumnConstraintBundle bundle = loadColumnConstraintBundle(column);
		boolean result = false;
		
		if(!bundle.getUniqueConstraints().isEmpty()) {
			result = true;
		}
		uniqueConstraintMap.put(column, result);
		return result;
	}

	@Override
	public boolean isColumnNullConstraint(DatabaseColumnInfo column) {
		
		if(nullConstraintMap.containsKey(column)) {
			return nullConstraintMap.get(column);
		}
		
		ColumnConstraintBundle bundle = loadColumnConstraintBundle(column);
		boolean result = false;
		
		if(bundle.getNotNullConstraint() != null) {
			result = true;
		}
		nullConstraintMap.put(column, result);
		return result;
	} 
}
