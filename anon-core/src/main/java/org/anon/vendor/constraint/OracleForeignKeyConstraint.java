package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;

// TODO: support for multi column FKs
public class OracleForeignKeyConstraint extends ForeignKeyConstraint {

	private String sourceColumn;
	private String targetColumn;

	
	public OracleForeignKeyConstraint(ResultSet rs) throws SQLException {
		constraintName = rs.getString("constraintName");
		sourceTableName = rs.getString("sourceTableName");
		targetTableName = rs.getString("targetTableName");
		sourceColumn = rs.getString("sourceColumnName");
		targetColumn = rs.getString("targetColumnName");
	}

	@Override
	public String createDeactivateSql() {
		return "alter table " + getSourceTableName()+ " DISABLE constraint " + getConstraintName();
	}	
	
	@Override
	public String createActivateSql() {
		return "alter table " + getSourceTableName() + " ENABLE constraint " + getConstraintName();
	}

	@Override
	public String[] getSourceColumnNames() {
		return new String[]{sourceColumn};
	}

	@Override
	public String[] getTargetColumnNames() {
		return new String[]{targetColumn};
	}			

}
