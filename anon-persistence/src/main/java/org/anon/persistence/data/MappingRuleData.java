package org.anon.persistence.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.anon.logic.map.MappingRule.MappingRuleType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class MappingRuleData extends PersistentObject{

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;	

	@Column
	private MappingRuleType mappingRuleType;

	@Column
	private String boundary;
	
	@Column
	private String mappedValue;
	
	@ManyToOne @JoinColumn(name="AnonymisationMethodData_ID")
	private AnonymisationMethodMappingData anonymisationMethodMappingData;

	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		MappingRuleData other = (MappingRuleData)obj;
		return new EqualsBuilder().append(id, other.id).isEquals();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MappingRuleType getMappingRuleType() {
		return mappingRuleType;
	}

	public void setMappingRuleType(MappingRuleType mappingRuleType) {
		this.mappingRuleType = mappingRuleType;
	}

	public String getBoundary() {
		return boundary;
	}

	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}

	public String getMappedValue() {
		return mappedValue;
	}

	public void setMappedValue(String mappedValue) {
		this.mappedValue = mappedValue;
	}

	public AnonymisationMethodMappingData getAnonymisationMethodMappingData() {
		return anonymisationMethodMappingData;
	}

	public void setAnonymisationMethodMappingData(AnonymisationMethodMappingData anonymisationMethodMappingData) {
		this.anonymisationMethodMappingData = anonymisationMethodMappingData;
	}
	
	
	

}
