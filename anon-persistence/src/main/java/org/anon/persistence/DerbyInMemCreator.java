package org.anon.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;


public class DerbyInMemCreator {

	protected Logger logger = Logger.getLogger(getClass());

	protected JdbcTemplate jdbcTemplate;
	protected DataSource dataSource;
	
	@Required
	public void setDataSource(DataSource dataSource) {
    	this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }		
	
	public void createTables() throws IOException {
		logger.warn(">>>>>>>>>>>>>>>>>> Creating Tables in the memory database <<<<<<<<<<<<<<<<<<<<<<");
		runFile("/sql/create_tables.sql");
	}
	
	public void dropTables() throws IOException {
		logger.warn(">>>>>>>>>>>>>>>>>> Dropping tables in the memory database <<<<<<<<<<<<<<<<<<<<<<");
		runFile("/sql/drop_tables.sql");
	}
	

	private void runFile(String fileName) throws IOException {
		for (String sql : getStatements(fileName)) {
			try {
				jdbcTemplate.execute(sql);
			} catch (Exception e) {
				logger.error("failed " + sql, e);
			}
		}
		
	}

	private List<String> getStatements(String fileName) throws IOException {
		InputStream inputStream = this.getClass().getResourceAsStream(fileName);  
		StringWriter writer = new StringWriter();
		IOUtils.copy(inputStream, writer);
		String fileContent = writer.toString();
		String [] res = fileContent.split("go");
		
		return filerStatements(res);
	}

	private List<String> filerStatements(String[] statements) {
		List<String> res = new ArrayList<>();
		for(String sql:statements){
			if(sql.isEmpty()){
				continue;
			}
			if(sql.length() < 10){
				continue;
			}
			if(sql.trim().startsWith("--")){
				continue;
			}
			if(sql.trim().startsWith("/*")){
				continue;
			}			
//			if(sql.toLowerCase().contains("drop table")){
//				continue;
//			}
			res.add(sql.trim().replace("\n", " ").replace("\r", " "));
		}
		return res;
	}
}
