package org.anon.persistence;

import org.apache.derby.drda.NetworkServerControl;
import org.apache.log4j.Logger;

public class DerbyServerStarter {

	private Logger logger = Logger.getLogger(getClass());

	public void startServer() throws Exception {
		logger.info("-----------------------------------------Starting derby server");

		System.setProperty("derby.drda.startNetworkServer", "true");
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
		
		waitForStart();

	}

	private void waitForStart() throws Exception {

		// Server instance for testing connection
		NetworkServerControl server = new NetworkServerControl();

		// Use NetworkServerControl.ping() to wait for
		// NetworkServer to come up.
		logger.debug("Testing if Network Server is up and running!");
		for (int i = 0; i < 10; i++) {
			try {

				Thread.sleep(1000);
				server.ping();
				break;
			} catch (Exception e) {
				logger.debug("Try #" + i + " " + e.toString());
				if (i == 9) {
					logger.error("Giving up trying to connect to Network Server!");
					throw e;
				}
			}
		}
		logger.debug("Derby Network Server now running");

	}
}
