package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class OracleConstraintManager extends ConstraintManager<OracleConstraint> {
	
	public OracleConstraintManager(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected List<OracleConstraint> loadForeignKeysFrom(String tableName, String columnName, String schema) {
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
	protected List<OracleConstraint> loadForeignKeysTo(String tableName, String columnName, String schema) {
		String SQL = "SELECT sourceColumn.table_name sourceTableName, sourceColumn.column_name sourceColumnName, sourceColumn.constraint_name constraintName,                              "+
		" targetConstraint.table_name targetTableName, targetCoumn.COLUMN_NAME targetColumnName                                                                               "+
		" FROM all_cons_columns sourceColumn                                                                                                                                  "+
		" JOIN all_constraints sourceConstraint ON sourceColumn.owner = sourceConstraint.owner AND sourceColumn.constraint_name = sourceConstraint.constraint_name            "+       
		" JOIN all_constraints targetConstraint ON sourceConstraint.r_owner = targetConstraint.owner AND sourceConstraint.r_constraint_name = targetConstraint.constraint_name"+       
		" JOIN all_cons_columns targetCoumn ON targetCoumn.owner = targetConstraint.owner AND targetCoumn.constraint_name = targetConstraint.constraint_name   and sourceColumn.POSITION = targetCoumn.POSITION               "+
		" WHERE sourceConstraint.constraint_type = 'R'  AND targetConstraint.table_name = ? and targetCoumn.COLUMN_NAME = ? and  targetCoumn.owner = ?                        ";

		return doLoad(tableName, columnName, schema, SQL);
	}

	protected List<OracleConstraint> doLoad(String tableName, String columnName, String schema, String sql_select) {
		List<OracleConstraint> constraints = jdbcTemplate.query(sql_select, new Object [] {tableName, columnName, schema}, 
		new RowMapper<OracleConstraint>(){
			@Override
			public OracleConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
				try {
					return new OracleConstraint(rs);
				} catch (Exception e) {
					logger.error("constraint loading failed", e);
					return null;
				}
			}
			
		});
		
		return constraints;
	}
}