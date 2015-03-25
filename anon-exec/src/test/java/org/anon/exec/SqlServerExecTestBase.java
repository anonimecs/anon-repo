package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.Database;
import org.anon.exec.audit.ExecAuditor;
import org.anon.exec.mock.LicenseManagerMock;
import org.junit.Assume;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class SqlServerExecTestBase extends BaseExecTest {

	@Autowired
	DataSource dataSourceSqlServer;
	
	@Autowired
	AnonConfig anonConfig;

	
	@Autowired
	ExecAuditor execAuditor;
	
	
	@Value("${sqlserver.available}")
	private String available;

	
	@Override
	DataSource getDataSource() {
		return dataSourceSqlServer;
	}
	

	
	TestTableCreatorSqlServer testTableCreator = new TestTableCreatorSqlServer();

	@Value("${sqlserver.test.schema}")
	private String schema;
	
	
	@Override
	protected String getSchema() {
		return schema;
	}

	@Override
	protected boolean assumeDbAvailable() {
		return Boolean.valueOf(available);
		
	}


	
	public SqlServerExecTestBase() {
		super();
	}

	protected SqlServerExec createSqlServerExec() {
		SqlServerExec sqlServerExec = new SqlServerExec();
		sqlServerExec.setDataSource(dataSourceSqlServer);
		sqlServerExec.setUserName("junit");
		sqlServerExec.setLicenseManager(new LicenseManagerMock());
		sqlServerExec.setExecAuditor(execAuditor);
		sqlServerExec.setGuiNotifier(new NullGuiNotifier());
		sqlServerExec.setDbConnectionFactory(new DummyConnectionFactory(dataSourceSqlServer, Database.SYBASE));
		return sqlServerExec;
	}
	public TestTableCreatorSqlServer getTestTableCreator() {
		return testTableCreator;
	}



}