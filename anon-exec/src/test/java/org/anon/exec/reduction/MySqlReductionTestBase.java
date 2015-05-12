package org.anon.exec.reduction;

import javax.sql.DataSource;

import org.anon.exec.TestTableCreatorMySql;
import org.anon.exec.TwoTestTablesCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class MySqlReductionTestBase extends ReductionExecTest {
	
	@Autowired
	DataSource dataSourceMySql;
		
	TestTableCreatorMySql testTableCreator = new TestTableCreatorMySql();
	
	@Value("${mysql.test.schema}")
	private String schema;
	
	@Value("${mysql.available}")
	private String available;
	
	
	@Override
	protected boolean assumeDbAvailable() {
		return Boolean.valueOf(available);
		
	}

	
	@Override
	protected String getSchema() {
		return schema;
	}

	
	@Override
	protected DataSource getDataSource() {
		return dataSourceMySql;
	}

	@Override
	protected TwoTestTablesCreator getTestTableCreator() {
		return testTableCreator;
	}
	


}
