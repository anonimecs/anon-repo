package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.Database;
import org.anon.exec.audit.ExecAuditor;
import org.anon.exec.mock.LicenseManagerMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class MySqlExecTestBase extends BaseExecTest {
	
	@Autowired
	DataSource dataSourceMySql;
	
	@Autowired
	AnonConfig anonConfig;
	
	@Autowired
	ExecAuditor execAuditor;
	
	TestTableCreatorMySql testTableCreator = new TestTableCreatorMySql();
	
	@Value("${mysql.test.schema}")
	private String schema;
	
	
	@Override
	protected String getSchema() {
		return schema;
	}

	
	@Override
	DataSource getDataSource() {
		return dataSourceMySql;
	}

	@Override
	protected TwoTestTablesCreator getTestTableCreator() {
		return testTableCreator;
	}
	
	protected MySqlExec createExec() {
		MySqlExec mysqlExec = new MySqlExec();
		mysqlExec.setDataSource(dataSourceMySql);
		mysqlExec.setLicenseManager(new LicenseManagerMock());
		mysqlExec.setUserName("junit");
		mysqlExec.setExecAuditor(execAuditor);
		mysqlExec.setGuiNotifier(new NullGuiNotifier());
		
		mysqlExec.setDbConnectionFactory(new DummyConnectionFactory(dataSourceMySql, Database.MYSQL));
		return mysqlExec;
	}

}
