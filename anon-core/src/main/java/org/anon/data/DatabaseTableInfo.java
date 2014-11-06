package org.anon.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class DatabaseTableInfo extends DataObject{


	private String name;
	private String schema;
	private List<DatabaseColumnInfo> columns = new LinkedList<DatabaseColumnInfo>();
	private int rowCount;

	private List<AnonCandidate> anonCandidateList = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void addColumns(List<DatabaseColumnInfo> someCols) {
		for(DatabaseColumnInfo col:someCols){
			addColumn(col);
		}
	}
	
	public void addColumn(DatabaseColumnInfo databaseColumnInfo){
		columns.add(databaseColumnInfo);
		databaseColumnInfo.setTable(this);
	}
	
	public List<DatabaseColumnInfo> getColumns() {
		return columns;
	}


	public List<AnonymisedColumnInfo> anonymisedColumnsList() {
		List<AnonymisedColumnInfo> res = new ArrayList<AnonymisedColumnInfo>();
		for(DatabaseColumnInfo databaseColumnInfo:columns){
			if(databaseColumnInfo instanceof AnonymisedColumnInfo){
				res.add((AnonymisedColumnInfo)databaseColumnInfo);
			}
		}
		return res;
	}


	public void addAnonymisedColumn(AnonymisedColumnInfo anonymizedColumn) {
		if(anonymizedColumn.getAnonymisationMethod() == null){
			throw new RuntimeException("No method");
		}
		int index = findColumnIndex(anonymizedColumn.getName());
		columns.remove(index);
		columns.add(index, anonymizedColumn);
	}

	public void removeAnonymisedColumn(AnonymisedColumnInfo anonymizedColumn) {
		DatabaseColumnInfo normalCol = new DatabaseColumnInfo(anonymizedColumn);
		int index = findColumnIndex(anonymizedColumn.getName());
		columns.remove(index);
		columns.add(index, normalCol);
		
	}

	private int findColumnIndex(String searchedColumnName) {
		for(int i=0; i<columns.size(); i++){
			if(columns.get(i).getName().equals(searchedColumnName)){
				return i;
			}
		}
		throw new RuntimeException("Column " +searchedColumnName+" not found on table " + getName());
	}


	public DatabaseColumnInfo findColumn(String searchedColumn) {
		for(DatabaseColumnInfo databaseColumnInfo: columns){
			if(databaseColumnInfo.getName().equals(searchedColumn)){
				return databaseColumnInfo;
			}
		}
		throw new RuntimeException("Column " + searchedColumn + " not found in table " + name);
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void addAnonCandidate(AnonCandidate anonCandidate) {
		anonCandidateList.add(anonCandidate);
		
	}
	
	public List<AnonCandidate> getAnonCandidateList() {
		return anonCandidateList;
	}
	
	public List<AnonCandidate>  getActiveAnonCandidateList() {
		List<AnonCandidate> res = new LinkedList<>();
		for(AnonCandidate anonCandidate:anonCandidateList){
			if(anonCandidate.isActive()){
				res.add(anonCandidate);
			}
		}
		return res;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	
}
