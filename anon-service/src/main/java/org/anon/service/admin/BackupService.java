package org.anon.service.admin;

import java.util.List;

public interface BackupService {

	List<BackupDir> listBackups();

	String getDerbyDirPath();

	String createBackup();

}