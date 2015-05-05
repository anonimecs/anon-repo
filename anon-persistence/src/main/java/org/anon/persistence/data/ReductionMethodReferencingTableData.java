package org.anon.persistence.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.anon.data.ReductionType;

@Entity
public class ReductionMethodReferencingTableData extends PersistentObject{
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	private Long id;	
	
	@ManyToOne
	@JoinColumn(name="ReductionMethodData_ID")
	private ReductionMethodData reductionMethodData;

	@Column
	private ReductionType reductionType;
	
	@Column(name="TABLENAME")
	private String tableName;

	@Column(name="SCHEMANAME")
	private String schemaName;

	@Column(name="WHERECONDITION")
	private String whereCondition;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReductionMethodData getReductionMethodData() {
		return reductionMethodData;
	}

	public void setReductionMethodData(ReductionMethodData reductionMethodData) {
		this.reductionMethodData = reductionMethodData;
	}

	public ReductionType getReductionType() {
		return reductionType;
	}

	public void setReductionType(ReductionType reductionType) {
		this.reductionType = reductionType;
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

	public void setWhereCondition(String whereCondition) {
		this.whereCondition = whereCondition;
	}



	
}
