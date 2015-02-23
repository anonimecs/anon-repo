package org.anon.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestTableCreatorSupport {

	public List<String> getStatements(String filePath) throws IOException {
		InputStream inputStream = this.getClass().getResourceAsStream(filePath);  
		StringWriter writer = new StringWriter();
		IOUtils.copy(inputStream, writer);
		String fileContent = writer.toString();
		String [] res = split(fileContent);
		
		return filerStatements(res);
	}

	public String[] split(String fileContent) {
		return fileContent.split(";");
	}

	
	public List<String> filerStatements(String[] statements) {
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
			if(sql.toLowerCase().contains("drop table")){
				continue;
			}
			res.add(sql.trim().replace("\n", " ").replace("\r", " "));
		}
		return res;
	}

	public void runScript(JdbcTemplate jdbcTemplate, String filePath, String useSchemaSql) throws IOException{
		jdbcTemplate.execute(useSchemaSql);
		for(String sql:getStatements(filePath)){
			jdbcTemplate.execute(sql);
		}
	}
}
