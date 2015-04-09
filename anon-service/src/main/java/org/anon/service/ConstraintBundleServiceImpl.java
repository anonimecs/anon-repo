package org.anon.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.logic.AnonymisationMethod;
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
	
	private Map<Long, Boolean> pkConstraintMap;
	private Map<Long, Boolean> fkConstraintMap;
	private Map<Long, Boolean> uniqueConstraintMap;
	private Map<Long, Boolean> nullConstraintMap;
	
	@PostConstruct
	public void init() {
		pkConstraintMap = new HashMap<Long, Boolean>();
		fkConstraintMap = new HashMap<Long, Boolean>();
		uniqueConstraintMap = new HashMap<Long, Boolean>();
		nullConstraintMap = new HashMap<Long, Boolean>();
		
	}
	
	@Override
	public ColumnConstraintBundle loadColumnConstraintBundle(AnonymisedColumnInfo column) {
		
		AnonymisationMethod anonymisationMethod = column.getAnonymisationMethod();
		DataSource dataSource = dbConnectionFactory.getConnection().getDataSource();
		DatabaseSpecifics databaseSpecifics = column.getDatabaseSpecifics();
		
		return constraintBundleFactory.createConstraintBundle(databaseSpecifics, column, anonymisationMethod, dataSource);
	}
	
	@Override
	public boolean isColumnPK(AnonymisedColumnInfo column) {
		
		if(pkConstraintMap.containsKey(column.getId())) {
			return pkConstraintMap.get(column.getId());
		}
		
		ColumnConstraintBundle bundle = loadColumnConstraintBundle(column);
		boolean result = false;
		
		if(bundle.getPrimaryKey() != null) {
			result = true;
		}
		pkConstraintMap.put(column.getId(), result);
		return result;
	}

	@Override
	public boolean isColumnFK(AnonymisedColumnInfo column) {
		
		if(fkConstraintMap.containsKey(column.getId())) {
			return fkConstraintMap.get(column.getId());
		}
		
		ColumnConstraintBundle bundle = loadColumnConstraintBundle(column);
		boolean result = false;
		
		if(!bundle.getForeignKeyConstraintsFrom().isEmpty() 
				|| !bundle.getForeignKeyConstraintsTo().isEmpty()) {
			result = true;
		}
		fkConstraintMap.put(column.getId(), result);
		return result;
	}

	@Override
	public boolean isColumnUnique(AnonymisedColumnInfo column) {
		
		if(uniqueConstraintMap.containsKey(column.getId())) {
			return uniqueConstraintMap.get(column.getId());
		}
		
		ColumnConstraintBundle bundle = loadColumnConstraintBundle(column);
		boolean result = false;
		
		if(!bundle.getUniqueConstraints().isEmpty()) {
			result = true;
		}
		uniqueConstraintMap.put(column.getId(), result);
		return result;
	}

	@Override
	public boolean isColumnNullConstraint(AnonymisedColumnInfo column) {
		
		if(nullConstraintMap.containsKey(column.getId())) {
			return nullConstraintMap.get(column.getId());
		}
		
		ColumnConstraintBundle bundle = loadColumnConstraintBundle(column);
		boolean result = false;
		
		if(bundle.getNotNullConstraint() != null) {
			result = true;
		}
		nullConstraintMap.put(column.getId(), result);
		return result;
	} 
}
