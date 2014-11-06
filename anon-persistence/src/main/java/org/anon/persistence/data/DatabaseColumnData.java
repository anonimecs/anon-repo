package org.anon.persistence.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DatabaseColumnData extends PersistentObject {

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;	

	@Column(name="COLUMNNAME")
	private String name;
	
	@Column(name="COLUMNTYPE")
	private String type;
	
	@ManyToOne @JoinColumn(name="DatabaseTableData_ID")
	private DatabaseTableData table;
	
	
	
	public DatabaseColumnData(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public DatabaseColumnData() {}
	

	@Override
	public String toString() {
		return getTable().getName() + "." + getName();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public DatabaseTableData getTable() {
		return table;
	}
	public void setTable(DatabaseTableData table) {
		this.table = table;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
