package org.anon.data;

import java.util.List;

import org.anon.vendor.DatabaseSpecifics;


public class DatabaseColumnInfo extends DataObject {


	private String name;
	private String type;
	private DatabaseTableInfo table;


	private List<?> exampleValues;
	private DatabaseSpecifics databaseSpecifics;
	
	private AnonCandidate anonCandidate;
	
	public DatabaseColumnInfo(String name, String type, DatabaseSpecifics databaseSpecifics) {
		super();
		this.name = name;
		this.type = type;
		this.databaseSpecifics = databaseSpecifics;
	}
	public DatabaseColumnInfo(DatabaseColumnInfo editedColumn) {
		this(editedColumn.name,editedColumn.type, editedColumn.databaseSpecifics);
		setTable(editedColumn.getTable());
		this.exampleValues = editedColumn.exampleValues;
		
	}

	public DatabaseColumnInfo() {}
	
	public boolean isJavaTypeString() {
		return databaseSpecifics.isJavaTypeString(this);
		
	}
	
	public boolean isJavaTypeDate() {
		return databaseSpecifics.isJavaTypeDate(this);
	}

	public boolean isJavaTypeLong() {
		return databaseSpecifics.isJavaTypeLong(this);
	}

	public boolean isJavaTypeDouble() {
		return databaseSpecifics.isJavaTypeDouble(this);
	}
	
	public String getQuoteIfNeeded(){
		if(isJavaTypeString() || isJavaTypeDate()){
			return "'";
		}
		return "";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<?> getExampleValues() {
		return exampleValues;
	}
	public void setExampleValues(List<?> exampleValues) {
		this.exampleValues = exampleValues;
	}
	public DatabaseTableInfo getTable() {
		return table;
	}
	public void setTable(DatabaseTableInfo table) {
		this.table = table;
	}

	public void setAnonCandidate(AnonCandidate anonCandidate) {
		this.anonCandidate = anonCandidate;
	}
	
	public AnonCandidate getAnonCandidate() {
		return anonCandidate;
	}
	
	public DatabaseSpecifics getDatabaseSpecifics() {
		return databaseSpecifics;
	}
	
	@Override
	public String toString() {
		return getTable() == null ? "N/A." + getName() : getTable().getName() + "." + getName();
	}
	

}
