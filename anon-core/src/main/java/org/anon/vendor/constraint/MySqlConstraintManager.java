package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class MySqlConstraintManager extends ConstraintManager<MySqlConstraint> {
	public MySqlConstraintManager(DataSource dataSource) {
		super(dataSource);
	}



	protected List<MySqlConstraint> doLoad(String tableName, String columnName, String schema, String sql_select) {
		List<MySqlConstraint> constraints = jdbcTemplate.query(sql_select, new Object [] {tableName, columnName, schema}, 
		new RowMapper<MySqlConstraint>(){
			@Override
			public MySqlConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
				try {
					MySqlConstraint constraint = new MySqlConstraint(rs);
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
	protected List<MySqlConstraint> loadForeignKeysFrom(String tableName, String columnName, String schema) {
		String sql_select = "select  TABLE_NAME, GROUP_CONCAT(COLUMN_NAME) as sourceColumns,CONSTRAINT_NAME, REFERENCED_TABLE_NAME, GROUP_CONCAT(REFERENCED_COLUMN_NAME) as targetColumns" +
				" from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
				"  where  TABLE_NAME = ? and COLUMN_NAME = ? and REFERENCED_TABLE_NAME is not null and CONSTRAINT_SCHEMA = ? " +
				" group by TABLE_NAME " +
				" order by CONSTRAINT_NAME";
		return doLoad(tableName, columnName, schema, sql_select);
	}
	
	@Override
	protected List<MySqlConstraint> loadForeignKeysTo(String tableName, String columnName, String schema) {
		String sql_select = "select  TABLE_NAME, GROUP_CONCAT(COLUMN_NAME) as sourceColumns,CONSTRAINT_NAME, REFERENCED_TABLE_NAME, GROUP_CONCAT(REFERENCED_COLUMN_NAME) as targetColumns" +
				" from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
				"  where  REFERENCED_TABLE_NAME = ? and COLUMN_NAME = ? and CONSTRAINT_SCHEMA = ? " +
				" group by TABLE_NAME " +
				" order by CONSTRAINT_NAME";
		return doLoad(tableName, columnName, schema, sql_select);
	}
}