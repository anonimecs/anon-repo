package org.anon.vendor.constraint.notnull;

import org.anon.data.DatabaseColumnInfo;

public class SqlServerNotNullConstraint extends NotNullConstraint{

	// TODO: data type precision must be added

	@Override
	public String createActivateSql(DatabaseColumnInfo databaseColumnInfo) {
		return "ALTER TABLE " +databaseColumnInfo.getTable().getName()+ " ALTER COLUMN "+ databaseColumnInfo.getName() +" " +databaseColumnInfo.getType() + " NOT NULL";
	}

	@Override
	public String createDeactivateSql(DatabaseColumnInfo databaseColumnInfo) {
		return "ALTER TABLE " +databaseColumnInfo.getTable().getName()+ " ALTER COLUMN "+ databaseColumnInfo.getName() +" " +databaseColumnInfo.getType() + " NULL";
	}




}
