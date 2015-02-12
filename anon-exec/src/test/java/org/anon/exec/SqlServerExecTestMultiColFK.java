package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.exec.audit.ExecAuditor;
import org.anon.exec.mock.LicenseManagerMock;
import org.anon.logic.AnonymisationMethodEncryptSqlServer;
import org.anon.vendor.SqlServerDbConnection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class SqlServerExecTestMultiColFK extends BaseExecTest {

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
	
	protected SqlServerExec createSqlServerExec() {
		SqlServerExec sqlServerExec = new SqlServerExec();
		sqlServerExec.setDataSource(dataSourceSqlServer);
		sqlServerExec.setLicenseManager(new LicenseManagerMock());
		sqlServerExec.setExecAuditor(execAuditor);
		sqlServerExec.setGuiNotifier(new NullGuiNotifier());
		return sqlServerExec;
	}
	
	@Test
	public void test_anonymiseString() {
		SqlServerExec sqlServerExec = createSqlServerExec();
		
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		
		AnonymisationMethodEncryptSqlServer method = new AnonymisationMethodEncryptSqlServer();
		anonConfig.addAnonMethod(method);
		
		
		getTestAnonimisedColumnInfo("col1_ref", "VARCHAR2", "TMP_TABLE_B",method, SqlServerDbConnection.databaseSpecifics, anonConfig);
		getTestAnonimisedColumnInfo("col1", "VARCHAR2", "TMP_TABLE_A",method, SqlServerDbConnection.databaseSpecifics, anonConfig);

		
		sqlServerExec.setExecConfig(anonConfig);
		sqlServerExec.runAll();
		
	}
	


}
