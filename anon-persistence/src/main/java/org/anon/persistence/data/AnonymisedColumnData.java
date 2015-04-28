package org.anon.persistence.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class AnonymisedColumnData extends PersistentObject{

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;	

	@Column
	private String columnName;
	
	@Column
	private String columnType;
	
	@Column
	private String tableName;
	
	@Column
	private String schemaName;
	
	@Column
	private String whereCondition;
	
	@Column
	private String guiFieldWhereCondition;
	
	@Column
	private String guiFieldApplicability;
	
	@ManyToOne
	@JoinColumn(name="AnonymisationMethodData_ID")
	private AnonymisationMethodData anonymisationMethodData;

	public AnonymisationMethodData getAnonymisationMethodData() {
		return anonymisationMethodData;
	}

	public void setAnonymisationMethodData(AnonymisationMethodData anonymisationMethodData) {
		this.anonymisationMethodData = anonymisationMethodData;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getWhereCondition() {
		return whereCondition;
	}

	public void setWhereCondition(String whereClause) {
		this.whereCondition = whereClause;
	}

	public String getGuiFieldWhereCondition() {
		return guiFieldWhereCondition;
	}

	public void setGuiFieldWhereCondition(String guiFieldWhereCondition) {
		this.guiFieldWhereCondition = guiFieldWhereCondition;
	}

	public String getGuiFieldApplicability() {
		return guiFieldApplicability;
	}

	public void setGuiFieldApplicability(String guiFieldApplicability) {
		this.guiFieldApplicability = guiFieldApplicability;
	}

	
	
}
