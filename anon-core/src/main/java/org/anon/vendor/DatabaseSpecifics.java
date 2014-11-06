package org.anon.vendor;

import org.anon.data.DatabaseColumnInfo;

public interface DatabaseSpecifics {

	boolean isJavaTypeString(DatabaseColumnInfo databaseColumnInfo);

	boolean isJavaTypeDate(DatabaseColumnInfo databaseColumnInfo);

	boolean isJavaTypeLong(DatabaseColumnInfo databaseColumnInfo);

	boolean isJavaTypeDouble(DatabaseColumnInfo databaseColumnInfo);

}
