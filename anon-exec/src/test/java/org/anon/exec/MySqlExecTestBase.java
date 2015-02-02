package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.Database;
import org.anon.exec.mock.LicenseManagerMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:BaseExecTest.xml")
public class MySqlExecTestBase extends BaseExecTest {
	
	@Autowired
	DataSource dataSourceMySql;
	
	@Autowired
	AnonConfig anonConfig;
	
	TestTableCreatorMySql testTableCreator = new TestTableCreatorMySql();
	
	@Override
	DataSource getDataSource() {
		return dataSourceMySql;
	}

	@Override
	protected TestTableCreator getTestTableCreator() {
		return testTableCreator;
	}
	
	protected MySqlExec createExec() {
		MySqlExec mysqlExec = new MySqlExec();
		mysqlExec.setDataSource(dataSourceMySql);
		mysqlExec.setLicenseManager(new LicenseManagerMock());
		mysqlExec.setDbConnectionFactory(new DummyConnectionFactory(dataSourceMySql, Database.MYSQL));
		return mysqlExec;
	}

}
