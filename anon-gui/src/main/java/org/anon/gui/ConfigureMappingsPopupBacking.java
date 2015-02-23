package org.anon.gui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.anon.logic.map.MappingDefault;
import org.anon.logic.map.MappingRule;
import org.apache.catalina.startup.ContextConfig;

@ManagedBean
@SessionScoped
public class ConfigureMappingsPopupBacking {

	@ManagedProperty(value="#{configContext}")
	private ConfigContext configContext;
	
	private MappingDefault mappingDefault;
	private List<MappingRule> mappingRuleList = new ArrayList<>();
	
	@PostConstruct
	public void init(){
		mappingDefault = new MappingDefault(configContext.getTestValue());
	}
	

	public MappingDefault getMappingDefault() {
		return mappingDefault;
	}

	public void setMappingDefault(MappingDefault mappingDefault) {
		this.mappingDefault = mappingDefault;
	}


	public ConfigContext getConfigContext() {
		return configContext;
	}


	public void setConfigContext(ConfigContext configContext) {
		this.configContext = configContext;
	}


	public List<MappingRule> getMappingRuleList() {
		return mappingRuleList;
	}


	public void setMappingRuleList(List<MappingRule> mappingRuleList) {
		this.mappingRuleList = mappingRuleList;
	}
	

	
}
