package org.anon.exec;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.anon.test.AnonUnitTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration("classpath:spring-test-datasources.xml")
public abstract class BaseDbTest extends AbstractJUnit4SpringContextTests implements AnonUnitTest{

	@Value("${mysql.available}")
	protected Boolean mysqlAvailable;
	@Value("${sybase.available}")
	protected Boolean sybaseAvailable;
	@Value("${oracle.available}")
	protected Boolean oracleAvailable;
	@Value("${sqlserver.available}")
	protected Boolean sqlserverAvailable;

	
	
    /**
     * Run a custom SQL against the database to validate your result
     */
    protected List<Map<String, Object>> loadFromDb(String sqlQuery) {
        return new JdbcTemplate(getDataSource()).query(sqlQuery, new ColumnMapRowMapper());
    }

    protected <T> T loadFromDbObject(String sqlQuery, Class <T> clazz) {
        return new JdbcTemplate(getDataSource()).queryForObject(sqlQuery, clazz);
    }

    /**
     * Clean up something in the DB
     */
    protected void updateDB(String sqlUpdate) {
        new JdbcTemplate(getDataSource()).update(sqlUpdate);
    }
    
    
    DataSource getDataSource(){
    	throw new RuntimeException("This method must be overloaded to deliver the datasource");
    }
}
