package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class SybaseConstraintManager extends ConstraintManager {
	public SybaseConstraintManager(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<SybaseConstraint> loadConstraints(String tableName, String schema) {
		jdbcTemplate.execute("use " + schema);
		String sp_helpconstraint = "sp_helpconstraint '" + tableName + "', 'detail'";
		List<SybaseConstraint> allConstraints = jdbcTemplate.query(sp_helpconstraint, new RowMapper<SybaseConstraint>(){
			@Override
			public SybaseConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
				try {
					return new SybaseConstraint(rs);
				} catch (Exception e) {
					logger.error("deactivateConstraints failed", e);
					return null;
				}
			}
			
		});
		
		List<SybaseConstraint> referentialConstraints = new ArrayList<>();
		for(SybaseConstraint sybaseConstraint:allConstraints){
			if(sybaseConstraint != null && sybaseConstraint.isReferentialConstraint()){
				referentialConstraints.add(sybaseConstraint);
			}
		}
		return referentialConstraints;
	}
}