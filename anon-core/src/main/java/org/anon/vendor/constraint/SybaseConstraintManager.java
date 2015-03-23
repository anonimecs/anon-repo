package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class SybaseConstraintManager extends ForeignKeyConstraintManager<SybaseForeignKeyConstraint> {
	public SybaseConstraintManager(DataSource dataSource) {
		super(dataSource);
	}


	@SuppressWarnings("unchecked")
	protected List<SybaseForeignKeyConstraint> spHelpconstraint(String tableName, String schema) {
		jdbcTemplate.execute("use " + schema);
		String sp_helpconstraint = "sp_helpconstraint '" + tableName + "', 'detail'";
		try {
			List<SybaseForeignKeyConstraint> allConstraints = jdbcTemplate.query(sp_helpconstraint, new RowMapper<SybaseForeignKeyConstraint>(){
				@Override
				public SybaseForeignKeyConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
					try {
						return new SybaseForeignKeyConstraint(rs);
					} catch (Exception e) {
						logger.error("deactivateConstraints failed", e);
						return null;
					}
				}
				
			});
			return allConstraints;
		} catch (Exception e) {
			// sybase sp_helpconstraint fails for tables with no constraints
			logger.error("Probably unconstrained table. Failed " + sp_helpconstraint, e);
			return (List<SybaseForeignKeyConstraint>)Collections.EMPTY_LIST;
		}
	}
	
	@Override
	protected List<SybaseForeignKeyConstraint> loadForeignKeysTo(String tableName, String columnName, String schema) {
		List<SybaseForeignKeyConstraint> allConstraints = spHelpconstraint(tableName, schema);

		List<SybaseForeignKeyConstraint> res = new ArrayList<>();
		for(SybaseForeignKeyConstraint sybaseConstraint:allConstraints){
			if(sybaseConstraint != null && sybaseConstraint.isReferentialConstraint() 
					&& sybaseConstraint.getTargetTableName().equalsIgnoreCase(tableName)
					&& sybaseConstraint.containsTargetColumn(columnName)){
				res.add(sybaseConstraint);
			}
		}
		return res;
	}
	
	@Override
	protected List<SybaseForeignKeyConstraint> loadForeignKeysFrom(String tableName, String columnName, String schema) {
		List<SybaseForeignKeyConstraint> allConstraints = spHelpconstraint(tableName, schema);

		List<SybaseForeignKeyConstraint> res = new ArrayList<>();
		for(SybaseForeignKeyConstraint sybaseConstraint:allConstraints){
			if(sybaseConstraint != null && sybaseConstraint.isReferentialConstraint() 
					&& sybaseConstraint.getSourceTableName().equalsIgnoreCase(tableName)
					&& sybaseConstraint.containsSourceColumn(columnName)){
				res.add(sybaseConstraint);
			}
		}
		return res;
	}
}