package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.ExecutionMessage;
import org.springframework.jdbc.BadSqlGrammarException;

public class AnonymisationMethodReshuffleOracle extends AnonymisationMethodReshuffle {

	private static final long serialVersionUID = 1L;
	
	public static final String SHUFFLE_RAN = "TMP_SHUFFLE_1";
	public static final String SHUFFLE_ORG = "TMP_SHUFFLE_2";
	
	
	@Override
	public void setupInDb() {
		
		cleanupInDb();

		String createTmpShuffleRandom =
				"CREATE TABLE " + SHUFFLE_RAN + " (" +
				"id NUMBER(11) NOT NULL," +
				"shuffle_values VARCHAR(255))";
		
		execute(createTmpShuffleRandom);
		
		String alterShuffleRandomTable =
				"ALTER TABLE " + SHUFFLE_RAN + " ADD (" +
				"CONSTRAINT " + SHUFFLE_RAN + "_pk PRIMARY KEY(id))";
		
		execute(alterShuffleRandomTable);
		
		String createShuffleSequence = 
				"CREATE SEQUENCE " + SHUFFLE_RAN + "_seq";
		
		execute(createShuffleSequence);
		
		String createShuffleSeqTrigger = 
				"CREATE OR REPLACE TRIGGER " + SHUFFLE_RAN + "_bir " + 
				"BEFORE INSERT ON " + SHUFFLE_RAN + " " +
				"FOR EACH ROW " +
				"BEGIN " +
				"  SELECT " + SHUFFLE_RAN + "_seq.NEXTVAL " +
				"  INTO :new.id " +
				"  FROM dual;" +
				"END;";
		
		execute(createShuffleSeqTrigger);
		
		String insertTmpShuffleRandom =
				"INSERT INTO "+ SHUFFLE_RAN +" (shuffle_values) SELECT COLCOL FROM " +
				"( " + createUnionSelectForColumns() + ") " +
				"ORDER BY dbms_random.value";
		
		execute(insertTmpShuffleRandom);
		
		String createTmpShuffleOrg =
				"CREATE TABLE " + SHUFFLE_ORG + " AS " +
				"SELECT ROWNUM as ROWNUMBER, COLCOL as orginal_values FROM " +
				"( " + createUnionSelectForColumns() + ") AAA " +
				"ORDER BY ROWNUM";
		
		execute(createTmpShuffleOrg);
		
		logger.debug("AnonymisationMethodReshuffleOracle.setupInDb() complete");
	}
	
	private String createUnionSelectForColumns() {
		
		StringBuilder builder = new StringBuilder("");
		
		for(int i =0 ; i < applyedToColumns.size(); i++){
			AnonymisedColumnInfo column = applyedToColumns.get(i);
			String subselect = "select distinct " + column.getName() + " as COLCOL from " + column.getTable().getName();
			builder.append(subselect);
			if( i < (applyedToColumns.size() - 1)){
				builder.append(" union ");
			}
		}
		return builder.toString();
	}

	@Override
	public void cleanupInDb() {
		try {
			execute("drop table " + SHUFFLE_RAN + "");
		} catch (BadSqlGrammarException e) {
			logger.debug(SHUFFLE_RAN + " not exists.");
		}
		try {
			execute("drop table " + SHUFFLE_ORG + "");
		} catch (BadSqlGrammarException e) {
			logger.debug(SHUFFLE_ORG + " not exists.");
		}
		try {
			execute("drop sequence " + SHUFFLE_RAN + "_seq");
		} catch (BadSqlGrammarException e) {
			logger.debug(SHUFFLE_RAN + "_seq not exists.");
		}
	}
	
	@Override
	public ExecutionMessage runOnColumn(AnonymisedColumnInfo col) {
		String colName = col.getName();
		String tableName = col.getTable().getName();
		String sql =
				"UPDATE " + tableName + " " +
				"SET " + tableName + "." + colName + "=" +
				"(SELECT " + SHUFFLE_RAN + ".shuffle_values " +
				"FROM " + SHUFFLE_RAN + " " +
				"JOIN " + SHUFFLE_ORG + " ON " + SHUFFLE_ORG + ".rownumber=" + SHUFFLE_RAN + ".id " +
				"WHERE " + tableName + "." + colName + "=" + SHUFFLE_ORG + ".orginal_values)";
				
		int rowCount = update(sql);
		return new ExecutionMessage("Reshuffled Rows", rowCount);
	}
	
	
}
