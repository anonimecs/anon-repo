package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.logic.AnonymisationMethodEncryptSybase;
import org.anon.vendor.SybaseDbConnection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:BaseExecTest.xml")
public class SybaseExecTestMultiColFK extends BaseExecTest {

	@Autowired
	DataSource dataSourceSybase;
	
	@Autowired
	AnonConfig anonConfig;
	
	@Override
	DataSource getDataSource() {
		return dataSourceSybase;
	}
	
	TestTableCreatorSybase testTableCreator = new TestTableCreatorSybase("TMP_TABLE_A", "TMP_TABLE_B", "TMP_TABLE_A_2_SYBASE.sql", "TMP_TABLE_B_2_SYBASE.sql", 3, 3);

	public TestTableCreatorSybase getTestTableCreator() {
		return testTableCreator;
	}
	
	protected SybaseExec createExec() {
		SybaseExec sybaseExec = new SybaseExec();
		sybaseExec.setDataSource(dataSourceSybase);
		sybaseExec.setLicenseManager(new DummyLicenseManager(false));
		return sybaseExec;
	}
	
	@Test
	public void test_anonymiseString() {
		SybaseExec sybaseExec = createExec();
		
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		
		AnonymisationMethodEncryptSybase method = new AnonymisationMethodEncryptSybase();
		anonConfig.addAnonMethod(method);
		
		
		getTestAnonimisedColumnInfo("col1_ref", "VARCHAR2", "TMP_TABLE_B",method, SybaseDbConnection.databaseSpecifics, anonConfig);
		getTestAnonimisedColumnInfo("col1", "VARCHAR2", "TMP_TABLE_A",method, SybaseDbConnection.databaseSpecifics, anonConfig);

		
		sybaseExec.setExecConfig(anonConfig);
		sybaseExec.runAll();
		
	}
	


}
