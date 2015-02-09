package org.anon.service.admin;

import java.io.File;

import org.anon.data.DataObject;

public class BackupDir extends DataObject{
	private String dirName;

	public BackupDir(String dirName) {
		super();
		this.dirName = dirName;
	}
	
	public String getName(){
		return new File(dirName).getName();
	}
	
	@Override
	public String toString() {
		return getName();
	}


}
