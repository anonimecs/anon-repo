package org.anon.logic.map;


public class EqualsTo extends MappingRule {

	
	public EqualsTo(String boundary, String mappedValue) {
		super();
		this.boundary = boundary;
		this.mappedValue = mappedValue;
	}


	@Override
	public MappingRuleType getMappingRuleType() {
		return MappingRuleType.EqualsTo;
	}
	
	@Override
	public String getSqlCondition() {
		return " = ";
	}
}
