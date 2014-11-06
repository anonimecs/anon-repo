package org.anon.data;

import org.anon.logic.AnonymisationMethod;
import org.anon.vendor.DatabaseSpecifics;

public class AnonymisedColumnInfo extends DatabaseColumnInfo{

	private AnonymisationMethod anonymisationMethod;
	
	public AnonymisedColumnInfo(DatabaseColumnInfo editedColumn) {
		super(editedColumn);
	}

	public AnonymisedColumnInfo(String name, String type, DatabaseSpecifics databaseSpecifics) {
		super(name, type, databaseSpecifics);
	}

	public AnonymisationMethod getAnonymisationMethod() {
		return anonymisationMethod;
	}

	public void setAnonymisationMethod(AnonymisationMethod anonymisationMethod) {
		this.anonymisationMethod = anonymisationMethod;
	}


	

}
