package org.anon.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.anon.AnonStatic;
import org.anon.persistence.data.DatabaseConfig;
import org.springframework.stereotype.Service;

@Service
public class DbConnectionValidatorService extends AnonStatic{

	public ServiceResult connectionValid(DatabaseConfig config) {
		
		StringBuilder testUrl = new StringBuilder();
		String drvClass = "";
		ServiceResult result = new ServiceResult();
		
		if(config.getVendor().equals(ORACLE)) {
			testUrl.append(ORACLE_JDBC_PREFIX).append(config.getUrl());
			drvClass = ORACLE_DRIVER_CLASS;
		}
		else if (config.getVendor().equals(SYBASE)) {
			testUrl.append(SYBASE_JDBC_PREFIX).append(config.getUrl());
			drvClass = SYBASE_DRIVER_CLASS;
		}
		else {
			result.addErrorMessage("Vendor not supported", null);
			return result;
		}
		
		try {
			Class.forName(drvClass);
			Connection conn = DriverManager.getConnection
					(testUrl.toString(), config.getLogin(), config.getPassword());
			result.setFailed(!conn.isValid(3));
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			result.addErrorMessage("Connection invalid", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage());
		}
		return result;
	}
	
	
}
