package org.anon.service.admin;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BackupService {
	private Logger logger = Logger.getLogger(getClass());

	private static String EXTENSION = ".anonbackup";

	@Value("${derby.dir}")
	private String derbyDir;
	
	
	@Autowired
	DataSource derbyDataSource;
	
	public List<BackupDir> listBackups(){
		
		logger.debug("loading backup list from " + derbyDir);
		
		List<BackupDir> result = new LinkedList<>();
		File dir = new File(derbyDir);
		String[] dirList = dir.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(EXTENSION);
			}
		});
		
		for (String dirName : dirList) {
	    	logger.debug(dirName);
	    	result.add(new BackupDir(dirName));
		}		
		
		return result;
	}


	public String createBackup() {
		String dirName = createFileName(); 

		logger.info("Backing up the database to " + dirName);
		
		JdbcTemplate jdbcTemplate =new JdbcTemplate(derbyDataSource);
		String sql = "CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE('"+dirName+"')";
		jdbcTemplate.execute(sql);
		
		return dirName; 
	}


	protected String createFileName() {
		String datetime = new SimpleDateFormat("yyMMdd_HHmm").format(new Date());
		return derbyDir + "/backup_" + datetime + EXTENSION;
	}
	
	void setDerbyDir(String derbyDir) {
		this.derbyDir = derbyDir;
	}
}