package org.anon.vendor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.anon.AbstractDbConnection;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.data.RelatedTableColumnInfo.Relation;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class MySqlDbConnection extends AbstractDbConnection   {
	private Logger logger = Logger.getLogger(getClass());

	private static DatabaseSpecifics databaseSpecifics = DatabaseSpecifics.MySqlSpecific;
	
		
	public MySqlDbConnection(String defaultSchema) {
		super(defaultSchema);
	}


	@Override
	public List<DatabaseTableInfo> getTableNamesAndRowcounts(String selectedSchema) {
		String SQL = "select table_name as name, table_rows as rowcnt from INFORMATION_SCHEMA.TABLES where   table_schema = '" + selectedSchema + "' order by   table_name";
		return jdbcTemplate.query(SQL, new RowMapper<DatabaseTableInfo>(){

			@Override
			public DatabaseTableInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				DatabaseTableInfo databaseTableInfo = new DatabaseTableInfo();
				databaseTableInfo.setName(rs.getString("name"));
				databaseTableInfo.setRowCount(rs.getInt("rowcnt"));
				return databaseTableInfo;
			}});	}


	@Override
	public  List<DatabaseColumnInfo> getColumns(final DatabaseTableInfo  databaseTableInfo) {
		
		String SQL = "SELECT column_name, DATA_TYPE, IS_NULLABLE FROM information_schema.columns WHERE table_name = ? and table_schema = ?";
		return jdbcTemplate.query(SQL, new Object[]{databaseTableInfo.getName(), databaseTableInfo.getSchema()}, new RowMapper<DatabaseColumnInfo>(){

			@Override
			public DatabaseColumnInfo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				DatabaseColumnInfo col = new DatabaseColumnInfo(rs.getString("column_name"), rs.getString("DATA_TYPE"), "YES".equalsIgnoreCase(rs.getString("IS_NULLABLE")),  databaseSpecifics);
				return col;
			}
			
		});
	}

	@Override
	public void fillExampleValues(DatabaseTableInfo editedTable) {
		for(DatabaseColumnInfo column: editedTable.getColumns()){
			String sql = " select distinct " + column.getName() + " from " + editedTable.getSchema() + "." +editedTable.getName()+ " limit 5";
			if(column.isJavaTypeString()){
				List<String> values = jdbcTemplate.queryForList(sql, String.class);
				column.setExampleValues(values);
			}
			else if(column.isJavaTypeDate()){
				List<Date> values = jdbcTemplate.queryForList(sql, Date.class);
				column.setExampleValues(values);
			}
			else if(column.isJavaTypeLong() ){
				List<Long> values = jdbcTemplate.queryForList(sql, Long.class);
				column.setExampleValues(values);
			}
			else if(column.isJavaTypeDouble()){
				List<Double> values = jdbcTemplate.queryForList(sql, Double.class);
				column.setExampleValues(values);
			}
			else {
				logger.warn(column.getType() + " is not supported");
			}

		}
		
	}

	@Override
	protected Collection<RelatedTableColumnInfo> findRelatedTablesByName(DatabaseTableInfo editedTable, final DatabaseColumnInfo editedColumn) {
		String SQL =   " select t.table_name as table_name"+
				" from INFORMATION_SCHEMA.TABLES t "+ 
				" join INFORMATION_SCHEMA.COLUMNS c on c.TABLE_NAME = t.TABLE_NAME and c.TABLE_SCHEMA = t.TABLE_SCHEMA "+
				" where   c.COLUMN_NAME = ? and t.table_name != ? and  t.TABLE_SCHEMA = ?";
		return jdbcTemplate.query(SQL, new Object[]{editedColumn.getName(), editedTable.getName(), editedTable.getSchema()}, new RowMapper<RelatedTableColumnInfo>(){

			@Override
			public RelatedTableColumnInfo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return new RelatedTableColumnInfo(editedColumn.getName(), rs.getString("table_name"), Relation.SameColumnName);
			}
			
		});
		
	}
	
	@Override
	protected Collection<RelatedTableColumnInfo> findRelatedTablesByForeignKey(DatabaseTableInfo editedTable,
			DatabaseColumnInfo editedColumn) {
		String SQL = " select  TABLE_NAME as src_table, COLUMN_NAME as src_column,CONSTRAINT_NAME     "
				+ " from INFORMATION_SCHEMA.KEY_COLUMN_USAGE                                          "
				+ " where  (REFERENCED_TABLE_NAME = ? and REFERENCED_COLUMN_NAME = ?) ";
		return jdbcTemplate.query(SQL, new Object[] { editedTable.getName(), editedColumn.getName() },
				new RowMapper<RelatedTableColumnInfo>() {

					@Override
					public RelatedTableColumnInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
						RelatedTableColumnInfo info = new RelatedTableColumnInfo(rs.getString("src_column"), rs
								.getString("src_table"), Relation.ForeignKey);
						info.setRemark("Constraint Name: " + rs.getString("CONSTRAINT_NAME"));
						return info;
					}

				});
	}


	@Override
	public DatabaseSpecifics getDatabaseSpecifics() {
		return databaseSpecifics;
	}


	@Override
	public List<String> getSchemas() {
		return  jdbcTemplate.queryForList("select distinct(table_schema) from information_schema.tables", String.class);
	}


	@Override
	protected String[] getTestSufficientPermissionsScript(String selectedSchema) {
		String [] SCRIPT = {
				databaseSpecifics.getUseSchemaSql(selectedSchema),
				"create table TMP_TABLE_A(col1 varchar(50) not null,col2 varchar(50) not null,primary key(col1))",
				"create table TMP_TABLE_B(col1_ref varchar(50) not null,col2_ref varchar(50) not null,CONSTRAINT my_fk    FOREIGN KEY (col1_ref)    REFERENCES TMP_TABLE_A (col1) )",
				"alter table TMP_TABLE_B drop FOREIGN KEY my_fk",
				"drop table TMP_TABLE_B",
				"drop table TMP_TABLE_A"
				};
		
			return SCRIPT;

	}






}
