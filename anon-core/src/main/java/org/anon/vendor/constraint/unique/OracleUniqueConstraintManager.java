package org.anon.vendor.constraint.unique;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class OracleUniqueConstraintManager extends UniqueConstraintManager<UniqueConstraint> {
	

	private static final String CONSTRAINT_TYPE_UNIQUE = "U";
	private static final String CONSTRAINT_TYPE_PK = "P";

	public OracleUniqueConstraintManager(DataSource dataSource) {
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
		String sql_select = "select listagg(cols.COLUMN_NAME, ',') WITHIN GROUP (ORDER BY cols.COLUMN_NAME) as COLUMN_NAMES, cons.CONSTRAINT_NAME "+
							" from ALL_CONSTRAINTS cons  "+
							" join ALL_CONS_COLUMNS cols on cons.OWNER = cols.OWNER and cons.CONSTRAINT_NAME = cols.CONSTRAINT_NAME "+
							" where cons.CONSTRAINT_TYPE = ? and cons.OWNER = ? and cons.TABLE_NAME = ? group by cons.CONSTRAINT_NAME";
		
		List<UniqueConstraint> allTableUniqeConstraints = jdbcTemplate.query(sql_select, new Object [] {constraintType, schemaName, tableName}, mapper);
		
		return allTableUniqeConstraints;


	}

	public class UniqueMapper implements RowMapper<UniqueConstraint> {
		private final String tableName;

		public UniqueMapper(String tableName) {
			this.tableName = tableName;
		}

		@Override
		public OracleUniqueConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				OracleUniqueConstraint constraint = new OracleUniqueConstraint(rs, tableName);
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
		public OraclePrimaryKeyConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				OraclePrimaryKeyConstraint constraint = new OraclePrimaryKeyConstraint(rs, tableName);
				return constraint;
			} catch (Exception e) {
				logger.error("deactivateConstraints failed", e);
				return null;
			}
		}
	}

	

}