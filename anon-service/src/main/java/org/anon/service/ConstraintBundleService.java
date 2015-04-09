package org.anon.service;

import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.constraint.ColumnConstraintBundle;

public interface ConstraintBundleService {

	public ColumnConstraintBundle loadColumnConstraintBundle(AnonymisedColumnInfo column);
	
	public ColumnConstraintBundle loadColumnConstraintBundle(DatabaseSpecifics databaseSpecifics, 
																AnonymisedColumnInfo column,
																AnonymisationMethod anonymisationMethod,
																DataSource dataSource);
}
