package org.anon;

import javax.sql.DataSource;

import org.anon.persistence.dao.EntitiesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;


@ContextConfiguration(locations = { "classpath:spring-persistence-test.xml" })
public abstract class DbEntitiesTest extends AbstractJUnit4SpringContextTests {

		@Autowired
		EntitiesDao entitiesDao;
		
		@Autowired
		DataSource dataSource;
		

}
