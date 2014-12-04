package org.anon.vendor;

import org.anon.data.DatabaseColumnInfo;

public class MySqlSpecific implements DatabaseSpecifics{

	
	@Override
	public boolean isJavaTypeString(DatabaseColumnInfo databaseColumnInfo) {
		return databaseColumnInfo.getType().toUpperCase().startsWith("VARCHAR") || databaseColumnInfo.getType().equalsIgnoreCase("CHAR");
	}


	@Override
	public boolean isJavaTypeDate(DatabaseColumnInfo databaseColumnInfo) {
		return databaseColumnInfo.getType().equalsIgnoreCase("DATE") || databaseColumnInfo.getType().toUpperCase().startsWith("TIMESTAMP");
	}

	@Override
	public boolean isJavaTypeLong(DatabaseColumnInfo databaseColumnInfo) {
		return databaseColumnInfo.getType().equalsIgnoreCase("NUMBER");
	}

	@Override
	public boolean isJavaTypeDouble(DatabaseColumnInfo databaseColumnInfo) {
		return databaseColumnInfo.getType().equalsIgnoreCase("numeric")|| databaseColumnInfo.getType().equalsIgnoreCase("float");
	}	

}
