package org.anon.data;

import java.util.List;

public class Lookups {

	public static <T extends DatabaseTableInfo> T findTable(String tableName, List<T> tableList) {
		for(T aDatabaseTableInfo:tableList){
			if(tableName.equals(aDatabaseTableInfo.getName())){
				return aDatabaseTableInfo;
			}
		}
		throw new RuntimeException("Table not found " + tableName);
	}
	
	public static <T extends DatabaseColumnInfo> T findColumn(String searchedColumnName, List<T> columns) {
		for(T databaseColumnInfo: columns){
			if(databaseColumnInfo.getName().equals(searchedColumnName)){
				return databaseColumnInfo;
			}
		}
		throw new RuntimeException("Column " + searchedColumnName + " not found in columns " + columns);
	}
	
	public static <T extends DatabaseColumnInfo> T findTableColumn(String searchedColumnName, String searchedTableName, List<T> columns) {
		for(T databaseColumnInfo: columns){
			if(databaseColumnInfo.getName().equals(searchedColumnName) && databaseColumnInfo.getTable().getName().equals(searchedTableName)){
				return databaseColumnInfo;
			}
		}
		throw new RuntimeException("Column " + searchedTableName+"."+searchedColumnName + " not found in columns " + columns);
	}

}
