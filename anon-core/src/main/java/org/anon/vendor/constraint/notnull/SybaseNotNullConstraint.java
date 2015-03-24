package org.anon.vendor.constraint.notnull;

import org.anon.data.DatabaseColumnInfo;

public class SybaseNotNullConstraint extends NotNullConstraint{


	@Override
	public String createActivateSql(DatabaseColumnInfo databaseColumnInfo) {
		return "alter table " +databaseColumnInfo.getTable().getName()+ " modify "+ databaseColumnInfo.getName() +" not null";
	}

	@Override
	public String createDeactivateSql(DatabaseColumnInfo databaseColumnInfo) {
		return "alter table " +databaseColumnInfo.getTable().getName()+ " modify "+ databaseColumnInfo.getName() +" null";
	}




}
