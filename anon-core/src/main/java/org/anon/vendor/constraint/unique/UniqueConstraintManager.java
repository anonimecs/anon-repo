package org.anon.vendor.constraint.unique;

import javax.sql.DataSource;

import org.anon.vendor.constraint.ConstraintManager;

public abstract class UniqueConstraintManager <C extends UniqueConstraint> extends ConstraintManager<C>{
	
	public UniqueConstraintManager(DataSource dataSource) {
		super(dataSource);
	}



}
