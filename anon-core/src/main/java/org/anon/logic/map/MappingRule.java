package org.anon.logic.map;

import org.anon.data.AnonymisedColumnInfo;

public abstract class MappingRule {

	protected String boundary;
	protected String mappedValue;
	
	public enum MappingRuleType{LessThan, MoreThan, EqualsTo}
	
	public String getMappedValue() {
		return mappedValue;
	}
	
	public String getBoundary() {
		return boundary;
	}
	
	public String generateWhenSql(AnonymisedColumnInfo col) {
		return " when " + col.getName() + getSqlCondition() + col.getQuoteIfNeeded() + boundary + col.getQuoteIfNeeded() + " then "+ col.getQuoteIfNeeded() +  mappedValue + col.getQuoteIfNeeded();
	}

	public String generatePreviewWhenSql(Object value, AnonymisedColumnInfo col) {
		return " when " + col.getQuoteIfNeeded() + value.toString() + col.getQuoteIfNeeded() + getSqlCondition() + col.getQuoteIfNeeded() + boundary + col.getQuoteIfNeeded() + " then "+ col.getQuoteIfNeeded() +  mappedValue + col.getQuoteIfNeeded();
	}

	
	public abstract String getSqlCondition();

	public abstract MappingRuleType getMappingRuleType();
	
	
}
