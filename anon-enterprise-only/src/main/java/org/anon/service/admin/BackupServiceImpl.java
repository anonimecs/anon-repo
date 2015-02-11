package org.anon.service.admin;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BackupServiceImpl implements BackupService {
	private Logger logger = Logger.getLogger(getClass());

	private static String EXTENSION = ".anonbackup";

	@Value("${derby.dir}")
	private String derbyDirPath;
	
	
	@Autowired
	DataSource derbyDataSource;
	
	@PostConstruct
	public void init(){
		if(derbyDirPath == null || derbyDirPath.isEmpty() ){
			throw new RuntimeException("Incorrect derby.dir parameter " + derbyDirPath);
		}
		derbyDirPath = derbyDirPath.replace("\\", "/");
		if(!derbyDirPath.endsWith("/")){
			derbyDirPath += "/";
		}
	}
	
	/* (non-Javadoc)
	 * @see org.anon.service.admin.BackupService#listBackups()
	 */
	@Override
	public List<BackupDir> listBackups(){
		
		logger.debug("loading backup list from " + derbyDirPath);
		
		List<BackupDir> result = new LinkedList<>();
		File dir = new File(derbyDirPath);
		String[] dirList = dir.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(EXTENSION);
			}
		});
		
		for (String backupDirName : dirList) {
	    	logger.debug("Backup found: " + backupDirName);
	    	result.add(new BackupDir(backupDirName, derbyDirPath));
		}		
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.anon.service.admin.BackupService#getDerbyDirPath()
	 */
	@Override
	public String getDerbyDirPath() {
		return derbyDirPath;
	}


	/* (non-Javadoc)
	 * @see org.anon.service.admin.BackupService#createBackup()
	 */
	@Override
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
		return derbyDirPath + "/backup_" + datetime + EXTENSION;
	}
	
	void setDerbyDir(String derbyDir) {
		this.derbyDirPath = derbyDir;
	}
}