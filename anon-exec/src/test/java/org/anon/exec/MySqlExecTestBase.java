package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.Database;
import org.anon.exec.audit.ExecAuditor;
import org.anon.exec.mock.LicenseManagerMock;
import org.anon.vendor.constraint.ConstraintBundleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

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
	
	@Value("${mysql.available}")
	private String available;
	
	
	@Override
	protected boolean assumeDbAvailable() {
		return Boolean.valueOf(available);
		
	}

	
	@Override
	protected String getSchema() {
		return schema;
	}

	
	@Override
	protected DataSource getDataSource() {
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
		ReflectionTestUtils.setField(mysqlExec, "constraintBundleFactory", new ConstraintBundleFactory());

		mysqlExec.setDbConnectionFactory(new DummyConnectionFactory(dataSourceMySql, Database.MYSQL));
		return mysqlExec;
	}

}
