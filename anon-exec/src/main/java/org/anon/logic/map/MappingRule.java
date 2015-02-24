package org.anon.logic.map;

import org.anon.data.AnonymisedColumnInfo;

public abstract class MappingRule {

	public enum MappingRuleType{LessThan}
	
	public abstract String generateWhenSql(AnonymisedColumnInfo col);

	public abstract String getCondition();

	public abstract String getMappedValue();
	
	public abstract MappingRuleType getMappingRuleType();
	
	
}
