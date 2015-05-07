package org.anon.service.reduce;

import java.util.Arrays;

import org.anon.data.ReductionType;
import org.anon.vendor.constraint.referential.ForeignKeyConstraint;

public class RelatedTable{
	private ForeignKeyConstraint foreignKeyConstraint;
	private String relatedTableName;
	private String [] relatedTableColumns;
	private ReductionType reductionType = ReductionType.DEREFERENCE;
	private String guiWhereCondition; 
	
	
	public RelatedTable(ForeignKeyConstraint foreignKeyConstraint, String relatedTableName, String []relatedTableColumns) {
		super();
		this.foreignKeyConstraint = foreignKeyConstraint;
		this.relatedTableName = relatedTableName;
		this.relatedTableColumns  = relatedTableColumns;
	}
	
	public ForeignKeyConstraint getForeignKeyConstraint() {
		return foreignKeyConstraint;
	}
	
	public String getRelatedTableName() {
		return relatedTableName;
	}
	
	public String [] getRelatedTableColumns() {
		return relatedTableColumns;
	}
	
	public String getRelatedTableColumnsAsString() {
		return Arrays.toString(relatedTableColumns);
	}
	
	public ReductionType getReductionType() {
		return reductionType;
	}
	
	public void setReductionType(ReductionType reductionType) {
		this.reductionType = reductionType;
	}

	public String getGuiWhereCondition() {
		return guiWhereCondition;
	}

	public void setGuiWhereCondition(String guiWhereCondition) {
		this.guiWhereCondition = guiWhereCondition;
	}
	
}
