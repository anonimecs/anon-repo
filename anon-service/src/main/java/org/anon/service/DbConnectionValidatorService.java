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
		ServiceResult result = new ServiceResult();
		
		String drvClass = config.getVendor().getDriver();
		testUrl.append(config.getVendor().getJdbcPrefix()).append(config.getUrl());
		
		if(config.getDefaultSchema() != null && !config.getDefaultSchema().isEmpty()) {
			testUrl.append(config.getVendor().getSchemaAppendix(config.getDefaultSchema()));
		}
		
		try {
			Class.forName(drvClass);
			Connection conn = DriverManager.getConnection
					(testUrl.toString(), config.getLogin(), config.getPassword());
			result.setFailed(!conn.isValid(3));
			conn.close();
		} catch (SQLException e) {
			result.addErrorMessage("Connection invalid", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage());
		} catch (ClassNotFoundException e) {
			result.addErrorMessage("Driver not found", drvClass);
		}
		return result;
	}
	
	
}
