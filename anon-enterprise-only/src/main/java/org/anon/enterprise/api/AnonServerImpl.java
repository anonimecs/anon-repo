package org.anon.enterprise.api;

import java.util.List;

import org.anon.AbstractDbConnection;
import org.anon.data.AnonConfig;
import org.anon.exec.BaseExec;
import org.anon.exec.ExecFactory;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.service.DatabaseConfigService;
import org.anon.service.DatabaseLoaderService;
import org.anon.service.DbConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class AnonServerImpl implements AnonServer {

	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	protected ExecFactory execFactory ;

	@Autowired
	protected DbConnectionFactory dbConnectionFactory; 
	
	@Autowired
	protected DatabaseConfigService databaseConfigService;

	@Autowired
	protected DatabaseLoaderService databaseLoaderService;
	
	@Autowired
	protected AnonConfig anonConfig;

	
	@Override
	public void runAll(String databaseConfigGuiName) {

		logger.info("Loading configuration " + databaseConfigGuiName);

		
		DatabaseConfig databaseConfig = databaseConfigService.loadConnectionConfig(databaseConfigGuiName);
		if(databaseConfig == null){
			throw new RuntimeException("Configuration with gui name " + databaseConfigGuiName + " can not be found");
		}
		
		dbConnectionFactory.setDatabaseConfig(databaseConfig);
		

		AbstractDbConnection connection = dbConnectionFactory.getConnection();
		databaseLoaderService.loadTableListFromTargetDb(connection.getDefaultSchema());
		List<String> loadErrors = databaseLoaderService.getLoadErrors();
		if(loadErrors != null && !loadErrors.isEmpty()){
			throw new RuntimeException("Configuration load errors occured:" + loadErrors);
		}

		logger.info("Running " + anonConfig.getAnonMethods().size() + " anonymisation methods.");

		databaseLoaderService.loadExecConfig();

		BaseExec baseExec = execFactory.createExec(connection.getDatabaseSpecifics(), "AnonServer");
		baseExec.setDataSource(connection.getDataSource());
		baseExec.runAll();
		logger.warn("Anonimisation finished for " + databaseConfigGuiName);
	}


	

}
