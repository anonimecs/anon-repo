package org.anon.gui.anonymise;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.gui.BackingBase;
import org.anon.gui.ConfigContext;
import org.anon.logic.AnonymisationMethodMapping;
import org.anon.logic.map.LessThan;
import org.anon.logic.map.MappingRule;
import org.anon.logic.map.MappingRule.MappingRuleType;

@ManagedBean
@ViewScoped
public class ConfigureMappingsPopupBacking extends BackingBase{

	@ManagedProperty(value="#{configContext}")
	private ConfigContext configContext;
	
	
	private List<MappingRuleType> mappingRuleTypeList;
	private MappingRuleType selectedMappingRuleType;
	private String condition;
	private String mappedValue;
	
	public void onClose(){
		logDebug("onClose ");
		
	}
	
	public void onConfigureMappingsClicked(){
		logDebug("onConfigureMappingsClicked ");
		mappingRuleTypeList = Arrays.asList(MappingRule.MappingRuleType.values());
	}
	
	public void onUp(MappingRule mappingRule){
		logDebug("onUp " + mappingRule);
		getAnonymisationMethodMapping().moveUp(mappingRule);
	}
	
	public void onDown(MappingRule mappingRule){
		logDebug("onDown " + mappingRule);
		getAnonymisationMethodMapping().moveDown(mappingRule);
	
	}
	
	public void onDelete(MappingRule mappingRule){
		logDebug("onDelete " + mappingRule);
		getAnonymisationMethodMapping().deleteMappingRule(mappingRule);
	}
	
	public void onAddClicked(){
		logDebug("on_addClicked");
		
		MappingRule mappingRule = new LessThan(condition, mappedValue);
		getAnonymisationMethodMapping().addMappingRule(mappingRule);
		
	}
	
	public void onConditionSelectChanged(){
		logDebug("on_addClicked");
		
		
	}
	
	public AnonymisationMethodMapping getAnonymisationMethodMapping(){
		return (AnonymisationMethodMapping)configContext.getAnonymisationMethod();
	}
	

	public ConfigContext getConfigContext() {
		return configContext;
	}


	public void setConfigContext(ConfigContext configContext) {
		this.configContext = configContext;
	}


	public List<MappingRule> getMappingRuleList() {
		return getAnonymisationMethodMapping().getMappingRulesList();
	}


	public MappingRuleType getSelectedMappingRuleType() {
		return selectedMappingRuleType;
	}

	public void setSelectedMappingRuleType(MappingRuleType selectedMappingRuleType) {
		this.selectedMappingRuleType = selectedMappingRuleType;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getMappedValue() {
		return mappedValue;
	}

	public void setMappedValue(String mappedValue) {
		this.mappedValue = mappedValue;
	}

	public List<MappingRuleType> getMappingRuleTypeList() {
		return mappingRuleTypeList;
	}
	

	
}
