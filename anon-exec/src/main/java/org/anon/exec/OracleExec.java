package org.anon.exec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.exec.constraint.ConstraintManager;
import org.anon.exec.constraint.OracleConstraint;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class OracleExec extends BaseExec{

	@Override
	protected ConstraintManager getConstraintManager(final DataSource dataSource) {
		return new ConstraintManager(dataSource) {
			
			@Override
			protected List<OracleConstraint> loadConstraints(AnonymisedColumnInfo anonymisedColumnInfo) {
				String sql_select = "SELECT a.table_name sourceTableName, a.column_name, a.constraint_name constraintName, c.owner, c.r_owner, c_pk.table_name targetTableName " +
								  " FROM all_cons_columns a                                                                                  " +
								  " JOIN all_constraints c ON a.owner = c.owner AND a.constraint_name = c.constraint_name                    " +
								  " JOIN all_constraints c_pk ON c.r_owner = c_pk.owner AND c.r_constraint_name = c_pk.constraint_name       " +
								  " WHERE c.constraint_type = 'R'   AND (c_pk.table_name = ? or a.table_name = ?)";
				
				List<OracleConstraint> constraints = jdbcTemplate.query(sql_select, new Object [] {anonymisedColumnInfo.getTable().getName(), anonymisedColumnInfo.getTable().getName()}, 
				new RowMapper<OracleConstraint>(){
					@Override
					public OracleConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
						try {
							return new OracleConstraint(rs);
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
