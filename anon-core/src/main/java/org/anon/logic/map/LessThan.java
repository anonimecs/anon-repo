package org.anon.logic.map;


public class LessThan extends MappingRule {

	
	public LessThan(String boundary, String mappedValue) {
		super();
		this.boundary = boundary;
		this.mappedValue = mappedValue;
	}

	@Override
	public MappingRuleType getMappingRuleType() {
		return MappingRuleType.LessThan;
	}

	@Override
	public String getSqlCondition() {
		return " < ";
	}
}
