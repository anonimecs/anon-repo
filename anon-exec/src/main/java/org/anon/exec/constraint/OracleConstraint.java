package org.anon.exec.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleConstraint extends Constraint {

	private String constraintName;
	private String sourceTableName;
	private String targetTableName;
	
	public OracleConstraint(ResultSet rs) throws SQLException {
		constraintName = rs.getString("constraintName");
		sourceTableName = rs.getString("sourceTableName");
		targetTableName = rs.getString("targetTableName");
	}

	public String getConstraintName() {
		return constraintName;
	}

	public String getSourceTableName() {
		return sourceTableName;
	}
	
	public String getTargetTableName() {
		return targetTableName;
	}

	@Override
	public String createDeactivateSql() {
		return "alter table " + getSourceTableName()+ " DISABLE constraint " + getConstraintName();
	}	
	
	@Override
	public String createActivateSql() {
		return "alter table " + getSourceTableName() + " ENABLE constraint " + getConstraintName();
	}			

}
