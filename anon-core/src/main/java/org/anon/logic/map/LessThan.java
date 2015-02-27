package org.anon.logic.map;

import org.anon.data.AnonymisedColumnInfo;

public class LessThan extends MappingRule {
	private String boundary;
	private String mappedValue;
	
	public LessThan(String boundary, String mappedValue) {
		super();
		this.boundary = boundary;
		this.mappedValue = mappedValue;
	}
	
	@Override
	public String getCondition(){
		return getBoundary();
	}
	
	public String getBoundary() {
		return boundary;
	}
	
	@Override
	public String getMappedValue() {
		return mappedValue;
	}

	@Override
	public String generateWhenSql(AnonymisedColumnInfo col) {
		return " when " + col.getName() + " < " + col.getQuoteIfNeeded() + boundary + col.getQuoteIfNeeded() + " then "+ col.getQuoteIfNeeded() +  mappedValue + col.getQuoteIfNeeded();
	}

	@Override
	public MappingRuleType getMappingRuleType() {
		return MappingRuleType.LessThan;
	}
}
