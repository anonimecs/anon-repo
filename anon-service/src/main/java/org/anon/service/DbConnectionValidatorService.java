package org.anon.service;

import org.anon.AnonStatic;
import org.anon.persistence.data.DatabaseConfig;
import org.springframework.stereotype.Service;

@Service
public class DbConnectionValidatorService extends AnonStatic{

	public void connectionValid(DatabaseConfig config) throws ServiceException{
		
		new RuntimeException("TODO").printStackTrace(System.err);

		
//		StringBuilder testUrl = new StringBuilder();	
//		
//		String drvClass = config.getVendor().getDriver();
//		testUrl.append(config.getVendor().getJdbcPrefix()).append(config.getUrl());
//		
//		if(config.getDefaultSchema() != null && !config.getDefaultSchema().isEmpty()) {
//			testUrl.append(config.getVendor().getSchemaAppendix(config.getDefaultSchema()));
//		}
//		
//		Connection conn = null;
//		try {
//			Class.forName(drvClass);
//			conn = DriverManager.getConnection
//					(testUrl.toString(), config.getLogin(), config.getPassword());
//			if (!conn.isValid(3)){
//				throw new ServiceException("Connection is not valid", null, null);
//			};
//			
//		} catch (SQLException e) {
//			throw new ServiceException("Connection invalid", 
//					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage(), e);
//		} catch (ClassNotFoundException e) {
//			throw new ServiceException("Driver not found", drvClass, e);
//		}
//		finally{
//			try {
//				conn.close();
//			} catch (Exception ignore) {}
//		}
	}
	
	
}
