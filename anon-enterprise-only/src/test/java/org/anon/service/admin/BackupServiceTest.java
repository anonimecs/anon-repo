package org.anon.service.admin;

import java.util.List;

import org.anon.test.AnonUnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration("classpath:BackupServiceTest.xml")
public class BackupServiceTest extends AbstractJUnit4SpringContextTests  implements AnonUnitTest{

	@Autowired
	BackupServiceImpl backupService;
	
	static int count = 0;
	
	@Before
	public void setup(){
		backupService.setDerbyDir("c:/temp");
	}
	
	@Test
	public void test_1_listBackupFiles() {
		List<BackupDir> listBackupFiles = backupService.listBackups();
		System.out.println(listBackupFiles);
		count = listBackupFiles.size();
	}

	@Test
	public void test_2_saveBackupFile() {
		backupService.createBackup();
	}

	
	@Test
	public void test_3_listBackupFiles() {
		List<BackupDir> listBackupFiles = backupService.listBackups();
		System.out.println(listBackupFiles);
		Assert.assertEquals("no file", count +1 , listBackupFiles.size());
	}
	

}
