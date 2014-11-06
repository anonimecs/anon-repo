package org.anon.data;

public class RelatedTableColumnInfo extends DataObject{

	public enum Relation{ForeignKey, SameColumnName}
	
	private String columnName;
	private String tableName;
	private Relation relation;
	
	
	
	public RelatedTableColumnInfo(String columnName, String tableName, Relation relation) {
		super();
		this.columnName = columnName;
		this.tableName = tableName;
		this.relation = relation;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Relation getRelation() {
		return relation;
	}
	public void setRelation(Relation relation) {
		this.relation = relation;
	}
	public String getKey() {
		return columnName + tableName;
	}
	public void setKey(String key) {
		
	}
	public boolean sameAs(DatabaseColumnInfo col) {
		return col.getName().equals(columnName)
				&&
			   col.getTable().getName().equals(tableName);
	}
	
	
}
