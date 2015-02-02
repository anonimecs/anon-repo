package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:BaseExecTest.xml")
public abstract class OracleExecTestBase extends BaseExecTest {

	@Autowired
	DataSource dataSourceOracle;
	
	@Autowired
	AnonConfig anonConfig;

	@Override
	DataSource getDataSource() {
		return dataSourceOracle;
	}
	
	TestTableCreatorOracle testTableCreator = new TestTableCreatorOracle();

	public TestTableCreatorOracle getTestTableCreator() {
		return testTableCreator;
	}
	
	protected OracleExec createExec() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceOracle);
		oracleExec.setLicenseManager(new DummyLicenseManager(false));
		oracleExec.setDbConnectionFactory(new DummyConnectionFactory(dataSourceOracle, Database.ORACLE));
		return oracleExec;
	}

}
