package org.anon.vendor.constraint.referential;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SybaseForeignKeyConstraint extends ForeignKeyConstraint {
	
	/** example: referential constraint */
	private String type; 
	/** example: POSITION FOREIGN KEY (RBE_CodeCpty) REFERENCES CPTY(RBE_CodeCpty) */
	private String definition;
	
	
	
	public SybaseForeignKeyConstraint(ResultSet rs) throws SQLException {
		type = rs.getString("type");
		definition = rs.getString("definition");

		constraintName = rs.getString("name");
		sourceTableName = parseSourceTableName();
		targetTableName = parseTargetTableName();
	}
	
	private String parseSourceTableName() {
		return definition.split(" ", 2)[0].trim();
	}

	private String parseTargetTableName() {
		return definition.split("REFERENCES")[1].split("\\(")[0].trim();
	}

	public String getStrippedDefinition() {
		// it is the first word in the definition
		return definition.split(" ", 2)[1];
	}

	public String getType() {
		return type;
	}
	public String getDefinition() {
		return definition;
	}
	
	@Override
	public String createDeactivateSql() {
		return "alter table " + getSourceTableName() + " drop constraint " + getConstraintName();
	}


	@Override
	public String createActivateSql() {
		return "alter table " +getSourceTableName() + " add constraint " + getConstraintName() + " " + getStrippedDefinition();
	}


	@Override
	public String[] getSourceColumnNames() {
		// example: Table1 FOREIGN KEY (Column1) REFERENCES Table2(Column2)
		// example2: Table1   FOREIGN KEY (col1_ref, col2_ref)    REFERENCES Table2 (col1, col2)
		return trim(definition.split("\\(")[1].split("\\)")[0].split(","));
	}
	
	private String[] trim(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].trim();
		}
		return array;
	}

	@Override
	public String[] getTargetColumnNames() {
		// example: Table1 FOREIGN KEY (Column1) REFERENCES Table2(Column2)
		// example2: Table1   FOREIGN KEY (col1_ref, col2_ref)    REFERENCES Table2 (col1, col2)
		return trim(definition.split("REFERENCES")[1]
				.split("\\(")[1].split("\\)")[0]
				.split(","));
	}

	
}
