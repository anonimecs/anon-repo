package org.anon.exec;

import java.io.IOException;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class TwoTestTablesCreator extends TestTableCreatorSupport
{

	public String nameTableA, nameTableB;
	public String fileNameTableA, fileNameTableB;
	public int rowcountTableA, rowcountTableB;
	
	
	
	public TwoTestTablesCreator(String nameTableA, String nameTableB, String fileNameTableA, String fileNameTableB,
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
