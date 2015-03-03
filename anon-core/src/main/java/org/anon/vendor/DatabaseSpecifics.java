package org.anon.vendor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.anon.data.DatabaseColumnInfo;

public enum DatabaseSpecifics {

	MySqlSpecific(
	    createSet("VARCHAR", "CHAR", "TEXT"),
		createSet("DATE","DATETIME", "TIMESTAMP", "TIME"),
		createSet("INT", "TINYINT", "SMALLINT", "MEDIUMINT", "BIGINT"),
		createSet("DECIMAL","FLOAT", "DOUBLE", "NUMERIC")
		) {
			@Override
			public String getUseSchemaSql(String schema) {
				return "use "  + schema;
			}

			@Override
			public String getFromDual(){
				return "";
			}		
	},
	
	OracleSpecific(
		createSet("VARCHAR", "VARCHAR2", "CHAR", "NCHAR", "NVARCHAR2")
		,createSet("DATE", "TIMESTAMP")
		,createSet("NUMBER", "LONG")
		,createSet("FLOAT", "NUMERIC","BINARY_FLOAT","BINARY_DOUBLE")
		){
		@Override
		public String getFromDual() {
			return " from dual ";
		}

		@Override
		public String getUseSchemaSql(String schema) {
			return "ALTER SESSION SET CURRENT_SCHEMA = " + schema;
		}
	},
		
	SqlServerSpecific(
		createSet("VARCHAR", "CHAR", "NCHAR", "NVARCHAR")
		,createSet("DATE", "DATETIME", "DATETIME2", "TIME", "SMALLDATETIME")
		,createSet("DECIMAL", "INT", "TINYINT", "BIGINT", "SMALLINT", "UINT")
		,createSet("NUMERIC","FLOAT", "REAL")
		) {
			@Override
			public String getUseSchemaSql(String schema) {
				return "use "  + schema;
			}
			@Override
			public String getFromDual(){
				return "";
			}		
		},

	SybaseSpecific(
		createSet("VARCHAR", "CHAR", "NCHAR", "NVARCHAR", "UNICHAR", "UNIVARCHAR")
		,createSet("DATE", "DATETIME", "TIMESTAMP", "SMALLDATETIME", "TIME")
		,createSet("DECIMAL", "INT", "TINYINT", "BIGINT", "SMALLINT", "UINT")
		,createSet("NUMERIC","FLOAT", "REAL")
		) {
			@Override
			public String getUseSchemaSql(String schema) {
				return "use "  + schema;
			}
			@Override
			public String getFromDual(){
				return "";
			}		
		}
		
	;
	
	
	DatabaseSpecifics(Set<String> javaTypeStringSet, Set<String> javaTypeDateSet, 
			Set<String> javaTypeLongSet,Set<String> javaTypeDoubleSet) {
		this.javaTypeStringSet = javaTypeStringSet;
		this.javaTypeDateSet = javaTypeDateSet;
		this.javaTypeLongSet = javaTypeLongSet;
		this.javaTypeDoubleSet = javaTypeDoubleSet;
	}

	/**
	 * You will need to initialize all these in a subclass
	 */
	protected Set<String> javaTypeStringSet;
	protected Set<String> javaTypeDateSet;
	protected Set<String> javaTypeLongSet;
	protected Set<String> javaTypeDoubleSet;
	
	public boolean isJavaTypeString(DatabaseColumnInfo databaseColumnInfo){
		return test(javaTypeStringSet, databaseColumnInfo);
	}

	public boolean isJavaTypeDate(DatabaseColumnInfo databaseColumnInfo){
		return test(javaTypeDateSet, databaseColumnInfo);
		
	}

	public boolean isJavaTypeLong(DatabaseColumnInfo databaseColumnInfo){
		return test(javaTypeLongSet, databaseColumnInfo);
		
	}

	public boolean isJavaTypeDouble(DatabaseColumnInfo databaseColumnInfo){
		return test(javaTypeDoubleSet, databaseColumnInfo);
	}

	public boolean isSupportedDataType(DatabaseColumnInfo databaseColumnInfo){
		return 
				isJavaTypeString(databaseColumnInfo) ||
				isJavaTypeDate(databaseColumnInfo) ||
				isJavaTypeLong(databaseColumnInfo) ||
				isJavaTypeDouble(databaseColumnInfo);
		
	}
	
	@SuppressWarnings("unchecked")
	protected static Set<String> createSet(String ... values){
		if(values == null || values.length == 0){
			return Collections.EMPTY_SET; 
		}
		return new HashSet<>(Arrays.asList(values));
	}
	
	protected boolean test(Set<String> set, DatabaseColumnInfo databaseColumnInfo) {
		String upperCaseType = databaseColumnInfo.getType().toUpperCase();
		if (set.contains(upperCaseType)){
			return true;
		}
		for(String value:set){
			if(upperCaseType.startsWith(value)){
				return true;
			}
		}
		return false;
	}

	public abstract String getUseSchemaSql(String schema);

	public abstract String getFromDual();
}
