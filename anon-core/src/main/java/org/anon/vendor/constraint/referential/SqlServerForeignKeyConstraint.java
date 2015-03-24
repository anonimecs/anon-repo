package org.anon.vendor.constraint.referential;

import java.sql.ResultSet;
import java.sql.SQLException;

// TODO support for multi col FKs
public class SqlServerForeignKeyConstraint extends ForeignKeyConstraint {

	private String sourceColumnName;
	private String targetColumnName;
	
	public SqlServerForeignKeyConstraint(ResultSet rs) throws SQLException {
		constraintName = rs.getString("FK_CONSTRAINT_NAME");
		sourceTableName = rs.getString("SOURCE_TABLE_NAME");
		sourceColumnName = rs.getString("SOURCE_COLUMN_NAME");
		targetTableName = rs.getString("TARGET_TABLE_NAME");
		targetColumnName = rs.getString("TARGET_COLUMN_NAME");
	}


	public String getSourceColumnName() {
		return sourceColumnName;
	}
	
	public String getTargetColumnName() {
		return targetColumnName;
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
		return new String[]{sourceColumnName};
	}


	@Override
	public String[] getTargetColumnNames() {
		return new String[]{targetColumnName};
	}			

}
