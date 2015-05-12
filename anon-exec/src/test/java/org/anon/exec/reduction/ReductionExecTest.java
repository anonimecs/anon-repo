package org.anon.exec.reduction;

import java.io.IOException;

import org.anon.exec.BaseDbTest;
import org.anon.exec.ExecFactory;
import org.anon.exec.TwoTestTablesCreator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:ReductionExecTest.xml")
public abstract class ReductionExecTest extends BaseDbTest{
	
	@Autowired ExecFactory execFactory;
	
	abstract protected String getSchema();

	private JdbcTemplate jdbcTemplate;
	
	protected JdbcTemplate getJdbcTemplate() {
		if(jdbcTemplate == null){
			jdbcTemplate = new JdbcTemplate(getDataSource());
			jdbcTemplate.execute("use " + getSchema());
		}
		return jdbcTemplate;
	}

	



	abstract protected boolean assumeDbAvailable();

	@Before
	public void beforeTest(){
		Assume.assumeTrue(assumeDbAvailable());
	}

	
	@Before
	public void createTable() throws IOException{
		if(!assumeDbAvailable()){
			return;
		}
		dropTables();
		getTestTableCreator().createTables(getJdbcTemplate());
	}


	
	@After
	public void dropTables() {
		if(!assumeDbAvailable()){
			return;
		}
		try{
			getTestTableCreator().dropTableB(getJdbcTemplate());
		}
		catch(Exception e){}
		try {
			getTestTableCreator().dropTableA(getJdbcTemplate());
		} catch (Exception e) {}
	}
	
	@Test
	public void test1_simpleTest() {
		Assert.assertEquals(getTestTableCreator().getRowcountTableB(), getJdbcTemplate().queryForInt("select count(*) from TMP_TABLE_B"));
		Assert.assertEquals(getTestTableCreator().getRowcountTableA(), getJdbcTemplate().queryForInt("select count(*) from TMP_TABLE_A"));
	}
	
	protected abstract TwoTestTablesCreator getTestTableCreator();
	





}
