package org.anon.service;

import org.anon.data.DatabaseColumnInfo;
import org.anon.vendor.constraint.ColumnConstraintBundle;

public interface ConstraintBundleService {
	
	public void init();
	
	public boolean isColumnPK(DatabaseColumnInfo column);
	
	public boolean isColumnFK(DatabaseColumnInfo column);
	
	public boolean isColumnUnique(DatabaseColumnInfo column);
	
	public boolean isColumnNullConstraint(DatabaseColumnInfo column);

	public ColumnConstraintBundle loadColumnConstraintBundle(DatabaseColumnInfo column);

}
