package org.anon.vendor.constraint.notnull;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.exec.BaseDbTest;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class MySqlNotNullConstraintTest  extends BaseDbTest{

    @Autowired
    protected DataSource dataSourceMySql;

	@Value("${mysql.test.schema}")
	protected String schema;

	MySqlNotNullConstraint mySqlNotNullConstraint;
	DatabaseColumnInfo databaseColumnInfo;
	JdbcTemplate jdbcTemplate ;
	
	@Before
	public void beforeTest(){
		jdbcTemplate = new JdbcTemplate(dataSourceMySql);
		jdbcTemplate.update("create table TMP_TABLE(COL1 varchar(50) not null,primary key(COL1))");
		mySqlNotNullConstraint = new MySqlNotNullConstraint(jdbcTemplate);
		
		DatabaseTableInfo databaseTableInfo = new DatabaseTableInfo();
		databaseTableInfo.setName("TMP_TABLE");
		databaseTableInfo.setSchema(schema);
		databaseColumnInfo = new DatabaseColumnInfo("COL1", "VARCHAR", false, DatabaseSpecifics.MySqlSpecific);
		databaseTableInfo.addColumn(databaseColumnInfo);

	}
	
	@After
	public void afterTest(){
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceMySql);
		try {
			jdbcTemplate.update("drop table TMP_TABLE");
		} catch (DataAccessException ignore) {}
	}
	
	@Test
	public void testCreateActivateSql() {
		String sql = mySqlNotNullConstraint.createActivateSql(databaseColumnInfo);
		Assert.assertTrue(sql.contains("varchar(50)"));
		jdbcTemplate.update(sql);
		System.out.println(sql);
	}

	@Test
	public void testCreateDeactivateSql() {
		String sql = mySqlNotNullConstraint.createDeactivateSql(databaseColumnInfo);
		Assert.assertTrue(sql.contains("varchar(50)"));
		jdbcTemplate.update(sql);
		System.out.println(sql);
	}

}
