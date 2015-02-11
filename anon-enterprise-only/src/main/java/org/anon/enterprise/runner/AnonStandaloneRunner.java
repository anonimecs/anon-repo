package org.anon.enterprise.runner;

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
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The standalone runner can be used, when the server (anon-gui) is not started and, only the (derby) database server is available.
 * It must be started with an existing configuration in the database (select * from DatabaseConfig where guiName=param)     
 * 
 */
public class AnonStandaloneRunner extends Runner{
	
	static Logger logger = Logger.getLogger(AnonStandaloneRunner.class);

	@Autowired
	protected ExecFactory execFactory;

	@Autowired
	protected DbConnectionFactory dbConnectionFactory; 
	
	@Autowired
	protected DatabaseConfigService databaseConfigService;

	@Autowired
	protected DatabaseLoaderService databaseLoaderService;
	
	@Autowired
	protected AnonConfig anonConfig;
	
	public static void main(String[] args) {
		try{
			String databaseConfigGuiName = checkParams(args);
			
			// Start Spring
			// AnonRunner assumes standalone database 
			System.setProperty("spring.profiles.active", "enterprise_edition");
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("AnonStandaloneRunner.xml");
			AnonStandaloneRunner anonRunner = context.getBean(AnonStandaloneRunner.class);
			
			// run the stuff
			anonRunner.runAll(databaseConfigGuiName);
		
			context.close();
		}catch(Exception e){
			logger.error("AnonRunner failed with error " + e.getMessage(), e);
		}
		
		
	}


	private void runAll(String databaseConfigGuiName) {

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
		
		BaseExec baseExec = execFactory.createExec(connection.getDatabaseSpecifics(), "AnonStandaloneRunner");
		baseExec.setDataSource(connection.getDataSource());
		baseExec.runAll();
		logger.warn("Anonimisation finished for " + databaseConfigGuiName);
	}

}
