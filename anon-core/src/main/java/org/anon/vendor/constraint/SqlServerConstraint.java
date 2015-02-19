package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlServerConstraint extends Constraint {

	private String sourceColumnName;
	
	public SqlServerConstraint(ResultSet rs) throws SQLException {
		constraintName = rs.getString("FK_CONSTRAINT_NAME");
		sourceTableName = rs.getString("SOURCE_TABLE_NAME");
		sourceColumnName = rs.getString("SOURCE_COLUMN_NAME");
	}


	public String getSourceColumnName() {
		return sourceColumnName;
	}
	

	@Override
	public String createDeactivateSql() {
		return "alter table " + getSourceTableName()+ " NOCHECK constraint " + getConstraintName();
	}	
	
	@Override
	public String createActivateSql() {
		return "alter table " + getSourceTableName() + " CHECK constraint " + getConstraintName();
	}


	@Override
	public String[] getSourceColumnNames() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String[] getTargetColumnNames() {
		// TODO Auto-generated method stub
		return null;
	}			

}
