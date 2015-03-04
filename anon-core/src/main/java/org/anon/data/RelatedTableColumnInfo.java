package org.anon.data;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class RelatedTableColumnInfo extends DataObject{

	public enum Relation{ForeignKey, SameColumnName}
	
	private String columnName;
	private String tableName;
	private Relation relation;
	private String remark;
	
	
	
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RelatedTableColumnInfo)) return false;
        RelatedTableColumnInfo other = (RelatedTableColumnInfo) obj;

        return new EqualsBuilder()
    		.append(columnName, other.columnName)
        	.append(tableName, other.tableName)
        	.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(columnName)
			.append(tableName)
			.toHashCode();
	}
	
	
}
