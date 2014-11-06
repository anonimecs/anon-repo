package org.anon.logic;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:spring-test-datasources.xml" })
public class AnonymisationMethodEncryptSybaseTest extends AbstractJUnit4SpringContextTests{

	
	@Autowired
	DataSource dataSource;

//	@Test
	public void testAnonymiseString() {
		AnonymisationMethodEncryptSybase anonymisationMethodEncryptSybase = new AnonymisationMethodEncryptSybase();
		anonymisationMethodEncryptSybase.setDataSource(dataSource);
		
		System.out.println("++++++++ " + anonymisationMethodEncryptSybase.anonymiseString("aaaaaa"));
		System.out.println("++++++++ " + anonymisationMethodEncryptSybase.anonymiseString("bbdf"));
		System.out.println("++++++++ " + anonymisationMethodEncryptSybase.anonymiseString("aaaaasdfwefaa"));
	}
	
	@Test
	public void testAnonymiseDouble() {
		AnonymisationMethodEncryptSybase anonymisationMethodEncryptSybase = new AnonymisationMethodEncryptSybase();
		anonymisationMethodEncryptSybase.setDataSource(dataSource);
		
		System.out.println("++++++++ " + anonymisationMethodEncryptSybase.anonymiseDouble(666.6));
		System.out.println("++++++++ " + anonymisationMethodEncryptSybase.anonymiseDouble(10000.0));
	}

}
