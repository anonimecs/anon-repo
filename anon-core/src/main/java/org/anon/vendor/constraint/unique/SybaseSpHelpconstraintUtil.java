package org.anon.vendor.constraint.unique;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SybaseSpHelpconstraintUtil {

	public enum ConstraintType {PK, UNIQUE}
	
	public static boolean isUnique(ResultSet rs) throws SQLException {
		
		return "unique constraint".equalsIgnoreCase(rs.getString("type"))
				&&
				rs.getString("definition").toLowerCase().startsWith("unique");
	}

	public static boolean isPrimaryKey(ResultSet rs) throws SQLException {
		return "unique constraint".equalsIgnoreCase(rs.getString("type"))
				&&
				rs.getString("definition").toLowerCase().startsWith("primary key");
	}

	/**
	 * Examples
	 * UNIQUE INDEX ( COL1, COL2) : NONCLUSTERED
	 * UNIQUE INDEX ( COL1) : NONCLUSTERED
	 */
	public static String [] parseColumnsForUnique(String definition) {
		return definition.split("\\(")[1].split("\\)")[0].trim().split("\\s*,\\s*");
	}

	/**
	 * Examples
	 * PRIMARY KEY INDEX ( COL1, COL2) : CLUSTERED
	 * PRIMARY KEY INDEX ( COL1) : CLUSTERED
	 */
	public static String [] parseColumnsForPk(String definition) {
		return definition.split("\\(")[1].split("\\)")[0].trim().split("\\s*,\\s*");
	}
	
	public static UniqueConstraint mapRow(final String tableName, ResultSet rs, ConstraintType requestedConstraintType) throws SQLException {
		if(requestedConstraintType == ConstraintType.UNIQUE && isUnique(rs) ){
			return new SybaseUniqueConstraint(rs, tableName);
		}
		else if(requestedConstraintType == ConstraintType.PK && isPrimaryKey(rs)){
			return new SybasePrimaryKeyConstraint(rs, tableName);
		}
		else {
			return null;
		}
	}


}
