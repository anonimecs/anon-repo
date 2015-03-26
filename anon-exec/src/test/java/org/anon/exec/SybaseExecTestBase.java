package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.Database;
import org.anon.exec.audit.ExecAuditor;
import org.anon.exec.mock.LicenseManagerMock;
import org.anon.vendor.constraint.ConstraintBundleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

public abstract class SybaseExecTestBase extends BaseExecTest {

	@Autowired
	protected DataSource dataSourceSybase;
	
	@Autowired
	protected ExecAuditor execAuditor;
	
	@Override
	DataSource getDataSource() {
		return dataSourceSybase;
	}
	
	TestTableCreatorSybase testTableCreator = new TestTableCreatorSybase();

	@Value("${sybase.test.schema}")
	private String schema;
	
	@Value("${sybase.available}")
	private String available;
	
	
	@Override
	protected String getSchema() {
		return schema;
	}

	@Override
	protected boolean assumeDbAvailable() {
		return Boolean.valueOf(available);
		
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
		ReflectionTestUtils.setField(sybaseExec, "constraintBundleFactory", new ConstraintBundleFactory());

		return sybaseExec;
	}
	public TestTableCreatorSybase getTestTableCreator() {
		return testTableCreator;
	}



}