package org.anon.exec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.exec.constraint.ConstraintManager;
import org.anon.exec.constraint.MySqlConstraint;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class MySqlExec extends BaseExec{
	@Override
	protected ConstraintManager getConstraintManager(DataSource dataSource) {
		return new ConstraintManager(dataSource) {

			@Override
			protected List<MySqlConstraint> loadConstraints(AnonymisedColumnInfo anonymisedColumnInfo) {
				String sql_select = "select  TABLE_NAME, GROUP_CONCAT(COLUMN_NAME) as sourceColumns,CONSTRAINT_NAME, REFERENCED_TABLE_NAME, GROUP_CONCAT(REFERENCED_COLUMN_NAME) as targetColumns" +
									" from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
									" where  (REFERENCED_TABLE_NAME = ? or TABLE_NAME = ? and REFERENCED_TABLE_NAME is not null) " +
									" order by CONSTRAINT_NAME";
				
				List<MySqlConstraint> constraints = jdbcTemplate.query(sql_select, new Object [] {anonymisedColumnInfo.getTable().getName(), anonymisedColumnInfo.getTable().getName()}, 
				new RowMapper<MySqlConstraint>(){
					@Override
					public MySqlConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
						try {
							return new MySqlConstraint(rs);
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
