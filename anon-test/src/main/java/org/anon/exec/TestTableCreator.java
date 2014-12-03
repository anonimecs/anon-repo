package org.anon.exec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class TestTableCreator {

	public void dropTableA(JdbcTemplate jdbcTemplate) throws IOException {
		jdbcTemplate.execute("drop table TMP_TABLE_A");
	}	

	public void dropTableB(JdbcTemplate jdbcTemplate) throws IOException {
		jdbcTemplate.execute("drop table TMP_TABLE_B");
	}	
	public abstract void createTables(JdbcTemplate jdbcTemplate) throws IOException;
	public abstract int getRowCount_TMP_TABLE_A();
	public abstract int getRowCount_TMP_TABLE_B();
	
	protected List<String> filerStatements(String[] statements) {
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
}
