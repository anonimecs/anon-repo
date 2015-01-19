package org.anon.gui;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.anon.data.Database;
import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.service.DatabaseConfigService;
import org.anon.service.DbConnectionFactory;
import org.anon.service.ServiceResult;

@ManagedBean
@SessionScoped
public class DatabaseConfigBacking extends BackingBase {

	@ManagedProperty(value = "#{databaseConfigServiceImpl}")
	private DatabaseConfigService configService;
	
	@ManagedProperty(value = "#{databasePanelBacking}")
	private DatabasePanelBacking databasePanelBacking;
	
	@ManagedProperty(value = "#{dbConnectionFactory}")
	private	DbConnectionFactory dbConnectionFactory;
	
	private List<DatabaseConfig> configList;
	private DatabaseConfig configBean;

	@PostConstruct
	private void init() {
		reset();
	}
	
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
		configBean = new DatabaseConfig();
	}
	
	public void updateDatabaseConfig() {
		logDebug("update databaseConfig " + configBean.getUrl());
		
		ServiceResult result = configService.updateDatabaseConfig(configBean);
		
		if(!result.isFailed()) {
			showExtInfoInGui("Config updated", configBean.getUrl());
			configBean = new DatabaseConfig();
			databasePanelBacking.init();
			redirectPageTo(NavigationCaseEnum.LIST_CONNECTION);
		}
		handleServiceResultAsInfoMessage(result);
	}
	
	public boolean isConfigInUse(DatabaseConfig config) {
		
		if(dbConnectionFactory.getDatabaseConfig() != null && 
				dbConnectionFactory.getDatabaseConfig().equals(config)) {
			return true;
		}
		return false;
	}
	
	public void prepareModify(DatabaseConfig config) {
		configBean = config;
		redirectPageTo(NavigationCaseEnum.MODIFY_CONNECTION);
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
	
	public Database[] getSupportedDatabases() {
		return Database.values();
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
	
	public void setDatabasePanelBacking(DatabasePanelBacking databasePanelBacking) {
		this.databasePanelBacking = databasePanelBacking;
	}

	public void setDbConnectionFactory(DbConnectionFactory dbConnectionFactory) {
		this.dbConnectionFactory = dbConnectionFactory;
	}
	
}
