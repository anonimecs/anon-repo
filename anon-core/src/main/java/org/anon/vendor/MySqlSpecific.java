package org.anon.vendor;


public class MySqlSpecific extends DatabaseSpecifics{

	public MySqlSpecific() {
		javaTypeStringSet = createSet("VARCHAR", "CHAR");
		javaTypeDateSet = createSet("DATE","DATETIME", "TIMESTAMP", "TIME");
		javaTypeLongSet = createSet("INT", "TINYINT", "SMALLINT", "MEDIUMINT", "BIGINT");
		javaTypeDoubleSet = createSet("DECIMAL","FLOAT", "DOUBLE");
	}
	

}
