package org.anon.vendor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.anon.AbstractDbConnection;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.data.RelatedTableColumnInfo.Relation;
import org.springframework.jdbc.core.RowMapper;

public class SybaseDbConnection extends AbstractDbConnection {


	private static DatabaseSpecifics databaseSpecifics = DatabaseSpecifics.SybaseSpecific;

	public SybaseDbConnection(String defaultSchema) {
		super(defaultSchema);
	}
	

	@Override
	public  List<DatabaseColumnInfo> getColumns(final DatabaseTableInfo  databaseTableInfo) {
		String SQL = "select cols.name as columnname, types.name as columntype                              "+
					" from " + databaseTableInfo.getSchema() + "..syscolumns as cols, " + databaseTableInfo.getSchema() + "..sysobjects as objs, " + databaseTableInfo.getSchema() + "..systypes as types   "+
					" where cols.id = objs.id                                                               "+
					" and cols.usertype = types.usertype                                                    "+
					" and objs.type='U'                                                                     "+
					" and objs.name=?";
		
		//String SQL = "select syscolumns.name as columnname, systypes.name as columntype from syscolumns, sysobjects, systypes where syscolumns.id = sysobjects.id and syscolumns.usertype = systypes.usertype and sysobjects.type='U' and sysobjects.name = ?";
		return jdbcTemplate.query(SQL, new Object[]{databaseTableInfo.getName()}, new RowMapper<DatabaseColumnInfo>(){

			@Override
			public DatabaseColumnInfo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				DatabaseColumnInfo col = new DatabaseColumnInfo(rs.getString("columnname"), rs.getString("columntype"), databaseSpecifics);
				col.setTable(databaseTableInfo);
				return col;
			}
			
		});
	}

	@Override
	public List<DatabaseTableInfo> getTableNamesAndRowcounts(String selectedSchema) {
		String SQL = "select ob.name,st.rowcnt from " + selectedSchema + "..sysobjects ob, " + selectedSchema + "..systabstats st where ob.type='U' and st.id=ob.id and indid = 0 order by ob.name";
		return jdbcTemplate.query(SQL, new RowMapper<DatabaseTableInfo>(){

			@Override
			public DatabaseTableInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				DatabaseTableInfo databaseTableInfo = new DatabaseTableInfo();
				databaseTableInfo.setName(rs.getString("name"));
				databaseTableInfo.setRowCount(rs.getInt("rowcnt"));
				return databaseTableInfo;
			}});
		
	}

	@Override
	public void fillExampleValues(DatabaseTableInfo editedTable) {
		for(DatabaseColumnInfo column: editedTable.getColumns()){
			String sql = "select distinct top 5 " + column.getName() + " from " + editedTable.getSchema() + ".." + editedTable.getName();
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
	protected Collection<RelatedTableColumnInfo> findRelatedTablesByForeignKey(DatabaseTableInfo editedTable,
			DatabaseColumnInfo editedColumn) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected Collection<RelatedTableColumnInfo> findRelatedTablesByName(DatabaseTableInfo editedTable,
			final DatabaseColumnInfo editedColumn) {
		String schema = editedTable.getSchema(); 
		String SQL = "select objs.name as tablename, typs.name columntype " +
				" from "+schema+"..syscolumns as cols, "+schema+"..sysobjects as objs, "+schema+"..systypes as typs"+
				" where cols.id = objs.id and cols.usertype = typs.usertype and objs.type='U'"+
				" and cols.name = ? and objs.name != ?";
		return jdbcTemplate.query(SQL, new Object[]{editedColumn.getName(), editedTable.getName()}, new RowMapper<RelatedTableColumnInfo>(){

			@Override
			public RelatedTableColumnInfo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return new RelatedTableColumnInfo(editedColumn.getName(), rs.getString("tablename"), Relation.SameColumnName);
			}
			
		});
	}


	@Override
	public DatabaseSpecifics getDatabaseSpecifics() {
		return databaseSpecifics;
	}

	@Override
	public List<String> getSchemas() {
		List<String> res = jdbcTemplate.query("sp_helpdb", new RowMapper<String>(){

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
			
		});
		
		Iterator<String> iter = res.iterator();
		while(iter.hasNext()){
			String schema = iter.next();
			if(isSystemSchema(schema)){
				iter.remove();
			}
		}
		
		return res;
		
	}

	private boolean isSystemSchema(String schema) {
		if(schema.startsWith("temp")){
			return true;
		}
		if(schema.startsWith("syb")){
			return true;
		}
		if(schema.equalsIgnoreCase("master")){
			return true;
		}
		if(schema.equalsIgnoreCase("model")){
			return true;
		}
		if(schema.equalsIgnoreCase("auditdb")){
			return true;
		}
		if(schema.equalsIgnoreCase("dbccdb")){
			return true;
		}
		
		return false;
	}


	@Override
	public String []  getTestSufficientPermissionsScript(String selectedSchema) {
		String [] SCRIPT = {
				"use " + selectedSchema,
				"create table TMP_TABLE_A(col1 varchar(50) not null,col2 varchar(50) not null,primary key(col1))",
				"create table TMP_TABLE_B(col1_ref varchar(50) not null,col2_ref varchar(50) not null,CONSTRAINT my_fk    FOREIGN KEY (col1_ref)    REFERENCES TMP_TABLE_A (col1) )",
				"alter table TMP_TABLE_B drop constraint my_fk",
				"drop table TMP_TABLE_B",
				"drop table TMP_TABLE_A"
				};
		
		
			return SCRIPT;

	}




}
