package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.exec.audit.ExecAuditor;
import org.anon.logic.AnonymisationMethodEncryptOracle;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class OracleExecTestMultiColFKTest extends OracleExecTestBase {

	@Autowired
	DataSource dataSourceOracle;
	
	@Autowired
	AnonConfig anonConfig;
	
	@Autowired
	ExecAuditor execAuditor;
	
	@Override
	protected DataSource getDataSource() {
		return dataSourceOracle;
	}
	
	TestTableCreatorOracle testTableCreator = new TestTableCreatorOracle("TMP_TABLE_A", "TMP_TABLE_B", "TMP_TABLE_A_2_ORA.sql", "TMP_TABLE_B_2_ORA.sql", 3, 3);

	public TestTableCreatorOracle getTestTableCreator() {
		return testTableCreator;
	}

	
	@Test
	public void test_anonymiseString() {
		OracleExec oracleExec = createExec();
		
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		
		AnonymisationMethodEncryptOracle method = new AnonymisationMethodEncryptOracle();
		anonConfig.addAnonMethod(method);
		
		
		getTestAnonimisedColumnInfo("col1_ref", "VARCHAR2", "TMP_TABLE_B",method, DatabaseSpecifics.OracleSpecific, anonConfig);
		getTestAnonimisedColumnInfo("col1", "VARCHAR2", "TMP_TABLE_A",method, DatabaseSpecifics.OracleSpecific, anonConfig);

		
		oracleExec.setExecConfig(anonConfig);
		oracleExec.runAll();
		
	}
	


}
