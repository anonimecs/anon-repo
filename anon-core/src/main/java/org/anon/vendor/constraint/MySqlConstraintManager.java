package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class MySqlConstraintManager extends ConstraintManager<MySqlForeignKeyConstraint> {
	public MySqlConstraintManager(DataSource dataSource) {
		super(dataSource);
	}



	protected List<MySqlForeignKeyConstraint> doLoad(String tableName, String columnName, String schema, String sql_select) {
		List<MySqlForeignKeyConstraint> constraints = jdbcTemplate.query(sql_select, new Object [] {tableName, columnName, schema}, 
		new RowMapper<MySqlForeignKeyConstraint>(){
			@Override
			public MySqlForeignKeyConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
				try {
					MySqlForeignKeyConstraint constraint = new MySqlForeignKeyConstraint(rs);
					return constraint.getSourceTableName() != null ? constraint : null;
				} catch (Exception e) {
					logger.error("deactivateConstraints failed", e);
					return null;
				}
			}
			
		});
		
		return constraints;
	}
	
	@Override
	protected List<MySqlForeignKeyConstraint> loadForeignKeysFrom(String tableName, String columnName, String schema) {
		String sql_select = "select  TABLE_NAME, GROUP_CONCAT(COLUMN_NAME) as sourceColumns,CONSTRAINT_NAME, REFERENCED_TABLE_NAME, GROUP_CONCAT(REFERENCED_COLUMN_NAME) as targetColumns" +
				" from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
				"  where  TABLE_NAME = ? and COLUMN_NAME = ? and REFERENCED_TABLE_NAME is not null and CONSTRAINT_SCHEMA = ? " +
				" group by TABLE_NAME " +
				" order by CONSTRAINT_NAME";
		return doLoad(tableName, columnName, schema, sql_select);
	}
	
	@Override
	protected List<MySqlForeignKeyConstraint> loadForeignKeysTo(String tableName, String columnName, String schema) {
		String sql_select = "select  TABLE_NAME, GROUP_CONCAT(COLUMN_NAME) as sourceColumns,CONSTRAINT_NAME, REFERENCED_TABLE_NAME, GROUP_CONCAT(REFERENCED_COLUMN_NAME) as targetColumns" +
				" from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
				"  where  REFERENCED_TABLE_NAME = ? and REFERENCED_COLUMN_NAME = ? and CONSTRAINT_SCHEMA = ? " +
				" group by TABLE_NAME " +
				" order by CONSTRAINT_NAME";
		return doLoad(tableName, columnName, schema, sql_select);
	}
}