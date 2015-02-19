package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.Database;
import org.anon.exec.audit.ExecAuditor;
import org.anon.exec.mock.LicenseManagerMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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

	@Value("${sybase.test.schema}")
	private String schema;
	
	
	@Override
	protected String getSchema() {
		return schema;
	}

	
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