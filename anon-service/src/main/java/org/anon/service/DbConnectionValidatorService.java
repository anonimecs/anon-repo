package org.anon.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.anon.AnonStatic;
import org.anon.persistence.data.DatabaseConnection;
import org.springframework.stereotype.Service;

@Service
public class DbConnectionValidatorService extends AnonStatic{

	public void connectionValid(DatabaseConnection databaseConnection) throws ServiceException{
		
		
		StringBuilder testUrl = new StringBuilder();	
		
		String drvClass = databaseConnection.getVendor().getDriver();
		testUrl.append(databaseConnection.getVendor().getJdbcPrefix()).append(databaseConnection.getUrl());
				
		Connection conn = null;
		try {
			Class.forName(drvClass);
			conn = DriverManager.getConnection
					(testUrl.toString(), databaseConnection.getLogin(), databaseConnection.getPassword());
			if (!conn.isValid(3)){
				throw new ServiceException("Connection is not valid", null, null);
			};
			
		} catch (SQLException e) {
			throw new ServiceException("Connection invalid", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new ServiceException("Driver not found", drvClass, e);
		}
		finally{
			try {
				conn.close();
			} catch (Exception ignore) {}
		}
	}
	
	
}
