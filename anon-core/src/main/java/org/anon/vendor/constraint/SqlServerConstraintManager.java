package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class SqlServerConstraintManager extends ConstraintManager {
	public SqlServerConstraintManager(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<SqlServerConstraint> loadConstraints(String tableName, String schema) {
		String sql_select = "SELECT       KCU1.CONSTRAINT_NAME AS FK_CONSTRAINT_NAME                     "+ 
					 " ,KCU1.TABLE_NAME AS SOURCE_TABLE_NAME 											 "+
					 " , KCU1.COLUMN_NAME AS SOURCE_COLUMN_NAME											 "+
					 " FROM "+schema+".INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS AS RC                             " +
					 " INNER JOIN "+schema+".INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KCU1                            " +
					 "     ON KCU1.CONSTRAINT_CATALOG = RC.CONSTRAINT_CATALOG                            " +
					 "     AND KCU1.CONSTRAINT_SCHEMA = RC.CONSTRAINT_SCHEMA                             " +
					 "     AND KCU1.CONSTRAINT_NAME = RC.CONSTRAINT_NAME                                 " +
					 " INNER JOIN "+schema+".INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KCU2                            " +
					 "     ON KCU2.CONSTRAINT_CATALOG = RC.UNIQUE_CONSTRAINT_CATALOG                     " +
					 "     AND KCU2.CONSTRAINT_SCHEMA = RC.UNIQUE_CONSTRAINT_SCHEMA                      " +
					 "     AND KCU2.CONSTRAINT_NAME = RC.UNIQUE_CONSTRAINT_NAME                          " +
					 "     AND KCU2.ORDINAL_POSITION = KCU1.ORDINAL_POSITION                             " +
					 " where (KCU1.TABLE_NAME = ?) " +
					 " or (KCU2.TABLE_NAME = ?) ";

		List<SqlServerConstraint> constraints = jdbcTemplate.query(sql_select, new Object [] {tableName, tableName}, 
				new RowMapper<SqlServerConstraint>(){
					@Override
					public SqlServerConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
						try {
							return new SqlServerConstraint(rs);
						} catch (Exception e) {
							logger.error("deactivateConstraints failed", e);
							return null;
						}
					}
					
				});
				
				return constraints;
	}
}