package org.anon.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestTableCreatorOracle extends TestTableCreator{


	public int rowCount_TMP_TABLE_B = 31;
	public int rowCount_TMP_TABLE_A = 5;

	
	@Override
	public void createTables(JdbcTemplate jdbcTemplate) throws IOException {
		for (String sql : getStatements("/TMP_TABLE_A_ORA.sql")) {
//			System.out.println(sql);
			jdbcTemplate.execute(sql);
		}
		for (String sql : getStatements("/TMP_TABLE_B_ORA.sql")) {
//			System.out.println(sql);
			jdbcTemplate.execute(sql);
		}
	}

	private List<String> getStatements(String filePath) throws IOException {
		InputStream inputStream = this.getClass().getResourceAsStream(filePath);  
		StringWriter writer = new StringWriter();
		IOUtils.copy(inputStream, writer);
		String fileContent = writer.toString();
		String [] res = fileContent.split(";");
		
		return filerStatements(res);
	}
	
	@Override
	public int getRowCount_TMP_TABLE_A() {
		return rowCount_TMP_TABLE_A;
	}
	
	@Override
	public int getRowCount_TMP_TABLE_B() {
		return rowCount_TMP_TABLE_B;
	}


}
