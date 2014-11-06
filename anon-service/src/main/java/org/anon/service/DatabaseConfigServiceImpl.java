package org.anon.service;

import static org.anon.AnonStatic.ORACLE;
import static org.anon.AnonStatic.ORACLE_JDBC_PREFIX;
import static org.anon.AnonStatic.SYBASE;
import static org.anon.AnonStatic.SYBASE_JDBC_PREFIX;

import java.util.List;

import org.anon.persistence.dao.DatabaseConfigDao;
import org.anon.persistence.data.DatabaseConfig;
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
	
	@Override
	public String getUrlPrefix(DatabaseConfig config){
		if(ORACLE.equals(config.getVendor())) {
			return ORACLE_JDBC_PREFIX;
		}
		else if (SYBASE.equals(config.getVendor())) {
			return SYBASE_JDBC_PREFIX;
		}
		else return "";
	}
	
	@Override
	public List<DatabaseConfig> loadConnectionConfigs() {
		return configDao.findAll();
	}

	@Override
	public DatabaseConfig loadConnectionConfig(String guiName) {
		return configDao.loadConnectionConfig(guiName);
	}
	
	@Override
	@Transactional(readOnly = false)
	public ServiceResult deleteDatabaseConfig(DatabaseConfig config) {
		
		ServiceResult result = new ServiceResult();
		
		try {
			configDao.removeDatabaseConfig(config);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			result.addErrorMessage("Configuration not deleted", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage());
		}
		return result;
	}

	@Override
	@Transactional(readOnly = false)
	public ServiceResult addDatabaseConfig(DatabaseConfig config) {
		
		ServiceResult result = connectionValidator.connectionValid(config);
		
		if(!result.isFailed()) {
			try {
				configDao.addDatabaseConfig(config);
			} catch (Exception e) {
				logger.warn(e.getMessage());
				result.addErrorMessage("Configuration not added", 
						e.getCause() != null ?  e.getCause().getMessage() : e.getMessage());
			}
		}
		return result;
	}

	public void setConfigDao(DatabaseConfigDao configDao) {
		this.configDao = configDao;
	}
}
