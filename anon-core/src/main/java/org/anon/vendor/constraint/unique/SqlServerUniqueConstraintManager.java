package org.anon.vendor.constraint.unique;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class SqlServerUniqueConstraintManager extends UniqueConstraintManager<UniqueConstraint> {
	

	private static final String CONSTRAINT_TYPE_UNIQUE = "UNIQUE";
	private static final String CONSTRAINT_TYPE_PK = "PRIMARY KEY";

	public SqlServerUniqueConstraintManager(DataSource dataSource) {
		super(dataSource);
	}



	@Override
	public List<UniqueConstraint> loadUniques(String tableName, String columnName, String schemaName) {
		List<UniqueConstraint> tableConstraints = loadTableConstraints(tableName, schemaName, CONSTRAINT_TYPE_UNIQUE,new UniqueMapper(tableName));
		return filterColumnConstraints(columnName, tableConstraints);
	}
	
	@Override
	public List<UniqueConstraint> loadPrimaryKeys(String tableName, String columnName, String schemaName) {
		List<UniqueConstraint> tableConstraints = loadTableConstraints(tableName, schemaName, CONSTRAINT_TYPE_PK,new PkMapper(tableName));
		return filterColumnConstraints(columnName, tableConstraints);

	}
	
	public List<UniqueConstraint> loadTableConstraints(final String tableName, String schemaName, String constraintType, RowMapper<UniqueConstraint> mapper) {
		String sql_select = "select con.CONSTRAINT_NAME, substring(        ( "+
            " Select ','+col.COLUMN_NAME  AS [text()] "+
            " From INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE col  "+
            " Where con.CONSTRAINT_NAME = col.CONSTRAINT_NAME and con.TABLE_CATALOG=col.TABLE_CATALOG and con.TABLE_SCHEMA = col.TABLE_SCHEMA "+
            " For XML PATH ('') "+
			" ), 2, 1000) [CONS_COLUMNS] "+
			"from INFORMATION_SCHEMA.TABLE_CONSTRAINTS con "+
		" where con.TABLE_CATALOG = ? and con.TABLE_NAME=? and con.CONSTRAINT_TYPE=?";
		
		List<UniqueConstraint> allTableUniqeConstraints = jdbcTemplate.query(sql_select, new Object [] {schemaName, tableName, constraintType}, mapper);
		
		return allTableUniqeConstraints;


	}

	public class UniqueMapper implements RowMapper<UniqueConstraint> {
		private final String tableName;

		public UniqueMapper(String tableName) {
			this.tableName = tableName;
		}

		@Override
		public SqlServerUniqueConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				SqlServerUniqueConstraint constraint = new SqlServerUniqueConstraint(rs, tableName);
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
		public SqlServerPrimaryKeyConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				SqlServerPrimaryKeyConstraint constraint = new SqlServerPrimaryKeyConstraint(rs, tableName);
				return constraint;
			} catch (Exception e) {
				logger.error("deactivateConstraints failed", e);
				return null;
			}
		}
	}

	

}