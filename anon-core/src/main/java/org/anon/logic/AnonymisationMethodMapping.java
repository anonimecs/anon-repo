package org.anon.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.logic.map.MappingDefault;
import org.anon.logic.map.MappingRule;


public abstract class AnonymisationMethodMapping extends AnonymisationMethod {
	
	/**
	 * If no rules apply, this default will be used
	 */
	protected MappingDefault mappingDefault;
	protected List<MappingRule> mappingRulesList = new ArrayList<>();
	
	public AnonymisationMethodMapping() {
		super(AnonymizationType.MAP);
	}
	
	@Override
	public boolean supports(AnonymisedColumnInfo anonymizedColumn) {
		return true;
	}

	public void setMappingDefault(MappingDefault mappingDefault) {
		this.mappingDefault = mappingDefault;
	}
	
	public MappingDefault getMappingDefault() {
		return mappingDefault;
	}
	
	public void addMappingRule(MappingRule mappingRule){
		mappingRulesList.add(mappingRule);
	}
	
	public void deleteMappingRule(MappingRule mappingRule){
		mappingRulesList.remove(mappingRule);
	}
	
	/**
	 * Moves this rule one up in the list 
	 */
	public void moveUp(MappingRule mappingRule){
		int index = mappingRulesList.indexOf(mappingRule);
		if(index > 0){
			Collections.swap(mappingRulesList, index, index - 1);
		}
		else{
			logger.warn("Can not move up rule on the top");
		}
	}
	
	/**
	 * Moves this rule one down in the list 
	 */
	public void moveDown(MappingRule mappingRule){
		int index = mappingRulesList.indexOf(mappingRule);
		if(index < mappingRulesList.size() - 1){
			Collections.swap(mappingRulesList, index, index + 1);
		}
		else{
			logger.warn("Can not move down rule on the bottom");
		}
	}
	
	public List<MappingRule> getMappingRulesList() {
		return mappingRulesList;
	}

}

