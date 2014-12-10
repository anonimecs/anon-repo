package org.anon.enterprise.runner;

import org.anon.enterprise.AnonServer;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The client runner will connect to the anon server, and run the anonymisation in the server context. 
 * 
 * This class runs in a standalone JVM and connects to the server JVM over AnonServer
 * 
 */
public class AnonServerRunner extends Runner{
	
	static Logger logger = Logger.getLogger(AnonServerRunner.class);

	
	public static void main(String[] args) {
		try{
			String databaseConfigGuiName = checkParams(args);
			
			// Start Spring
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("AnonServerRunner.xml");
			AnonServer anonServer= context.getBean(AnonServer.class);
			
			logger.info("Server connected, running anonimisation " + databaseConfigGuiName);
			// run the stuff
			anonServer.runAll(databaseConfigGuiName);
		
			logger.info("Anonimisation finished " + databaseConfigGuiName);
			
			context.close();
		}catch(Exception e){
			logger.error("AnonServerRunner failed with error " + e.getMessage(), e);
		}
		
		
	}




}
