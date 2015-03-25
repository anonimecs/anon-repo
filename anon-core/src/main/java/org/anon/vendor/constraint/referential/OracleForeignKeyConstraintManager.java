package org.anon.vendor.constraint.referential;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class OracleForeignKeyConstraintManager extends ForeignKeyConstraintManager<OracleForeignKeyConstraint> {
	
	public OracleForeignKeyConstraintManager(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<OracleForeignKeyConstraint> loadForeignKeysFrom(String tableName, String columnName, String schema) {
		String SQL = "SELECT sourceColumn.table_name sourceTableName, sourceColumn.column_name sourceColumnName, sourceColumn.constraint_name constraintName,                              "+
		" targetConstraint.table_name targetTableName, targetCoumn.COLUMN_NAME targetColumnName                                                                               "+
		" FROM all_cons_columns sourceColumn                                                                                                                                  "+
		" JOIN all_constraints sourceConstraint ON sourceColumn.owner = sourceConstraint.owner AND sourceColumn.constraint_name = sourceConstraint.constraint_name            "+       
		" JOIN all_constraints targetConstraint ON sourceConstraint.r_owner = targetConstraint.owner AND sourceConstraint.r_constraint_name = targetConstraint.constraint_name"+       
		" JOIN all_cons_columns targetCoumn ON targetCoumn.owner = targetConstraint.owner AND targetCoumn.constraint_name = targetConstraint.constraint_name   and sourceColumn.POSITION = targetCoumn.POSITION               "+
		" WHERE sourceConstraint.constraint_type = 'R'  AND sourceConstraint.table_name = ? and sourceColumn.COLUMN_NAME = ? and  sourceColumn.owner = ?                        ";

		return doLoad(tableName, columnName, schema, SQL);
	}
	
	@Override
	public List<OracleForeignKeyConstraint> loadForeignKeysTo(String tableName, String columnName, String schema) {
		String SQL = "SELECT sourceColumn.table_name sourceTableName, sourceColumn.column_name sourceColumnName, sourceColumn.constraint_name constraintName,                              "+
		" targetConstraint.table_name targetTableName, targetCoumn.COLUMN_NAME targetColumnName                                                                               "+
		" FROM all_cons_columns sourceColumn                                                                                                                                  "+
		" JOIN all_constraints sourceConstraint ON sourceColumn.owner = sourceConstraint.owner AND sourceColumn.constraint_name = sourceConstraint.constraint_name            "+       
		" JOIN all_constraints targetConstraint ON sourceConstraint.r_owner = targetConstraint.owner AND sourceConstraint.r_constraint_name = targetConstraint.constraint_name"+       
		" JOIN all_cons_columns targetCoumn ON targetCoumn.owner = targetConstraint.owner AND targetCoumn.constraint_name = targetConstraint.constraint_name   and sourceColumn.POSITION = targetCoumn.POSITION               "+
		" WHERE sourceConstraint.constraint_type = 'R'  AND targetConstraint.table_name = ? and targetCoumn.COLUMN_NAME = ? and  targetCoumn.owner = ?                        ";

		return doLoad(tableName, columnName, schema, SQL);
	}

	protected List<OracleForeignKeyConstraint> doLoad(String tableName, String columnName, String schema, String sql_select) {
		List<OracleForeignKeyConstraint> constraints = jdbcTemplate.query(sql_select, new Object [] {tableName.toUpperCase(), columnName.toUpperCase(), schema}, 
		new RowMapper<OracleForeignKeyConstraint>(){
			@Override
			public OracleForeignKeyConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
				try {
					return new OracleForeignKeyConstraint(rs);
				} catch (Exception e) {
					logger.error("constraint loading failed", e);
					return null;
				}
			}
			
		});
		
		return constraints;
	}
}