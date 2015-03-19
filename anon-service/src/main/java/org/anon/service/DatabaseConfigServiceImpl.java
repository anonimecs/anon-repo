package org.anon.service;

import java.util.List;

import org.anon.license.LicenseManager;
import org.anon.persistence.dao.DatabaseConfigDao;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseConnection;
import org.anon.persistence.data.SecurityUser;
import org.anon.service.admin.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DatabaseConfigServiceImpl implements DatabaseConfigService {

	private static final Logger logger = Logger.getLogger(DatabaseConfigServiceImpl.class);
	
	@Autowired
	private DatabaseConfigDao configDao;

	@Autowired
	private DbConnectionValidatorService connectionValidator;
	
	@Autowired(required=false)
	private UserService userService;
	
	@Autowired
	private LicenseManager licenseManager;
	
	@Override
	public List<DatabaseConfig> loadDatabaseConfigsForUser() {
		
		return configDao.findDatabaseConfigForUser(userService.getUsername());
	}

	@Override
	public List<DatabaseConnection> loadDatabaseConnectionsForUser() {
		return configDao.findDatabaseConnectionsForUser(userService.loadSecurityUser());
	}

	@Override
	public DatabaseConfig loadDatabaseConfig(String configName) {
		return configDao.loadDatabaseConfig(configName, userService.loadSecurityUser());
	}
	
	@Override
	public DatabaseConnection loadDatabaseConnection(String guiName) {
		return configDao.loadDatabaseConnection(guiName, userService.loadSecurityUser());
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteDatabaseConfig(DatabaseConfig config)  {
		
		
		configDao.removeDatabaseConfig(config);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteDatabaseConnection(DatabaseConnection databaseConnection) {
		configDao.removeDatabaseConnection(databaseConnection);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteDatabaseConfig(String configGuiName) {
		configDao.removeDatabaseConfig(configGuiName, userService.loadSecurityUser());
	}

	@Override
	@Transactional(readOnly = false)
	public void addDatabaseConfig(DatabaseConfig config) {
		SecurityUser securityUser = userService.loadSecurityUser();
		config.setSecurityUser(securityUser);
		configDao.addDatabaseConfig(config);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void addDatabaseConnection(DatabaseConnection databaseConnection) throws ServiceException {
		validateDatabaseConnection(databaseConnection);

		SecurityUser securityUser = userService.loadSecurityUser();
		databaseConnection.setSecurityUser(securityUser);
	
		configDao.addDatabaseConnection(databaseConnection);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void updateDatabaseConfig(DatabaseConfig config) {
		configDao.updateDatabaseConfig(config);
	}
	
	
	

	@Override
	public void validateDatabaseConnection(DatabaseConnection databaseConnection)  throws ServiceException{
		
		connectionValidator.connectionValid(databaseConnection);
		
	}

	@Override
	public void updateDatabaseConnection(DatabaseConnection selectedDatabaseConnection) {
		configDao.updateDatabaseConnection(selectedDatabaseConnection);
		
	}
}

