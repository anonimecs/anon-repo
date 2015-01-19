package org.anon.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class TestTableCreator {

	public String nameTableA, nameTableB;
	public String fileNameTableA, fileNameTableB;
	public int rowcountTableA, rowcountTableB;
	
	
	
	public TestTableCreator(String nameTableA, String nameTableB, String fileNameTableA, String fileNameTableB,
			int rowcountTableA, int rowcountTableB) {
		super();
		this.nameTableA = nameTableA;
		this.nameTableB = nameTableB;
		this.fileNameTableA = fileNameTableA;
		this.fileNameTableB = fileNameTableB;
		this.rowcountTableA = rowcountTableA;
		this.rowcountTableB = rowcountTableB;
	}

	public void dropTableA(JdbcTemplate jdbcTemplate) throws IOException {
		jdbcTemplate.execute("drop table "  + nameTableA);
	}	

	public void dropTableB(JdbcTemplate jdbcTemplate) throws IOException {
		jdbcTemplate.execute("drop table " + nameTableB);
	}	

	public void createTables(JdbcTemplate jdbcTemplate) throws IOException {
		for (String sql : getStatements("/" + fileNameTableA)) {
			jdbcTemplate.execute(sql);
		}
		for (String sql : getStatements("/" + fileNameTableB)) {
			jdbcTemplate.execute(sql);
		}
	}
	
	protected List<String> getStatements(String filePath) throws IOException {
		InputStream inputStream = this.getClass().getResourceAsStream(filePath);  
		StringWriter writer = new StringWriter();
		IOUtils.copy(inputStream, writer);
		String fileContent = writer.toString();
		String [] res = split(fileContent);
		
		return filerStatements(res);
	}

	protected String[] split(String fileContent) {
		return fileContent.split(";");
	}

	
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

	public String getFileNameTableA() {
		return fileNameTableA;
	}

	public String getFileNameTableB() {
		return fileNameTableB;
	}

	public int getRowcountTableA() {
		return rowcountTableA;
	}

	public int getRowcountTableB() {
		return rowcountTableB;
	}

	public String getNameTableA() {
		return nameTableA;
	}

	public String getNameTableB() {
		return nameTableB;
	}
}
