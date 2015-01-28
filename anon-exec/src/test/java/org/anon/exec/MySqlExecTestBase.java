package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
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
		mysqlExec.setLicenseManager(new DummyLicenseManager(false));
		return mysqlExec;
	}

}
