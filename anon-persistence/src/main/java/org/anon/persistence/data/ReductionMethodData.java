package org.anon.persistence.data;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.anon.data.ReductionType;

@Entity
public class ReductionMethodData extends PersistentObject{
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	private Long id;	
	
	@Column(name="DATABASECONFIG_ID")
	private Long databaseConfigId;
	
	@Column
	private ReductionType reductionType;

	@Column(name="TABLENAME")
	private String tableName;

	@Column(name="SCHEMANAME")
	private String schemaName;

	@Column(name="WHERECONDITION")
	private String whereCondition;
	
	@OneToMany(mappedBy="reductionMethodData", fetch=FetchType.EAGER, cascade={CascadeType.ALL, CascadeType.REMOVE}, orphanRemoval=true) 
	private List<ReductionMethodReferencingTableData> referencingTableDatas = new LinkedList<>();

	public void add(ReductionMethodReferencingTableData data) {
		referencingTableDatas.add(data);
		data.setReductionMethodData(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReductionType getReductionType() {
		return reductionType;
	}

	public void setReductionType(ReductionType reductionType) {
		this.reductionType = reductionType;
	}

	public Long getDatabaseConfigId() {
		return databaseConfigId;
	}

	public void setDatabaseConfigId(Long databaseConfigId) {
		this.databaseConfigId = databaseConfigId;
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

	public List<ReductionMethodReferencingTableData> getReferencingTableDatas() {
		return referencingTableDatas;
	}

	public void setReferencingTableDatas(List<ReductionMethodReferencingTableData> referencingTableDatas) {
		this.referencingTableDatas = referencingTableDatas;
	}


	
}
