package org.anon.exec;


public class TestTableCreatorSybase extends TestTableCreator{



	public TestTableCreatorSybase() {
		super("TMP_TABLE_A", "TMP_TABLE_B", "TMP_TABLE_A_SYBASE.sql", "TMP_TABLE_B_SYBASE.sql", 178, 40);
	}
	
	public TestTableCreatorSybase(String nameTableA, String nameTableB, String fileNameTableA, String fileNameTableB,
			int rowcountTableA, int rowcountTableB) {
		super(nameTableA, nameTableB, fileNameTableA, fileNameTableB, rowcountTableA, rowcountTableB);
	}

	
	@Override
	protected String[] split(String fileContent) {
		return fileContent.split("GO");
	}
}
