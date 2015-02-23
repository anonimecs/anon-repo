package org.anon.exec;


public class TestTableCreatorOracle extends TwoTestTablesCreator{

	
	
	public TestTableCreatorOracle(String nameTableA, String nameTableB, String fileNameTableA, String fileNameTableB,
			int rowcountTableA, int rowcountTableB) {
		super(nameTableA, nameTableB, fileNameTableA, fileNameTableB, rowcountTableA, rowcountTableB);
	}

	public TestTableCreatorOracle() {
		super("TMP_TABLE_A", "TMP_TABLE_B", "TMP_TABLE_A_ORA.sql", "TMP_TABLE_B_ORA.sql", 5, 31);
	}


	
	



}
