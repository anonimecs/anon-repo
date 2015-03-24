package org.anon.vendor.constraint.notnull;

import org.anon.data.DatabaseColumnInfo;




public abstract class NotNullConstraint {

	abstract public String createActivateSql(DatabaseColumnInfo databaseColumnInfo );
	abstract public String createDeactivateSql(DatabaseColumnInfo databaseColumnInfo);


}
