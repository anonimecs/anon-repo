package org.anon.gui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.logic.AnonymisationMethod;

@ManagedBean
@SessionScoped
public class ConfigContext {

	private DatabaseTableInfo editedTable; 
	private DatabaseColumnInfo editedColumn;
	private AnonymisationMethod anonymisationMethod = null;
	private String testValue;
	
	public DatabaseTableInfo getEditedTable() {
		return editedTable;
	}
	public void setEditedTable(DatabaseTableInfo editedTable) {
		this.editedTable = editedTable;
	}
	public DatabaseColumnInfo getEditedColumn() {
		return editedColumn;
	}
	public void setEditedColumn(DatabaseColumnInfo editedColumn) {
		this.editedColumn = editedColumn;
	}
	public AnonymisationMethod getAnonymisationMethod() {
		return anonymisationMethod;
	}
	public void setAnonymisationMethod(AnonymisationMethod anonymisationMethod) {
		this.anonymisationMethod = anonymisationMethod;
	}
	public String getTestValue() {
		return testValue;
	}
	public void setTestValue(String testValue) {
		this.testValue = testValue;
	}

	
	
}
