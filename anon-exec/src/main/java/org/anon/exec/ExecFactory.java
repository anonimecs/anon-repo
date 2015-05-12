package org.anon.exec;

import org.anon.exec.reduction.MySqlReductionExec;
import org.anon.exec.reduction.OracleReductionExec;
import org.anon.exec.reduction.ReductionExec;
import org.anon.exec.reduction.SqlServerReductionExec;
import org.anon.exec.reduction.SybaseReductionExec;
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
	
	
	public AnonExec createExec(DatabaseSpecifics databaseSpecifics, String user) {
		AnonExec exec;
		if(databaseSpecifics == DatabaseSpecifics.SybaseSpecific){
			exec = applicationContext.getBean(SybaseExec.class);

		}
		else if(databaseSpecifics == DatabaseSpecifics.OracleSpecific){
			exec = applicationContext.getBean(OracleExec.class);
		} 
		else if(databaseSpecifics == DatabaseSpecifics.MySqlSpecific) {
			exec = applicationContext.getBean(MySqlExec.class);
		}
		else if(databaseSpecifics == DatabaseSpecifics.SqlServerSpecific) {
			exec = applicationContext.getBean(SqlServerExec.class);
		} else {
			throw new RuntimeException("Unspecified db " + databaseSpecifics);
		}
		
		exec.setUserName(user);
		return exec;	
	}
	
	public ReductionExec createReductionExec(DatabaseSpecifics databaseSpecifics, String user) {
		ReductionExec exec;
		if(databaseSpecifics == DatabaseSpecifics.SybaseSpecific){
			exec = applicationContext.getBean(SybaseReductionExec.class);
		}
		else if(databaseSpecifics == DatabaseSpecifics.OracleSpecific){
			exec = applicationContext.getBean(OracleReductionExec.class);
		} 
		else if(databaseSpecifics == DatabaseSpecifics.MySqlSpecific) {
			exec = applicationContext.getBean(MySqlReductionExec.class);
		}
		else if(databaseSpecifics == DatabaseSpecifics.SqlServerSpecific) {
			exec = applicationContext.getBean(SqlServerReductionExec.class);
		} else {
			throw new RuntimeException("Unspecified db " + databaseSpecifics);
		}
		
		exec.setUserName(user);
		return exec;
		
	}

}
