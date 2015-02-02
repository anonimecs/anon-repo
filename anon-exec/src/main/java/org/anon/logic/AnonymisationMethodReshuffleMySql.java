package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.ExecutionMessage;

public class AnonymisationMethodReshuffleMySql extends AnonymisationMethodReshuffle {
	
	public static final String SHUFFLE_RAN = "TMP_SHUFFLE_1";
	public static final String SHUFFLE_ORG = "TMP_SHUFFLE_2";

	
	@Override
	public void setupInDb() {
		
		cleanupInDb();
		
		String createTmpShuffleRandom =
				"CREATE TEMPORARY TABLE " + SHUFFLE_RAN + " ( " +
				"id INT(11) NOT NULL AUTO_INCREMENT, " +
				"shuffle_values VARCHAR(255), " +
				"PRIMARY KEY (id), " +
				"KEY shuffle_values(shuffle_values))";
		
		execute(createTmpShuffleRandom);
		
		String insertTmpShuffleRandom =
				"INSERT INTO " + SHUFFLE_RAN + " (id, shuffle_values) SELECT NULL, COLCOL FROM " +
				"( " + createUnionSelectForColumns() + ") AAA " +
				"ORDER BY rand(" + hashmodint + ")";
		
		execute(insertTmpShuffleRandom);
		
		String createTmpShuffleOrg =
				"CREATE TEMPORARY TABLE " + SHUFFLE_ORG + " AS " +
				"SELECT @rownum := @rownum + 1 AS rownumber, COLCOL as original_values " +
				"FROM" +
				"(select @rownum := 0) R, " +
				"(" + createUnionSelectForColumns() + ") AAA " +
				"ORDER BY rownumber";
		
		execute(createTmpShuffleOrg);
		
		logger.debug("AnonymisationMethodReshuffleMySql.setupInDb() complete");
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
		execute("DROP TEMPORARY TABLE IF EXISTS " + SHUFFLE_ORG + "");
		execute("DROP TEMPORARY TABLE IF EXISTS " + SHUFFLE_RAN + "");
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
				"WHERE " + tableName + "." + colName + "=" + SHUFFLE_ORG + ".original_values)";
		
		int rowCount = update(sql);
		return new ExecutionMessage("Reshuffled Rows", rowCount);
	}
}
