package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class SqlServerConstraintManager extends ConstraintManager<SqlServerForeignKeyConstraint> {
	public SqlServerConstraintManager(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected List<SqlServerForeignKeyConstraint> loadForeignKeysFrom(String tableName, String columnName, String schema) {
		String SQL = " SELECT                                                             "+
		" column1.CONSTRAINT_NAME AS FK_CONSTRAINT_NAME                                           "+
		" ,column1.TABLE_NAME AS SOURCE_TABLE_NAME                                                "+
		" , column1.COLUMN_NAME AS SOURCE_COLUMN_NAME                                             "+
		" ,column2.TABLE_NAME AS TARGET_TABLE_NAME                                                "+
		" , column2.COLUMN_NAME AS TARGET_COLUMN_NAME                                             "+
		" FROM "+schema+".INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS AS RC                         "+     
		" INNER JOIN "+schema+".INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS column1                     "+        
		"     ON column1.CONSTRAINT_CATALOG = RC.CONSTRAINT_CATALOG                               "+
		"     AND column1.CONSTRAINT_SCHEMA = RC.CONSTRAINT_SCHEMA                                "+
		"     AND column1.CONSTRAINT_NAME = RC.CONSTRAINT_NAME                                    "+
		" INNER JOIN "+schema+".INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS column2                     "+        
		"     ON column2.CONSTRAINT_CATALOG = RC.UNIQUE_CONSTRAINT_CATALOG                        "+
		"     AND column2.CONSTRAINT_SCHEMA = RC.UNIQUE_CONSTRAINT_SCHEMA                         "+
		"     AND column2.CONSTRAINT_NAME = RC.UNIQUE_CONSTRAINT_NAME                             "+
		"     AND column2.ORDINAL_POSITION = column1.ORDINAL_POSITION                             "+ 
		" where column1.TABLE_NAME = ? and column1.COLUMN_NAME = ?";
		return doloadConstraints(SQL, tableName, columnName, schema);
	}
	
	@Override
	protected List<SqlServerForeignKeyConstraint> loadForeignKeysTo(String tableName, String columnName, String schema) {
		String SQL = " SELECT                                                             "+
		" column1.CONSTRAINT_NAME AS FK_CONSTRAINT_NAME                                           "+
		" ,column1.TABLE_NAME AS SOURCE_TABLE_NAME                                                "+
		" , column1.COLUMN_NAME AS SOURCE_COLUMN_NAME                                             "+
		" ,column2.TABLE_NAME AS TARGET_TABLE_NAME                                                "+
		" , column2.COLUMN_NAME AS TARGET_COLUMN_NAME                                             "+
		" FROM "+schema+".INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS AS RC                         "+     
		" INNER JOIN "+schema+".INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS column1                     "+        
		"     ON column1.CONSTRAINT_CATALOG = RC.CONSTRAINT_CATALOG                               "+
		"     AND column1.CONSTRAINT_SCHEMA = RC.CONSTRAINT_SCHEMA                                "+
		"     AND column1.CONSTRAINT_NAME = RC.CONSTRAINT_NAME                                    "+
		" INNER JOIN "+schema+".INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS column2                     "+        
		"     ON column2.CONSTRAINT_CATALOG = RC.UNIQUE_CONSTRAINT_CATALOG                        "+
		"     AND column2.CONSTRAINT_SCHEMA = RC.UNIQUE_CONSTRAINT_SCHEMA                         "+
		"     AND column2.CONSTRAINT_NAME = RC.UNIQUE_CONSTRAINT_NAME                             "+
		"     AND column2.ORDINAL_POSITION = column1.ORDINAL_POSITION                             "+ 
		" where column2.TABLE_NAME = ? and column2.COLUMN_NAME = ? ";
		return doloadConstraints(SQL, tableName, columnName, schema);	}
	

	private List<SqlServerForeignKeyConstraint> doloadConstraints(String sql, String tableName, String columnName, String schema) {

		List<SqlServerForeignKeyConstraint> constraints = jdbcTemplate.query(sql, new Object [] {tableName, columnName}, 
				new RowMapper<SqlServerForeignKeyConstraint>(){
					@Override
					public SqlServerForeignKeyConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
						try {
							return new SqlServerForeignKeyConstraint(rs);
						} catch (Exception e) {
							logger.error("deactivateConstraints failed", e);
							return null;
						}
					}
					
				});
				
				return constraints;
	}
}