package org.anon.gui;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.service.admin.BackupDir;
import org.anon.service.admin.BackupService;

@ManagedBean
@ViewScoped
public class BackupBacking extends BackingBase{
	
	@ManagedProperty(value="#{backupService}")
	private BackupService backupService;
	
	private List<BackupDir> backupFileList; 
	
	public void onMenuClicked(){
		try{
			backupFileList = backupService.listBackups();
			
		} catch (Exception e) {
			logError(e.getMessage(), e);
			showErrorInGui("Backup erro: " + e.getMessage());
		} 
		redirectPageTo(NavigationCaseEnum.BACKUP);
		
	}
	
	public void createBackup(){
		try {
			String dirName = backupService.createBackup();
			showInfoInGui("Backup " + dirName +"  created");
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