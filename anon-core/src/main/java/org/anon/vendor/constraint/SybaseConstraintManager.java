package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class SybaseConstraintManager extends ConstraintManager<SybaseConstraint> {
	public SybaseConstraintManager(DataSource dataSource) {
		super(dataSource);
	}


	protected List<SybaseConstraint> spHelpconstraint(String tableName, String schema) {
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
		return allConstraints;
	}
	
	@Override
	protected List<SybaseConstraint> loadForeignKeysTo(String tableName, String columnName, String schema) {
		List<SybaseConstraint> allConstraints = spHelpconstraint(tableName, schema);

		List<SybaseConstraint> res = new ArrayList<>();
		for(SybaseConstraint sybaseConstraint:allConstraints){
			if(sybaseConstraint != null && sybaseConstraint.isReferentialConstraint() 
					&& sybaseConstraint.getTargetTableName().equalsIgnoreCase(tableName)
					&& sybaseConstraint.containsTargetColumn(columnName)){
				res.add(sybaseConstraint);
			}
		}
		return res;
	}
	
	@Override
	protected List<SybaseConstraint> loadForeignKeysFrom(String tableName, String columnName, String schema) {
		List<SybaseConstraint> allConstraints = spHelpconstraint(tableName, schema);

		List<SybaseConstraint> res = new ArrayList<>();
		for(SybaseConstraint sybaseConstraint:allConstraints){
			if(sybaseConstraint != null && sybaseConstraint.isReferentialConstraint() 
					&& sybaseConstraint.getSourceTableName().equalsIgnoreCase(tableName)
					&& sybaseConstraint.containsSourceColumn(columnName)){
				res.add(sybaseConstraint);
			}
		}
		return res;
	}
}