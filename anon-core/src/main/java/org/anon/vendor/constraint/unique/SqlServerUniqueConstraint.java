package org.anon.vendor.constraint.unique;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class SqlServerUniqueConstraint extends UniqueConstraint{

	public SqlServerUniqueConstraint(ResultSet rs, String tableName) throws SQLException {
		this.constraintName  = rs.getString("CONSTRAINT_NAME");
		String str = rs.getString("CONS_COLUMNS");
		this.columnNames = Arrays.asList(str.split("\\s*,\\s*"));
		this.tableName = tableName;
		
	}

	@Override
	public String createDeactivateSql() {
		return "alter table " +tableName+" drop constraint " + constraintName;
	}	
	
	@Override
	public String createActivateSql() {
		return "alter table " +tableName+"  add UNIQUE (" + getColumNamesAsCommaSeparatedList() + ") ";
	}


}
