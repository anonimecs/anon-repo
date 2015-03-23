package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlForeignKeyConstraint extends ForeignKeyConstraint {

	private String sourceColumns;
	private String targetColumns;
	
	public MySqlForeignKeyConstraint(ResultSet rs) throws SQLException {
		constraintName = rs.getString("CONSTRAINT_NAME");
		sourceTableName = rs.getString("TABLE_NAME");
		targetTableName = rs.getString("REFERENCED_TABLE_NAME");
		sourceColumns = rs.getString("sourceColumns");
		targetColumns = rs.getString("targetColumns");
		
	}

	@Override
	public String createDeactivateSql() {
		return "alter table " + sourceTableName + " drop FOREIGN KEY " + constraintName;
	}	
	
	@Override
	public String createActivateSql() {
		return "alter table " + sourceTableName + 
				" add constraint " + constraintName + "  FOREIGN KEY (" + sourceColumns +")    REFERENCES " + targetTableName + " (" +targetColumns+ ")";
	}



	public String getSourceColumns() {
		return sourceColumns;
	}
	public String getTargetColumns() {
		return targetColumns;
	}

	@Override
	public String[] getSourceColumnNames() {
		return sourceColumns.split(",");
	}

	@Override
	public String[] getTargetColumnNames() {
		return targetColumns.split(",");
	}
}
