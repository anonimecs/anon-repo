package org.anon.vendor.constraint.unique;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class SybasePrimaryKeyConstraint extends UniqueConstraint{

	private String definition;

	
	public SybasePrimaryKeyConstraint(ResultSet rs, String tableName) throws SQLException {
		this.tableName = tableName;
		this.constraintName  = rs.getString("name");
		this.definition = rs.getString("definition");
		
		String [] str = SybaseSpHelpconstraintUtil.parseColumnsForPk(definition);
		this.columnNames = Arrays.asList(str);
		
	}

	@Override
	public String createDeactivateSql() {
		return "alter table " +tableName+" drop constraint " + constraintName;
	}	

	
	@Override
	public String createActivateSql() {
		return "alter table " +tableName+" add PRIMARY KEY (" + getColumNamesAsCommaSeparatedList()+ ") ";
	}


}
