package org.anon.service;

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
	protected ConstraintBundleFactory constraintBundleFactory;
	
	@Autowired
	private	DbConnectionFactory dbConnectionFactory;
	
	@Override
	public ColumnConstraintBundle loadColumnConstraintBundle(DatabaseSpecifics databaseSpecifics, AnonymisedColumnInfo column,
															AnonymisationMethod anonymisationMethod, DataSource dataSource) {
		ColumnConstraintBundle columnConstraintBundle = constraintBundleFactory.createConstraintBundle
				(databaseSpecifics, column, anonymisationMethod, dataSource);
		
		return columnConstraintBundle;
	}
	
	public ColumnConstraintBundle loadColumnConstraintBundle(AnonymisedColumnInfo column) {
		
		AnonymisationMethod anonymisationMethod = column.getAnonymisationMethod();
		DataSource dataSource = dbConnectionFactory.getConnection().getDataSource();
		DatabaseSpecifics databaseSpecifics = column.getDatabaseSpecifics();
		
		return loadColumnConstraintBundle(databaseSpecifics, column, anonymisationMethod, dataSource);
	}
	
	
	public void setConstraintBundleFactory(ConstraintBundleFactory constraintBundleFactory) {
		this.constraintBundleFactory = constraintBundleFactory;
	}

}
