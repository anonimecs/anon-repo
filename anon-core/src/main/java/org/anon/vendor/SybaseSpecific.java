package org.anon.vendor;

import org.anon.data.DatabaseColumnInfo;

public class SybaseSpecific implements DatabaseSpecifics {

	@Override
	public boolean isJavaTypeString(DatabaseColumnInfo databaseColumnInfo) {
		return databaseColumnInfo.getType().equals("varchar") || databaseColumnInfo.getType().equals("char");
	}

	@Override
	public boolean isJavaTypeDate(DatabaseColumnInfo databaseColumnInfo) {
		return databaseColumnInfo.getType().equals("datetime") || databaseColumnInfo.getType().equals("timestamp");
	}

	@Override
	public boolean isJavaTypeLong(DatabaseColumnInfo databaseColumnInfo) {
		return databaseColumnInfo.getType().equals("int");
	}

	@Override
	public boolean isJavaTypeDouble(DatabaseColumnInfo databaseColumnInfo) {
		return databaseColumnInfo.getType().equals("numeric")|| databaseColumnInfo.getType().equals("float");
	}
	

}
