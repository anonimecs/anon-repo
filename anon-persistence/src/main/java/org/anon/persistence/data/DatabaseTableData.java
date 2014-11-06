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

@Entity
public class DatabaseTableData extends PersistentObject{

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;	

	@Column(name="TABLENAME")
	private String name;
	
	@Column(name="SCHEMANAME")
	private String schema;
	
	@OneToMany(mappedBy="table", fetch=FetchType.EAGER, cascade=CascadeType.ALL) 
	private List<DatabaseColumnData> columns = new LinkedList<DatabaseColumnData>();
	
	@Column(name="DATABASECONFIG_ID")
	private Long databaseConfigId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void addColumns(List<DatabaseColumnData> someCols) {
		for(DatabaseColumnData col:someCols){
			addColumn(col);
		}
	}
	
	public void addColumn(DatabaseColumnData databaseColumnData){
		columns.add(databaseColumnData);
		databaseColumnData.setTable(this);
	}
	
	public List<DatabaseColumnData> getColumns() {
		return columns;
	}
	public Long getDatabaseConfigId() {
		return databaseConfigId;
	}
	public void setDatabaseConfigId(Long databaseConfigId) {
		this.databaseConfigId = databaseConfigId;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}


	
	
}
