package org.anon.vendor.constraint.notnull;

import org.anon.data.DatabaseColumnInfo;
import org.anon.vendor.constraint.Constraint;




public abstract class NotNullConstraint extends Constraint{

	abstract public String createActivateSql(DatabaseColumnInfo databaseColumnInfo );
	abstract public String createDeactivateSql(DatabaseColumnInfo databaseColumnInfo);

	@Override
	public String toString() {
		return "Not Null Constraint";
	}
	
}
