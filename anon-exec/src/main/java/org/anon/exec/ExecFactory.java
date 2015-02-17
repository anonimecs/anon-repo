package org.anon.exec;

import org.anon.vendor.DatabaseSpecifics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ExecFactory {

	/**
	 * Need the context to be able to create new prototype scope beans every time the factory method is called on this singleton
	 */
	@Autowired ApplicationContext applicationContext;
	
	
	public BaseExec createExec(DatabaseSpecifics databaseSpecifics, String user) {
		
		if(databaseSpecifics == DatabaseSpecifics.SybaseSpecific){
			BaseExec exec = applicationContext.getBean(SybaseExec.class);
			exec.setUserName(user);
			return exec;
		}
		else if(databaseSpecifics == DatabaseSpecifics.OracleSpecific){
			BaseExec exec = applicationContext.getBean(OracleExec.class);
			exec.setUserName(user);
			return exec;
		} 
		else if(databaseSpecifics == DatabaseSpecifics.MySqlSpecific) {
			BaseExec exec = applicationContext.getBean(MySqlExec.class);
			exec.setUserName(user);
			return exec;
		}
		else if(databaseSpecifics == DatabaseSpecifics.SqlServerSpecific) {
			BaseExec exec = applicationContext.getBean(MySqlExec.class);
			exec.setUserName(user);
			return exec;
		}

		throw new RuntimeException("Unspecified db " + databaseSpecifics);
	}

}
