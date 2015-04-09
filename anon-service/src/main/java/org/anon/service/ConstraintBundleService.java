package org.anon.service;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.vendor.constraint.ColumnConstraintBundle;

public interface ConstraintBundleService {
	
	public boolean isColumnPK(AnonymisedColumnInfo column);
	
	public boolean isColumnFK(AnonymisedColumnInfo column);
	
	public boolean isColumnUnique(AnonymisedColumnInfo column);
	
	public boolean isColumnNullConstraint(AnonymisedColumnInfo column);

	public ColumnConstraintBundle loadColumnConstraintBundle(AnonymisedColumnInfo column);

}
