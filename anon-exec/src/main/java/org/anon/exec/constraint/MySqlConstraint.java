package org.anon.exec.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlConstraint extends Constraint {

	private String constraintName;
	private String sourceTableName;
	private String targetTableName;
	private String sourceColumns;
	private String targetColumns;
	
	public MySqlConstraint(ResultSet rs) throws SQLException {
		constraintName = rs.getString("CONSTRAINT_NAME");
		sourceTableName = rs.getString("TABLE_NAME");
		targetTableName = rs.getString("REFERENCED_TABLE_NAME");
		sourceColumns = rs.getString("sourceColumns");
		targetColumns = rs.getString("targetColumns");
		
	}

	//@Override
	public String createActivateSql() {
		return "alter table " + sourceTableName + 
				" add constraint " + constraintName + "  FOREIGN KEY (" + sourceColumns +")    REFERENCES " + targetTableName + " (" +targetColumns+ ")";
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
	

	public String getSourceColumns() {
		return sourceColumns;
	}
	public String getTargetColumns() {
		return targetColumns;
	}
}
