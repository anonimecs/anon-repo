package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.ExecutionMessage;

public class AnonymisationMethodReshuffleSybase extends AnonymisationMethodReshuffle {

	private static final long serialVersionUID = 1L;
	
	public static final String SHUFFLE_RAN = "TMP_SHUFFLE_1";
	public static final String SHUFFLE_ORG = "TMP_SHUFFLE_2";
	
	@Override
	public void setupInDb() {

		String createTmpShuffleRandom =
				"CREATE TABLE " + SHUFFLE_RAN + " ( " +
				"id INTEGER IDENTITY, " +
				"shuffle_values VARCHAR(255), " +
				"CONSTRAINT shuffle_values UNIQUE (shuffle_values))";
		
		execute(createTmpShuffleRandom);
		
		String insertTmpShuffleRandom = 
				"INSERT INTO " + SHUFFLE_RAN +" (shuffle_values) SELECT COLCOL FROM " +
				" (" + createUnionSelectForColumns() + ") AAA " +
				"ORDER BY rand2(" + hashmodint + ")";
		
		execute(insertTmpShuffleRandom);
		
		String createTmpShuffleOrg =
				"CREATE TABLE " + SHUFFLE_ORG + " ( " +
				"id INTEGER IDENTITY, " +
				"orginal_values VARCHAR(255), " +
				"CONSTRAINT orginal_values UNIQUE (orginal_values))";
		
		execute(createTmpShuffleOrg);
		
		String insertTmpShuffleOrg = 
				"INSERT INTO " + SHUFFLE_ORG +" (orginal_values) SELECT COLCOL FROM " +
				" (" + createUnionSelectForColumns() + ") AAA";
		
		execute(insertTmpShuffleOrg);
				
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
		dropTableIdExists(SHUFFLE_ORG);
		dropTableIdExists(SHUFFLE_RAN);
	}
	
	private void dropTableIdExists(String table) {
		String dropSql = 
				"IF EXISTS ( SELECT 1 FROM sysobjects WHERE name = '" + table + "' AND type = 'U' ) " +
				"EXECUTE('DROP TABLE "+ table +"')";
		
		execute(dropSql);
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
				"JOIN " + SHUFFLE_ORG + " ON " + SHUFFLE_ORG + ".id =" + SHUFFLE_RAN + ".id " +
				"WHERE " + tableName + "." + colName + "=" + SHUFFLE_ORG + ".original_values)";
		
		int rowCount = update(sql);
		return new ExecutionMessage("Reshuffled Rows", rowCount);
	}
}
