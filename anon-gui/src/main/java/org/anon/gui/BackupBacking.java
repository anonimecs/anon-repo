package org.anon.gui;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.service.admin.BackupDir;
import org.anon.service.admin.BackupService;

@ManagedBean
@SessionScoped
public class BackupBacking extends BackingBase{
	
	@ManagedProperty(value="#{backupService}")
	private BackupService backupService;
	
	private List<BackupDir> backupFileList; 
	
	public void onMenuClicked(){
		logDebug("loading list of backup files");
		
		failInFreeEdition();
		
		try{
			backupFileList = backupService.listBackups();
			
		} catch (Exception e) {
			logError(e.getMessage(), e);
			showErrorInGui("Backup error: " + e.getMessage());
		} 
		redirectPageTo(NavigationCaseEnum.BACKUP);
		
	}
	

	public void createBackup(){
		logDebug("creating new backup");
		try {
			failInFreeEdition();
			String dirName = backupService.createBackup();
			showInfoInGui("Backup " + dirName +"  created");
			backupFileList = backupService.listBackups();
		} catch (Exception e) {
			logError("Backup failed", e);
			showErrorInGui("Backup failed with error " + e.getMessage());
		}
	}
	
	public void setBackupService(BackupService backupService) {
		this.backupService = backupService;
	}
	
	public List<BackupDir> getBackupFileList() {
		return backupFileList;
	}

}