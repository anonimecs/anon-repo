package org.anon.exec;

import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.MySqlDbConnection;
import org.anon.vendor.OracleDbConnection;
import org.anon.vendor.SybaseDbConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ExecFactory {

	/**
	 * Need the context to be able to create new prototype scope beans every time the factory method is called on this singleton
	 */
	@Autowired ApplicationContext applicationContext;
	
	
	public BaseExec createExec(DatabaseSpecifics databaseSpecifics) {
		
		if(databaseSpecifics == SybaseDbConnection.databaseSpecifics){
			return applicationContext.getBean(SybaseExec.class);
		}
		else if(databaseSpecifics == OracleDbConnection.databaseSpecifics){
			return applicationContext.getBean(OracleExec.class);
		} 
		else if(databaseSpecifics == MySqlDbConnection.databaseSpecifics) {
			return applicationContext.getBean(MySqlExec.class);
		}

		throw new RuntimeException("Unspecified db " + databaseSpecifics);
	}

}
