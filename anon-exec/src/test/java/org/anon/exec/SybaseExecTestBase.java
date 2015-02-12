package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.Database;
import org.anon.exec.audit.ExecAuditor;
import org.anon.exec.mock.LicenseManagerMock;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SybaseExecTestBase extends BaseExecTest {

	@Autowired
	DataSource dataSourceSybase;
	
	@Autowired
	ExecAuditor execAuditor;
	
	@Override
	DataSource getDataSource() {
		return dataSourceSybase;
	}
	
	TestTableCreatorSybase testTableCreator = new TestTableCreatorSybase();

	public SybaseExecTestBase() {
		super();
	}

	protected SybaseExec createSybaseExec() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		sybaseExec.setUserName("junit");
		sybaseExec.setLicenseManager(new LicenseManagerMock());
		sybaseExec.setExecAuditor(execAuditor);
		sybaseExec.setGuiNotifier(new NullGuiNotifier());
		sybaseExec.setDbConnectionFactory(new DummyConnectionFactory(dataSourceSybase, Database.SYBASE));
		return sybaseExec;
	}
	public TestTableCreatorSybase getTestTableCreator() {
		return testTableCreator;
	}



}