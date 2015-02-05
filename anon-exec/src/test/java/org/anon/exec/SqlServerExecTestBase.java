package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.Database;
import org.anon.exec.audit.ExecAuditor;
import org.anon.exec.mock.LicenseManagerMock;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SqlServerExecTestBase extends BaseExecTest {

	@Autowired
	DataSource dataSourceSqlServer;
	
	@Autowired
	ExecAuditor execAuditor;
	
	@Override
	DataSource getDataSource() {
		return dataSourceSqlServer;
	}
	
	TestTableCreatorSqlServer testTableCreator = new TestTableCreatorSqlServer();

	public SqlServerExecTestBase() {
		super();
	}

	protected SqlServerExec createSqlServerExec() {
		SqlServerExec sqlServerExec = new SqlServerExec();
		sqlServerExec.setDataSource(dataSourceSqlServer);
		sqlServerExec.setUserName("junit");
		sqlServerExec.setLicenseManager(new LicenseManagerMock());
		sqlServerExec.setExecAuditor(execAuditor);
		sqlServerExec.setGuiNotifier(new GuiNotifier() {
			
			@Override
			public void refreshExecGui() {
			}
		});
		sqlServerExec.setDbConnectionFactory(new DummyConnectionFactory(dataSourceSqlServer, Database.SYBASE));
		return sqlServerExec;
	}
	public TestTableCreatorSqlServer getTestTableCreator() {
		return testTableCreator;
	}



}