package org.anon.vendor.constraint.notnull;

import java.util.List;
import java.util.Map;

import org.anon.data.DatabaseColumnInfo;
import org.springframework.jdbc.core.JdbcTemplate;

public class MySqlNotNullConstraint extends NotNullConstraint{

	private JdbcTemplate jdbcTemplate;
	
	
	public MySqlNotNullConstraint(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public String createActivateSql(DatabaseColumnInfo databaseColumnInfo) {
		return "alter table "+databaseColumnInfo.getTable().getName()+" modify "+databaseColumnInfo.getName() +" " + getTypeWithPrecision(databaseColumnInfo)+ " not null";
	}

	@Override
	public String createDeactivateSql(DatabaseColumnInfo databaseColumnInfo) {
		return "alter table "+databaseColumnInfo.getTable().getName()+" modify "+databaseColumnInfo.getName() +" " + getTypeWithPrecision(databaseColumnInfo)+ " null";
	}

	private String getTypeWithPrecision(DatabaseColumnInfo databaseColumnInfo) {
		String sql = "SHOW COLUMNS FROM "+databaseColumnInfo.getTable().getName()+" FROM "+databaseColumnInfo.getTable().getSchema()+" like '"+databaseColumnInfo.getName()+"'";
		List<Map<String, Object>> res = jdbcTemplate.queryForList(sql);
		String typeWithPrecision = (String) res.get(0).get("Type");
		return typeWithPrecision;
	}



}
