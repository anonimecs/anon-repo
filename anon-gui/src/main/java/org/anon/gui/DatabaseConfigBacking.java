package org.anon.gui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.anon.data.Database;
import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.service.DatabaseConfigService;
import org.anon.service.DatabaseLoaderService;
import org.anon.service.DbConnectionFactory;
import org.anon.service.ServiceException;
import org.anon.service.ServiceResultMessage;

@ManagedBean
@SessionScoped
public class DatabaseConfigBacking extends BackingBase {

	@ManagedProperty(value = "#{databaseConfigServiceImpl}")
	private DatabaseConfigService configService;
	
	@ManagedProperty(value = "#{databasePanelBacking}")
	private DatabasePanelBacking databasePanelBacking;
	
	@ManagedProperty(value = "#{dbConnectionFactory}")
	private	DbConnectionFactory dbConnectionFactory;
	
	@ManagedProperty(value = "#{databaseLoaderService}")
	private DatabaseLoaderService databaseLoaderService;
	
	private List<DatabaseConfig> configList;
	private List<String> schemaList;
	private DatabaseConfig configBean;

	private List<ServiceResultMessage> unsufficientPermissions;

	@PostConstruct
	private void init() {
		reset();
	}
	
	public void deleteDatabaseConfig(DatabaseConfig config) {
		logDebug("delete databaseConfig " + config.getUrl());
		
		try{
			configService.deleteDatabaseConfig(config);
			showExtInfoInGui("Config deleted", config.getUrl());
			reset();
		}
		catch(ServiceException exception){
			handleServiceResultAsInfoMessage(exception);
			
		}
		
		databasePanelBacking.init();
	}
	
	public void addDatabaseConfig() {
		logDebug("add databaseConfig " + configBean.getUrl());
		
		try{
			configService.addDatabaseConfig(configBean);
			showExtInfoInGui("Config added", configBean.getUrl());
			reset();	
		}
		catch(ServiceException exception){
			handleServiceResultAsInfoMessage(exception);
			
		}

		databasePanelBacking.init();
		configBean = new DatabaseConfig();
	}
	
	public void testDatabaseConfig() {
		logDebug("test databaseConfig " + configBean.getUrl());
		
		unsufficientPermissions = null;
		
		try{
			configService.testDatabaseConfig(configBean);
			databaseLoaderService.connectDb(configBean);
			schemaList = databaseLoaderService.getSchemas();
			databaseLoaderService.disconnectDb();
			configBean.setDefaultSchema(schemaList.get(0));
			testSufficientPermissions();

		}
		catch(ServiceException exception){
			handleServiceResultAsInfoMessage(exception);
			
		}
	}
	
	public void testSufficientPermissions() {
		logDebug("Testing perimissions for " + configBean.getDefaultSchema());

		try{
			databaseLoaderService.connectDb(configBean);
			databaseLoaderService.testSufficientPermissions(configBean.getDefaultSchema());
			databaseLoaderService.disconnectDb();
			unsufficientPermissions = new ArrayList<ServiceResultMessage>();
		}
		catch(ServiceException exception){
			handleServiceResultAsInfoMessage(exception);
			unsufficientPermissions = exception.getResultMessages();
			
		}
	}

	public void updateDatabaseConfig() {
		logDebug("update databaseConfig " + configBean.getUrl());
		
		try{
			configService.updateDatabaseConfig(configBean);
			showExtInfoInGui("Config updated", configBean.getUrl());
			configBean = new DatabaseConfig();
			databasePanelBacking.init();
			redirectPageTo(NavigationCaseEnum.LIST_CONNECTION);
		}
		catch(ServiceException exception){
			handleServiceResultAsInfoMessage(exception);
			
		}
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
		databaseLoaderService.connectDb(configBean);
		schemaList = databaseLoaderService.getSchemas();
		databaseLoaderService.disconnectDb();
		redirectPageTo(NavigationCaseEnum.MODIFY_CONNECTION);
	}

	public void reset() {
		configList = null;
		schemaList = new ArrayList<>();
		configBean = new DatabaseConfig();
		unsufficientPermissions = new ArrayList<>();
	}
	
	public List<DatabaseConfig> getConfigList() {
		if(configList==null) {
			configList = configService.loadConnectionConfigs();
		}
		return configList;
	}

	public List<String> getSchemaList() {
		return schemaList;
	}

	public void setSchemaList(List<String> schemaList) {
		this.schemaList = schemaList;
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
	
	public void setDatabaseLoaderService(DatabaseLoaderService databaseLoaderService) {
		this.databaseLoaderService = databaseLoaderService;
	}

	public void setDatabasePanelBacking(DatabasePanelBacking databasePanelBacking) {
		this.databasePanelBacking = databasePanelBacking;
	}

	public void setDbConnectionFactory(DbConnectionFactory dbConnectionFactory) {
		this.dbConnectionFactory = dbConnectionFactory;
	}
	
	public List<ServiceResultMessage> getUnsufficientPermissions() {
		return unsufficientPermissions;
	}
	
}
