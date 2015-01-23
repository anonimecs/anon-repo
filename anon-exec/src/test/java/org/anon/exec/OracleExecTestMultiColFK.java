package org.anon.exec;

import javax.sql.DataSource;

import org.anon.data.AnonConfig;
import org.anon.logic.AnonymisationMethodEncryptOracle;
import org.anon.vendor.OracleDbConnection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:BaseExecTest.xml")
public class OracleExecTestMultiColFK extends BaseExecTest {

	@Autowired
	DataSource dataSourceOracle;
	
	@Autowired
	AnonConfig anonConfig;
	
	@Override
	DataSource getDataSource() {
		return dataSourceOracle;
	}
	
	TestTableCreatorOracle testTableCreator = new TestTableCreatorOracle("TMP_TABLE_A", "TMP_TABLE_B", "TMP_TABLE_A_2_ORA.sql", "TMP_TABLE_B_2_ORA.sql", 3, 3);

	public TestTableCreatorOracle getTestTableCreator() {
		return testTableCreator;
	}
	
	protected OracleExec createExec() {
		OracleExec oracleExec = new OracleExec();
		oracleExec.setDataSource(dataSourceOracle);
		oracleExec.setLicenseManager(new DummyLicenseManager(false));
		return oracleExec;
	}
	
	@Test
	public void test_anonymiseString() {
		OracleExec oracleExec = createExec();
		
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		
		AnonymisationMethodEncryptOracle method = new AnonymisationMethodEncryptOracle();
		anonConfig.addAnonMethod(method);
		
		
		getTestAnonimisedColumnInfo("col1_ref", "VARCHAR2", "TMP_TABLE_B",method, OracleDbConnection.databaseSpecifics, anonConfig);
		getTestAnonimisedColumnInfo("col1", "VARCHAR2", "TMP_TABLE_A",method, OracleDbConnection.databaseSpecifics, anonConfig);

		
		oracleExec.setAnonConfig(anonConfig);
		oracleExec.runAll();
		
	}
	


}