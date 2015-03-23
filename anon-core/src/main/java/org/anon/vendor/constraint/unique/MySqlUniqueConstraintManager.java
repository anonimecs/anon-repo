package org.anon.vendor.constraint.unique;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class MySqlUniqueConstraintManager extends UniqueConstraintManager<MySqlUniqueConstraint> {
	
	public MySqlUniqueConstraintManager(DataSource dataSource) {
		super(dataSource);
	}



	@Override
	public List<MySqlUniqueConstraint> loadConstraints(String tableName, String columnName, String schemaName) {
		List<MySqlUniqueConstraint> columnConstraints = new ArrayList<>();
		for(MySqlUniqueConstraint constraint:loadTableConstraints(tableName, schemaName)){
			if(constraint.containsColumn(columnName)){
				columnConstraints.add(constraint);
			}
		}
		return columnConstraints;
	}
	
	public List<MySqlUniqueConstraint> loadTableConstraints(final String tableName, String schemaName) {
		String sql_select = "select GROUP_CONCAT(cu.COLUMN_NAME)  as COLUMN_NAMES, tc.CONSTRAINT_NAME from "+
							" information_schema.table_constraints tc "+
							" join information_schema.key_column_usage cu on tc.CONSTRAINT_SCHEMA = cu.CONSTRAINT_SCHEMA and tc.CONSTRAINT_CATALOG = cu.CONSTRAINT_CATALOG  "+
							" and tc.CONSTRAINT_NAME = cu.CONSTRAINT_NAME and tc.TABLE_SCHEMA = cu.TABLE_SCHEMA and tc.TABLE_NAME = cu.TABLE_NAME "+
							" where  tc.CONSTRAINT_TYPE = 'UNIQUE' and tc.CONSTRAINT_SCHEMA =  ? and tc.TABLE_NAME=? group by tc.CONSTRAINT_NAME";
		
		List<MySqlUniqueConstraint> allTableUniqeConstraints = jdbcTemplate.query(sql_select, new Object [] {schemaName, tableName}, 
		new RowMapper<MySqlUniqueConstraint>(){
			@Override
			public MySqlUniqueConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
				try {
					MySqlUniqueConstraint constraint = new MySqlUniqueConstraint(rs, tableName);
					return constraint;
				} catch (Exception e) {
					logger.error("deactivateConstraints failed", e);
					return null;
				}
			}
			
		});
		
		return allTableUniqeConstraints;


	}

	

}