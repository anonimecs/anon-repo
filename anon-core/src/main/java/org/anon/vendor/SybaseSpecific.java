package org.anon.vendor;


public class SybaseSpecific extends DatabaseSpecifics {

	public SybaseSpecific() {
		javaTypeStringSet = createSet("VARCHAR", "CHAR", "NCHAR", "NVARCHAR", "UNICHAR", "UNIVARCHAR");
		javaTypeDateSet = createSet("DATE", "DATETIME", "TIMESTAMP", "SMALLDATETIME", "TIME");
		javaTypeLongSet = createSet("DECIMAL", "INT", "TINYINT", "BIGINT", "SMALLINT", "UINT");
		javaTypeDoubleSet = createSet("NUMERIC","FLOAT", "REAL");
	}
	

}
