package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.exec.audit.ExecAuditor;
import org.anon.logic.AnonymisationMethodEncryptSqlServer;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class SqlServerExecTestMultiColFK extends SqlServerExecTestBase {

	@Autowired
	DataSource dataSourceSqlServer;
	
	@Autowired
	AnonConfig anonConfig;
	
	@Autowired
	ExecAuditor execAuditor;
	
	@Override
	DataSource getDataSource() {
		return dataSourceSqlServer;
	}
	
	TestTableCreatorSqlServer testTableCreator = new TestTableCreatorSqlServer("TMP_TABLE_A", "TMP_TABLE_B", "TMP_TABLE_A_2_SQLSERVER.sql", "TMP_TABLE_B_2_SQLSERVER.sql", 3, 3);

	public TestTableCreatorSqlServer getTestTableCreator() {
		return testTableCreator;
	}
	
	@Test
	public void test_anonymiseString() {
		SqlServerExec sqlServerExec = createSqlServerExec();
		
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		
		AnonymisationMethodEncryptSqlServer method = new AnonymisationMethodEncryptSqlServer();
		anonConfig.addAnonMethod(method);
		
		
		getTestAnonimisedColumnInfo("col1_ref", "VARCHAR2", "TMP_TABLE_B",method, DatabaseSpecifics.SqlServerSpecific, anonConfig);
		getTestAnonimisedColumnInfo("col1", "VARCHAR2", "TMP_TABLE_A",method, DatabaseSpecifics.SqlServerSpecific, anonConfig);

		
		sqlServerExec.setExecConfig(anonConfig);
		sqlServerExec.runAll();
		
	}
	


}
