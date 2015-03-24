package org.anon.vendor.constraint.unique;

import java.util.List;

import javax.sql.DataSource;

import org.anon.vendor.constraint.SybaseSpHelpconstraintUtil;

public class SybaseUniqueConstraintManager extends UniqueConstraintManager<UniqueConstraint> {
	
	public SybaseUniqueConstraintManager(DataSource dataSource) {
		super(dataSource);
	}



	@Override
	public List<UniqueConstraint> loadUniques(String tableName, String columnName, String schemaName) {
		List tableConstraints = SybaseSpHelpconstraintUtil.spHelpconstraint(tableName, schemaName, SybaseUniqueConstraint.class, jdbcTemplate);
		return filterColumnConstraints(columnName, tableConstraints);
	}
	
	@Override
	public List<UniqueConstraint> loadPrimaryKeys(String tableName, String columnName, String schemaName) {
		List tableConstraints = SybaseSpHelpconstraintUtil.spHelpconstraint(tableName, schemaName, SybasePrimaryKeyConstraint.class, jdbcTemplate);
		return filterColumnConstraints(columnName, tableConstraints);

	}
	
//	@SuppressWarnings("unchecked")
//	protected List<UniqueConstraint> spHelpconstraint(final String tableName, String schema, final ConstraintType requestedConstraintType) {
//		jdbcTemplate.execute("use " + schema);
//		String sp_helpconstraint = "sp_helpconstraint '" + tableName + "', 'detail'";
//		try {
//			List<UniqueConstraint> allConstraints = jdbcTemplate.query(sp_helpconstraint, new RowMapper<UniqueConstraint>(){
//				@Override
//				public UniqueConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
//					return SybaseSpHelpconstraintUtil.mapRow(tableName, rs, requestedConstraintType);
//				}
//
//				
//			});
//			// remove nulls
//			SybaseSpHelpconstraintUtil.removeNulls(allConstraints);
//			return allConstraints;
//		} catch (Exception e) {
//			// sybase sp_helpconstraint fails for tables with no constraints
//			logger.error("Probably unconstrained table. Failed " + sp_helpconstraint, e);
//			return (List<UniqueConstraint>)Collections.EMPTY_LIST;
//		}
//	}






	

}