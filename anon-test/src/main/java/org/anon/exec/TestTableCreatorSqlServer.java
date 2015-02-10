package org.anon.exec;


public class TestTableCreatorSqlServer extends TestTableCreator{



	public TestTableCreatorSqlServer() {
		super("TMP_TABLE_A", "TMP_TABLE_B", "TMP_TABLE_A_SQLSERVER.sql", "TMP_TABLE_B_SQLSERVER.sql", 178, 40);
	}
	
	public TestTableCreatorSqlServer(String nameTableA, String nameTableB, String fileNameTableA, String fileNameTableB,
			int rowcountTableA, int rowcountTableB) {
		super(nameTableA, nameTableB, fileNameTableA, fileNameTableB, rowcountTableA, rowcountTableB);
	}

	
	@Override
	protected String[] split(String fileContent) {
		return fileContent.split("GO");
	}
}
