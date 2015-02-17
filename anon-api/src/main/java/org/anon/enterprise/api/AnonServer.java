<<<<<<< HEAD
package org.anon.enterprise.api;

import org.anon.persistence.data.DatabaseConfig;



/**
 * This is the client JVM interface to the server JVM. Currently connected over RMI
 */
public interface AnonServer {
	
	void runAll(String databaseConfigGuiName);

	void useDatabaseConfig(DatabaseConfig databaseConfig);

}
=======
package org.anon.enterprise.api;

import org.anon.persistence.data.DatabaseConfig;



/**
 * This is the client JVM interface to the server JVM. Currently connected over RMI
 */
public interface AnonServer {
	
	void runAll(String databaseConfigGuiName);

	void useDatabaseConfig(DatabaseConfig databaseConfig);

}
>>>>>>> f433e150161bc23d087604a6480c2ef237bf6713
