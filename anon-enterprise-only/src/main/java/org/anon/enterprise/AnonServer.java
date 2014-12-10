package org.anon.enterprise;

/**
 * This is the client JVM interface to the server JVM. Currently connected over RMI
 */
public interface AnonServer {

	void runAll(String databaseConfigGuiName);

}
