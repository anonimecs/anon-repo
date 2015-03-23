package org.anon.vendor.constraint.unique;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class OraclePrimaryKeyConstraint extends UniqueConstraint{

	public OraclePrimaryKeyConstraint(ResultSet rs, String tableName) throws SQLException {
		this.constraintName  = rs.getString("CONSTRAINT_NAME");
		String str = rs.getString("COLUMN_NAMES");
		this.columnNames = Arrays.asList(str.split("\\s*,\\s*"));
		this.tableName = tableName;
		
	}

	@Override
	public String createDeactivateSql() {
		return "alter table " + tableName + " disable PRIMARY KEY cascade";
	}	
	
	@Override
	public String createActivateSql() {
		return "alter table " + tableName + " enable PRIMARY KEY";
	}


}
