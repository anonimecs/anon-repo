package org.anon.vendor.constraint.unique;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class MySqlUniqueConstraintManager extends UniqueConstraintManager<UniqueConstraint> {
	

	private static final String CONSTRAINT_TYPE_UNIQUE = "UNIQUE";
	private static final String CONSTRAINT_TYPE_PK = "PRIMARY KEY";

	public MySqlUniqueConstraintManager(DataSource dataSource) {
		super(dataSource);
	}



	@Override
	public List<UniqueConstraint> loadUniques(String tableName, String columnName, String schemaName) {
		List<UniqueConstraint> columnConstraints = new ArrayList<>();
		for(UniqueConstraint constraint:loadTableConstraints(tableName, schemaName, CONSTRAINT_TYPE_UNIQUE,new UniqueMapper(tableName))){
			if(constraint.containsColumn(columnName)){
				columnConstraints.add(constraint);
			}
		}
		return columnConstraints;
	}
	
	@Override
	public List<UniqueConstraint> loadPrimaryKeys(String tableName, String columnName, String schemaName) {
		List<UniqueConstraint> columnConstraints = new ArrayList<>();
		for(UniqueConstraint constraint:loadTableConstraints(tableName, schemaName, CONSTRAINT_TYPE_PK,new PkMapper(tableName))){
			if(constraint.containsColumn(columnName)){
				columnConstraints.add(constraint);
			}
		}
		return columnConstraints;
	}
	
	public List<UniqueConstraint> loadTableConstraints(final String tableName, String schemaName, String constraintType, RowMapper<UniqueConstraint> mapper) {
		String sql_select = "select GROUP_CONCAT(cu.COLUMN_NAME)  as COLUMN_NAMES, tc.CONSTRAINT_NAME from "+
							" information_schema.table_constraints tc "+
							" join information_schema.key_column_usage cu on tc.CONSTRAINT_SCHEMA = cu.CONSTRAINT_SCHEMA and tc.CONSTRAINT_CATALOG = cu.CONSTRAINT_CATALOG  "+
							" and tc.CONSTRAINT_NAME = cu.CONSTRAINT_NAME and tc.TABLE_SCHEMA = cu.TABLE_SCHEMA and tc.TABLE_NAME = cu.TABLE_NAME "+
							" where  tc.CONSTRAINT_TYPE = ? and tc.CONSTRAINT_SCHEMA =  ? and tc.TABLE_NAME=? group by tc.CONSTRAINT_NAME";
		
		List<UniqueConstraint> allTableUniqeConstraints = jdbcTemplate.query(sql_select, new Object [] {constraintType, schemaName, tableName}, mapper);
		
		return allTableUniqeConstraints;


	}

	public class UniqueMapper implements RowMapper<UniqueConstraint> {
		private final String tableName;

		public UniqueMapper(String tableName) {
			this.tableName = tableName;
		}

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
	}

	public class PkMapper implements RowMapper<UniqueConstraint> {
		private final String tableName;

		public PkMapper(String tableName) {
			this.tableName = tableName;
		}

		@Override
		public MySqlPrimaryKeyConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				MySqlPrimaryKeyConstraint constraint = new MySqlPrimaryKeyConstraint(rs, tableName);
				return constraint;
			} catch (Exception e) {
				logger.error("deactivateConstraints failed", e);
				return null;
			}
		}
	}

	

}