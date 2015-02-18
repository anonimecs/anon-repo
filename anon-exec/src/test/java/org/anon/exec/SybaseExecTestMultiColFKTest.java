package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.exec.audit.ExecAuditor;
import org.anon.logic.AnonymisationMethodEncryptSybase;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class SybaseExecTestMultiColFKTest extends SybaseExecTestBase {

	@Autowired
	DataSource dataSourceSybase;
	
	@Autowired
	AnonConfig anonConfig;
	
	@Autowired
	ExecAuditor execAuditor;
	
	@Override
	DataSource getDataSource() {
		return dataSourceSybase;
	}
	
	TestTableCreatorSybase testTableCreator = new TestTableCreatorSybase("TMP_TABLE_A", "TMP_TABLE_B", "TMP_TABLE_A_2_SYBASE.sql", "TMP_TABLE_B_2_SYBASE.sql", 3, 3);

	public TestTableCreatorSybase getTestTableCreator() {
		return testTableCreator;
	}

	
	@Test
	public void test_anonymiseString() {
		SybaseExec sybaseExec = createSybaseExec();
		
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		
		AnonymisationMethodEncryptSybase method = new AnonymisationMethodEncryptSybase();
		anonConfig.addAnonMethod(method);
		
		
		getTestAnonimisedColumnInfo("col1_ref", "VARCHAR2", "TMP_TABLE_B",method, DatabaseSpecifics.SybaseSpecific, anonConfig);
		getTestAnonimisedColumnInfo("col1", "VARCHAR2", "TMP_TABLE_A",method, DatabaseSpecifics.SybaseSpecific, anonConfig);

		
		sybaseExec.setExecConfig(anonConfig);
		sybaseExec.runAll();
		
	}
	


}
