package org.anon.exec;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class SybaseExecTestBase extends BaseExecTest {

	@Autowired
	DataSource dataSourceSybase;
	
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
		sybaseExec.setLicenseManager(new DummyLicenseManager(false));
		return sybaseExec;
	}
	public TestTableCreatorSybase getTestTableCreator() {
		return testTableCreator;
	}



}