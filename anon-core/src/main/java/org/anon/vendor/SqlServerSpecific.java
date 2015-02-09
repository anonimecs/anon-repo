package org.anon.vendor;


public class SqlServerSpecific extends DatabaseSpecifics {

	public SqlServerSpecific() {
		javaTypeStringSet = createSet("VARCHAR", "CHAR", "NCHAR", "NVARCHAR");
		javaTypeDateSet = createSet("DATE", "DATETIME", "DATETIME2", "TIME", "SMALLDATETIME");
		javaTypeLongSet = createSet("DECIMAL", "INT", "TINYINT", "BIGINT", "SMALLINT", "UINT");
		javaTypeDoubleSet = createSet("NUMERIC","FLOAT", "REAL");
	}
	

}
