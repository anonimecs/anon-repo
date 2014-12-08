package org.anon.vendor;


public class OracleSpecific extends DatabaseSpecifics{

	
	public OracleSpecific() {
		javaTypeStringSet = createSet("VARCHAR", "VARCHAR2", "CHAR", "NCHAR", "NVARCHAR2");
		javaTypeDateSet = createSet("DATE", "TIMESTAMP");
		javaTypeLongSet = createSet("NUMBER", "LONG");
		javaTypeDoubleSet = createSet("FLOAT", "NUMERIC","BINARY_FLOAT","BINARY_DOUBLE");
	}

}
