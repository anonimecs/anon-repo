package org.anon.logic.map;


public class MoreThan extends MappingRule {

	
	public MoreThan(String boundary, String mappedValue) {
		super();
		this.boundary = boundary;
		this.mappedValue = mappedValue;
	}


	@Override
	public MappingRuleType getMappingRuleType() {
		return MappingRuleType.MoreThan;
	}
	
	@Override
	public String getSqlCondition() {
		return " > ";
	}
}
