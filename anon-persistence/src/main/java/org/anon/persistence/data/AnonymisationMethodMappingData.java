package org.anon.persistence.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class AnonymisationMethodMappingData extends AnonymisationMethodData{
	
	
	@OneToOne(mappedBy="anonymisationMethodMappingData", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	private MappingDefaultData mappingDefaultData;

	@OneToMany(mappedBy="anonymisationMethodMappingData", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true) 
	protected Set<MappingRuleData> mappingRules = new HashSet<>();

	public void addMappingRuleData(MappingRuleData mappingRuleData) {
		mappingRules.add(mappingRuleData);
		mappingRuleData.setAnonymisationMethodMappingData(this);
	}

	
	public MappingDefaultData getMappingDefaultData() {
		return mappingDefaultData;
	}

	public void setMappingDefaultData(MappingDefaultData mappingDefaultData) {
		this.mappingDefaultData = mappingDefaultData;
	}

	public Set<MappingRuleData> getMappingRules() {
		return mappingRules;
	}

	public void setMappingRules(Set<MappingRuleData> mappingRules) {
		this.mappingRules = mappingRules;
	}
	

	
	
}
