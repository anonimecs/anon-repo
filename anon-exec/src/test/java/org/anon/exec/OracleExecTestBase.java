package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.data.Database;
import org.anon.exec.audit.ExecAuditor;
import org.anon.exec.mock.LicenseManagerMock;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class OracleExecTestBase extends BaseExecTest {

	@Autowired
	DataSource dataSourceOracle;
	
	@Autowired
	AnonConfig anonConfig;

	@Autowired
	ExecAuditor execAuditor;
	
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
		oracleExec.setUserName("junit");
		oracleExec.setLicenseManager(new LicenseManagerMock());
		oracleExec.setExecAuditor(execAuditor);
		oracleExec.setGuiNotifier(new NullGuiNotifier());
		
		oracleExec.setDbConnectionFactory(new DummyConnectionFactory(dataSourceOracle, Database.ORACLE));
		return oracleExec;
	}

}
