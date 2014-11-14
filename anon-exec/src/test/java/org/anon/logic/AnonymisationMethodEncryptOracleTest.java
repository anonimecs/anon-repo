package org.anon.logic;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:spring-test-datasources.xml" })
public class AnonymisationMethodEncryptOracleTest extends AbstractJUnit4SpringContextTests{

	
	@Autowired
	DataSource dataSourceOracle;

	@Test
	public void testAnonymiseString() {
		AnonymisationMethodEncryptOracle anonymisationMethodEncryptOracle = new AnonymisationMethodEncryptOracle();
		anonymisationMethodEncryptOracle.setDataSource(dataSourceOracle);
		
		System.out.println("++++++++ " + anonymisationMethodEncryptOracle.anonymiseString("aaaaaa"));
		System.out.println("++++++++ " + anonymisationMethodEncryptOracle.anonymiseString("bbdf"));
		System.out.println("++++++++ " + anonymisationMethodEncryptOracle.anonymiseString("aaaaasdfwefaa"));
	}
	
	@Test
	public void testAnonymiseDouble() {
		AnonymisationMethodEncryptOracle anonymisationMethodEncryptOracle = new AnonymisationMethodEncryptOracle();
		anonymisationMethodEncryptOracle.setDataSource(dataSourceOracle);
		
		System.out.println("++++++++ " + anonymisationMethodEncryptOracle.anonymiseDouble(666.6));
		System.out.println("++++++++ " + anonymisationMethodEncryptOracle.anonymiseDouble(10000.0));
	}

}
