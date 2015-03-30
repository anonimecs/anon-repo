package org.anon.data;

import org.anon.logic.AnonymisationMethod;
import org.anon.vendor.DatabaseSpecifics;

public class AnonymisedColumnInfo extends DatabaseColumnInfo{

	private AnonymisationMethod anonymisationMethod;
	protected Long id;

	
	public AnonymisedColumnInfo(DatabaseColumnInfo editedColumn) {
		super(editedColumn);
	}

	public AnonymisedColumnInfo(String name, String type, boolean nullable, DatabaseSpecifics databaseSpecifics) {
		super(name, type, nullable, databaseSpecifics);
	}

	public AnonymisationMethod getAnonymisationMethod() {
		return anonymisationMethod;
	}

	public void setAnonymisationMethod(AnonymisationMethod anonymisationMethod) {
		this.anonymisationMethod = anonymisationMethod;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	

}
