package org.anon.premium.runner;

import java.util.Arrays;
import java.util.List;

import org.anon.AbstractDbConnection;
import org.anon.data.AnonConfig;
import org.anon.exec.BaseExec;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.service.DatabaseConfigService;
import org.anon.service.DatabaseLoaderService;
import org.anon.service.DbConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

// TODO This class is part of the premium package. Move it to a new project (jar)
@Service
public class AnonRunner {
	
	static Logger logger = Logger.getLogger(AnonRunner.class);

	@Autowired
	protected BaseExec baseExec;

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
			logger.info("Starting AnonRunner with params " + Arrays.toString(args));
			Assert.isTrue(args.length > 0, "1 param required");
			
			String databaseConfigGuiName = args[0];
			Assert.isTrue(databaseConfigGuiName != null, "databaseConfogGuiName is null");
			Assert.isTrue(! databaseConfigGuiName.isEmpty(), "databaseConfogGuiName is empty");
			
			// Start Spring
			// AnonRunner assumes standalone database 
			System.setProperty("spring.profiles.active", "enterprise_edition");
			ApplicationContext context = new ClassPathXmlApplicationContext("anonRunnerContext.xml");
			AnonRunner anonRunner = context.getBean(AnonRunner.class);
			
			// run the stuff
			anonRunner.runAll(databaseConfigGuiName);
		
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

		logger.info("Runnig " + anonConfig.getAnonMethods().size() + " anonymisation methods.");

		baseExec.setDataSource(connection.getDataSource());
		baseExec.runAll();
		logger.warn("Anonimisation finished for " + databaseConfigGuiName);
	}

}
