package org.anon.logic.map;

import org.anon.data.DatabaseColumnInfo;

public class MappingDefault {
	private String defaultValue;
	
	
	public MappingDefault(String defaultValue) {
		super();
		this.defaultValue = defaultValue;
	}



	public String getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	
	public String getDefaultValueSql(DatabaseColumnInfo databaseColumnInfo) {
		return databaseColumnInfo.getQuoteIfNeeded() + getDefaultValue() + databaseColumnInfo.getQuoteIfNeeded();
	}
}
