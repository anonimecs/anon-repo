package org.anon.enterprise.runner;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

public abstract class Runner {
	static Logger logger = Logger.getLogger(Runner.class);

	protected static String checkParams(String[] args) {
		logger.info("Starting AnonRunner with params " + Arrays.toString(args));
		Assert.isTrue(args.length > 0, "1 param is required (guiname of the configuration)");
		
		String databaseConfigGuiName = args[0];
		Assert.isTrue(databaseConfigGuiName != null, "databaseConfogGuiName is null");
		Assert.isTrue(! databaseConfigGuiName.isEmpty(), "databaseConfogGuiName is empty");
		return databaseConfigGuiName;
	}
}
