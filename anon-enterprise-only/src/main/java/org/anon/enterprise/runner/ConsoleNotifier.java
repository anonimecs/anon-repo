package org.anon.enterprise.runner;

import org.anon.exec.GuiNotifier;
import org.apache.log4j.Logger;

public class ConsoleNotifier implements GuiNotifier {
	Logger logger = Logger.getLogger(getClass());

	@Override
	public void refreshExecGui(String message) {
		if(message != null){
			logger.info("Execution message: " + message);
		}

	}
	
	@Override
	public void refreshReductionExecGui() {
		logger.info("Reduction event.");
	}

}
