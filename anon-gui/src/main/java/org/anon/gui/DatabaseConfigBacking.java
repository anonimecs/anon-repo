package org.anon.gui;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.data.DatabaseSupportEnum;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.service.DatabaseConfigService;
import org.anon.service.ServiceResult;

@ManagedBean
@ViewScoped
public class DatabaseConfigBacking extends BackingBase {

	@ManagedProperty(value = "#{databaseConfigServiceImpl}")
	private DatabaseConfigService configService;
	
	@ManagedProperty(value = "#{databasePanelBacking}")
	private DatabasePanelBacking databasePanelBacking;


	
	private List<DatabaseConfig> configList;
	private DatabaseConfig configBean;

	@PostConstruct
	private void init() {
		reset();
	}
	
//	public void vendorChanged(){
//		configBean.setUrlPrefix(configService.getUrlPrefix(configBean));
//	}
	
	public void deleteDatabaseConfig(DatabaseConfig config) {
		logDebug("delete databaseConfig " + config.getUrl());
		
		ServiceResult result = configService.deleteDatabaseConfig(config);
		
		if(!result.isFailed()) {
			showExtInfoInGui("Config deleted", config.getUrl());
			reset();
		}
		handleServiceResultAsInfoMessage(result);
		
		databasePanelBacking.init();
	}
	
	public void addDatabaseConfig() {
		logDebug("add databaseConfig " + configBean.getUrl());
		
		ServiceResult result = configService.addDatabaseConfig(configBean);
		
		if(!result.isFailed()) {
			showExtInfoInGui("Config added", configBean.getUrl());
			reset();	
		}
		handleServiceResultAsInfoMessage(result);
		databasePanelBacking.init();
	}

	public void reset() {
		configList = null;
		configBean = new DatabaseConfig();
	}
	
	public List<DatabaseConfig> getConfigList() {
		if(configList==null) {
			configList = configService.loadConnectionConfigs();
		}
		return configList;
	}
	
	public DatabaseSupportEnum[] getSupportedDatabases() {
		return DatabaseSupportEnum.values();
	}
	
	public String[] getSupportedVersions() {
		if(configBean.getVendor()==null) {
			return new String[]{""};
		}else {
			return DatabaseSupportEnum.getVersionsByName(configBean.getVendor());
		}
	}

	public DatabaseConfig getConfigBean() {
		return configBean;
	}

	public void setConfigBean(DatabaseConfig configBean) {
		this.configBean = configBean;
	}

	public void setConfigList(List<DatabaseConfig> configList) {
		this.configList = configList;
	}

	public void setConfigService(DatabaseConfigService configService) {
		this.configService = configService;
	}
	
	public DatabasePanelBacking getDatabasePanelBacking() {
		return databasePanelBacking;
	}
	
	public void setDatabasePanelBacking(DatabasePanelBacking databasePanelBacking) {
		this.databasePanelBacking = databasePanelBacking;
	}
}
