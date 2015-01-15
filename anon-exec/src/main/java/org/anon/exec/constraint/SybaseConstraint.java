package org.anon.exec.constraint;

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
	
	public String getTableName() {
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
	
	
	
}
