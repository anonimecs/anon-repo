package org.anon.enterprise.api;



/**
 * This is the client JVM interface to the server JVM. Currently connected over RMI
 */
public interface AnonServer {
	
	void runAll(String databaseConfigGuiName);


}
