package org.anon.service.admin;

import java.io.File;
import java.util.Date;

import org.anon.data.DataObject;

public class BackupDir extends DataObject{
	private String dirName;
	private String path;

	public BackupDir(String dirName, String path) {
		super();
		this.dirName = dirName;
		this.path = path;
	}
	
	public String getName(){
		return new File(dirName).getName();
	}
	
	public Date getBackupDate(){
		return new Date(new File(path + dirName).lastModified());
	}
	
	@Override
	public String toString() {
		return getName();
	}


}
