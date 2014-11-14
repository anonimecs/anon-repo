package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.RunResult;
import org.springframework.jdbc.core.JdbcTemplate;

public class AnonymisationMethodReshuffleOracle extends AnonymisationMethodReshuffle {

	public static final String TMP_TABLE = "TMP_TABLE"; 
	public static final String LOOKUP_TABLE = "TMP_TABLE_LOOKUP"; 
	

	
	@Override
	public void setupInDb() {

		String createTmpTableSql =
				" create table " + TMP_TABLE + " as " +
				" select ROWNUM as MYROWNUMBER, COLCOL as orig, COLCOL as reshuffled        " +
				" from ( ";

		for(int i =0 ; i < applyedToColumns.size(); i++){
			AnonymisedColumnInfo anonymisedColumnInfo = applyedToColumns.get(i);
			String subselect = "select distinct " + anonymisedColumnInfo.getName() + " as COLCOL from  " + anonymisedColumnInfo.getTable().getName();
			createTmpTableSql += subselect;
			if( i < (applyedToColumns.size() - 1)){
				createTmpTableSql += " union ";
			}
		}
		
		createTmpTableSql = createTmpTableSql + 
				" ) AAA                                                                                      " +
				" order by 2  ";

		
		String createTmpTableIndexSql = "create index " +TMP_TABLE+ "_IND on " + TMP_TABLE + " (MYROWNUMBER)";
		String createLookupTableIndexSql = "create index " +LOOKUP_TABLE+ "_IND on " + LOOKUP_TABLE + " (orig)";

		
		execute(createTmpTableSql);
		execute(createTmpTableIndexSql);
		
		@SuppressWarnings("deprecation")
		int rowCount = new JdbcTemplate(dataSource).queryForInt("SELECT COUNT(*)  FROM " + TMP_TABLE);
		int shift = rowCount / 4  + hashmodint;

		String reshuffleToLookupTable =
				" create table " + LOOKUP_TABLE + " as " + 
				" select MYROWNUMBER,orig,(	SELECT orig                            		"+
					" FROM "+TMP_TABLE+" a                                     		"+
					" WHERE                                                    		"+
					"	a.MYROWNUMBER = mod(x.MYROWNUMBER + "+shift+", "+rowCount+") + 1) as reshuffled  "+
					" from " + TMP_TABLE+" x                ";

		
		execute(reshuffleToLookupTable);
		execute(createLookupTableIndexSql);
				
	}
	
	@Override
	public void cleanupInDb() {
		execute("drop table " + TMP_TABLE + "");
		execute("drop table " + LOOKUP_TABLE + "");
	}
	
	@Override
	public RunResult runOnColumn(AnonymisedColumnInfo col) {
		String colName = col.getName();
		String tableName = col.getTable().getName();
		String sql =
				" update " + tableName + " x "		   +
				" set " +colName+" = (                   "+
				" select s.reshuffled                    "+
				" from                                   "+
				" " + LOOKUP_TABLE + " s                   "+
				" where s.orig = x."+colName			  +
				" )                                      "
				;
		int rowCount = update(sql);
		return new RunResult("Reshuffled Rows", rowCount);
	}
	
	
}
