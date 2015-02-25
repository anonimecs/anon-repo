package org.anon.service;

import java.util.List;

import org.anon.persistence.dao.DatabaseConfigDao;
import org.anon.persistence.data.DatabaseConfig;
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
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<DatabaseConfig> loadConnectionConfigs() {		
		return configDao.findConfigForUser(userService.getUsername());
	}

	@Override
	public DatabaseConfig loadConnectionConfig(String guiName) {
		return configDao.loadConnectionConfig(guiName);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteDatabaseConfig(DatabaseConfig config)  throws ServiceException{
		
		
		try {
			configDao.removeDatabaseConfig(config);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("Configuration not deleted", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteDatabaseConfig(String configGuiName)  throws ServiceException{

		
		try {
			configDao.removeDatabaseConfig(configGuiName);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("Configuration not deleted", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage(), e);
		}

	}

	@Override
	@Transactional(readOnly = false)
	public void addDatabaseConfig(DatabaseConfig config) throws ServiceException{
		
		connectionValidator.connectionValid(config);
		
		try {
			SecurityUser user = userService.loadSecurityUser();
			
			configDao.addDatabaseConfig(config);
			user.getAssignedDatabases().add(config);
			userService.updateUser(user, null);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("Configuration not added", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void updateDatabaseConfig(DatabaseConfig config)  throws ServiceException{
		
		testDatabaseConfig(config);
		
		try {
			configDao.updateDatabaseConifg(config);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("Configuration not updated", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage(), e);
		}
	}

	@Override
	public void testDatabaseConfig(DatabaseConfig config)  throws ServiceException{
		
		connectionValidator.connectionValid(config);

	}

}

