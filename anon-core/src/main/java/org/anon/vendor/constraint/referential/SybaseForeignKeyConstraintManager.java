package org.anon.vendor.constraint.referential;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.anon.vendor.constraint.SybaseSpHelpconstraintUtil;

public class SybaseForeignKeyConstraintManager extends ForeignKeyConstraintManager<SybaseForeignKeyConstraint> {
	public SybaseForeignKeyConstraintManager(DataSource dataSource) {
		super(dataSource);
	}



	
	@Override
	public List<SybaseForeignKeyConstraint> loadForeignKeysTo(String tableName, String columnName, String schema) {
		List<SybaseForeignKeyConstraint> allConstraints = SybaseSpHelpconstraintUtil.spHelpconstraint(tableName, schema, SybaseForeignKeyConstraint.class, jdbcTemplate);

		List<SybaseForeignKeyConstraint> res = new ArrayList<>();
		for(SybaseForeignKeyConstraint sybaseConstraint:allConstraints){
			if(sybaseConstraint.getTargetTableName().equalsIgnoreCase(tableName)
					&& sybaseConstraint.containsTargetColumn(columnName)){
				res.add(sybaseConstraint);
			}
		}
		return res;
	}
	
	@Override
	public List<SybaseForeignKeyConstraint> loadForeignKeysFrom(String tableName, String columnName, String schema) {
		List<SybaseForeignKeyConstraint> allConstraints =  SybaseSpHelpconstraintUtil.spHelpconstraint(tableName, schema, SybaseForeignKeyConstraint.class, jdbcTemplate);

		List<SybaseForeignKeyConstraint> res = new ArrayList<>();
		for(SybaseForeignKeyConstraint sybaseConstraint:allConstraints){
			if(sybaseConstraint.getSourceTableName().equalsIgnoreCase(tableName)
					&& sybaseConstraint.containsSourceColumn(columnName)){
				res.add(sybaseConstraint);
			}
		}
		return res;
	}
}