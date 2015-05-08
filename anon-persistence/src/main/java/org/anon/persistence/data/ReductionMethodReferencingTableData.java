package org.anon.persistence.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.anon.data.ReductionMethodReferencingTable;
import org.anon.data.ReductionType;

@Entity
public class ReductionMethodReferencingTableData extends PersistentObject implements ReductionMethodReferencingTable{
	
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

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public ReductionMethodData getReductionMethodData() {
		return reductionMethodData;
	}

	public void setReductionMethodData(ReductionMethodData reductionMethodData) {
		this.reductionMethodData = reductionMethodData;
	}

	@Override
	public ReductionType getReductionType() {
		return reductionType;
	}

	public void setReductionType(ReductionType reductionType) {
		this.reductionType = reductionType;
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	@Override
	public String getWhereCondition() {
		return whereCondition;
	}

	public void setWhereCondition(String whereCondition) {
		this.whereCondition = whereCondition;
	}



	
}
