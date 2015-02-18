package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SybaseConstraint extends Constraint {
	/** example: fk_RBE_CodeCpty_POS */
	private String name; 
	/** example: referential constraint */
	private String type; 
	/** example: POSITION FOREIGN KEY (RBE_CodeCpty) REFERENCES CPTY(RBE_CodeCpty) */
	private String definition;
	
	
	
	public SybaseConstraint(ResultSet rs) throws SQLException {
		name = rs.getString("name");
		type = rs.getString("type");
		definition = rs.getString("definition");
	}
	
	public boolean isReferentialConstraint(){
		return "referential constraint".equalsIgnoreCase(type);
	}

	public String getSourceTableName() {
		// it is the first word in the definition
		return definition.split(" ", 2)[0];
	}

	public String getStrippedDefinition() {
		// it is the first word in the definition
		return definition.split(" ", 2)[1];
	}

	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public String getDefinition() {
		return definition;
	}
	
	@Override
	public String createDeactivateSql() {
		return "alter table " + getSourceTableName() + " drop constraint " + getName();
	}


	@Override
	public String createActivateSql() {
		return "alter table " +getSourceTableName() + " add constraint " + getName() + " " + getStrippedDefinition();
	}

	public boolean isMultiColumnReferentialConstraint(){
		return getSourceColumnNames().length > 1;
	}

	public String[] getSourceColumnNames() {
		// example: Table1 FOREIGN KEY (Column1) REFERENCES Table2(Column2)
		// example2: Table1   FOREIGN KEY (col1_ref, col2_ref)    REFERENCES Table2 (col1, col2)
		return definition.split("\\(")[1].split("\\)")[0].split(",");
	}
	
	
}
