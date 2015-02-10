package org.anon.exec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.exec.constraint.Constraint;
import org.anon.exec.constraint.ConstraintManager;
import org.anon.exec.constraint.OracleConstraint;
import org.anon.exec.constraint.SqlServerConstraint;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class SqlServerExec extends BaseExec {

	@Override
	protected ConstraintManager getConstraintManager(final DataSource dataSource) {

		return new ConstraintManager(dataSource){

			@Override
			protected List<SqlServerConstraint> loadConstraints(AnonymisedColumnInfo anonymisedColumnInfo) {
				String sql_select = "SELECT       KCU1.CONSTRAINT_NAME AS FK_CONSTRAINT_NAME                     " +
							 " ,KCU1.TABLE_NAME AS FK_TABLE_NAME "+
							 " FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS AS RC                             " +
							 " INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KCU1                            " +
							 "     ON KCU1.CONSTRAINT_CATALOG = RC.CONSTRAINT_CATALOG                            " +
							 "     AND KCU1.CONSTRAINT_SCHEMA = RC.CONSTRAINT_SCHEMA                             " +
							 "     AND KCU1.CONSTRAINT_NAME = RC.CONSTRAINT_NAME                                 " +
							 " INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KCU2                            " +
							 "     ON KCU2.CONSTRAINT_CATALOG = RC.UNIQUE_CONSTRAINT_CATALOG                     " +
							 "     AND KCU2.CONSTRAINT_SCHEMA = RC.UNIQUE_CONSTRAINT_SCHEMA                      " +
							 "     AND KCU2.CONSTRAINT_NAME = RC.UNIQUE_CONSTRAINT_NAME                          " +
							 "     AND KCU2.ORDINAL_POSITION = KCU1.ORDINAL_POSITION                             " +
							 " where (KCU1.TABLE_NAME = ?) " +
							 " or (KCU2.TABLE_NAME = ?) ";

				List<SqlServerConstraint> constraints = jdbcTemplate.query(sql_select, new Object [] {anonymisedColumnInfo.getTable().getName(), anonymisedColumnInfo.getTable().getName()}, 
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


		};
	}


}

