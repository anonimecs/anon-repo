package org.anon.persistence.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class MappingDefaultData extends PersistentObject{

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;	

	@Column
	private String defaultValue;
	
    @OneToOne
    @JoinColumn(name = "AnonymisationMethodData_ID")
    private AnonymisationMethodMappingData anonymisationMethodMappingData;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public AnonymisationMethodMappingData getAnonymisationMethodMappingData() {
		return anonymisationMethodMappingData;
	}

	public void setAnonymisationMethodMappingData(AnonymisationMethodMappingData anonymisationMethodMappingData) {
		this.anonymisationMethodMappingData = anonymisationMethodMappingData;
	}

	
	
}
