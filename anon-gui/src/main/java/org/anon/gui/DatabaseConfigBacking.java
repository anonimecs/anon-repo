package org.anon.gui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.data.Database;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseConnection;
import org.anon.service.DatabaseConfigService;
import org.anon.service.DatabaseLoaderService;
import org.anon.service.DbConnectionFactory;
import org.anon.service.ServiceException;
import org.anon.service.ServiceResultMessage;

@ManagedBean
@ViewScoped
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
	private DatabaseConfig databaseConfig;

	private DatabaseConnection selectedDatabaseConnection;
	private List<DatabaseConnection> databaseConnections;

	private DatabaseConnection newDatabaseConnection;
	
	private List<ServiceResultMessage> unsufficientPermissions;

	@PostConstruct
	private void init() {
		reset();
		databaseConnections = configService.loadDatabaseConnectionsForUser();
		if(!databaseConnections.isEmpty()){
			selectedDatabaseConnection = databaseConnections.get(0);
			testDatabaseConnection(selectedDatabaseConnection);
			testSufficientPermissions();
		}
	}

	
	public void onShowCreateNewConnDialog(){
		logDebug("onShowCreateNewConnDialog");
		newDatabaseConnection = new DatabaseConnection();
		schemaList = null;
	}

	public void onHideCreateNewConnDialog(){
		logDebug("not creating " + newDatabaseConnection);
		newDatabaseConnection = null;
		
	}
	
	public void onSaveConnectionButtonClicked(){
		logDebug("creating " + newDatabaseConnection);
		testDatabaseConnection(newDatabaseConnection);

		try{
			configService.addDatabaseConnection(newDatabaseConnection);
			databaseConnections = configService.loadDatabaseConnectionsForUser();
			selectedDatabaseConnection = newDatabaseConnection;
			testDatabaseConnection(selectedDatabaseConnection);
			showExtInfoInGui("Database connection created.", newDatabaseConnection.getGuiName());
		}
		catch(Exception e){
			logError("onSaveConnectionButtonClicked failed", e);
			showErrorInGui("Failed to save the new database connection.");
			showExceptionInGui(e);
		}
	}

	public void onChangePasswordPopup(DatabaseConnection databaseConnection) {
		logDebug("onChangePasswordPopup " + databaseConnection);
		selectedDatabaseConnection = databaseConnection;
	}
	
	public void onChangePassword() {
		logDebug("onChangePassword ");
		if(testDatabaseConnection(selectedDatabaseConnection)){
			configService.updateDatabaseConnection(selectedDatabaseConnection);
			showInfoInGui("Database password updated");
		}else {
			showErrorInGui("This password is invalid, and has not been saved");
		}
	}
	
	public void onSaveDatabaseConfig() {
			logDebug("add databaseConfig " + databaseConfig);
			
			try{
				databaseConfig.setDatabaseConnection(selectedDatabaseConnection);
				configService.addDatabaseConfig(databaseConfig);
				showExtInfoInGui("Config added", databaseConfig.getDatabaseConnection().getUrl());
				reset();	
			}
			catch(Exception e){
				logError("addDatabaseConfig failed", e);
				showExceptionInGui(e);
			}
	
			databasePanelBacking.init();
	//		databaseConfig = new DatabaseConfig();
		}

	public void deleteDatabaseConnection(DatabaseConnection databaseConnection) {
		logDebug("delete databaseConnection " + databaseConnection);
		
		try{
			configService.deleteDatabaseConnection(databaseConnection);
			databaseConnections = configService.loadDatabaseConnectionsForUser();
		}
		catch(Exception exception){
			showErrorInGui("Connection " +databaseConnection.getGuiName() + " could not be deleted");
			showExceptionInGui(exception);
			
		}
		
	}

	public void deleteDatabaseConfig(DatabaseConfig config) {
		logDebug("delete databaseConfig " + config.getConfigurationName());
		
		try{
			configService.deleteDatabaseConfig(config);
			showExtInfoInGui("Config deleted", config.getConfigurationName());
			reset();
		}
		catch(Exception exception){
			showErrorInGui("Configuration " +config.getConfigurationName() + " could not be deleted");
			showExceptionInGui(exception);
			
		}
		
		databasePanelBacking.init();
	}
	
	public boolean testDatabaseConnection(DatabaseConnection databaseConnectionToTest) {
		logDebug("testDatabaseConnection " + databaseConnectionToTest);
		
		try{
			
			configService.validateDatabaseConnection(databaseConnectionToTest);
			DatabaseConfig tempDatabaseConfig = new DatabaseConfig();
			tempDatabaseConfig.setDatabaseConnection(databaseConnectionToTest);
			
			databaseLoaderService.connectDb(tempDatabaseConfig);
			schemaList = databaseLoaderService.getSchemas();
			databaseLoaderService.disconnectDb();
			databaseConfig.setDefaultSchema(schemaList.get(0));
			return true;

		}
		catch(ServiceException exception){
			handleServiceResultAsInfoMessage(exception);
			return false;
		}
	}
	
	public void testSufficientPermissions() {
		logDebug("Testing perimissions for " + databaseConfig.getDefaultSchema());

		try{
			databaseConfig.setDatabaseConnection(selectedDatabaseConnection);
			databaseLoaderService.connectDb(databaseConfig);
			databaseLoaderService.testSufficientPermissions(databaseConfig.getDefaultSchema());
			databaseLoaderService.disconnectDb();
			unsufficientPermissions = new ArrayList<ServiceResultMessage>();
		}
		catch(ServiceException exception){
			handleServiceResultAsInfoMessage(exception);
			unsufficientPermissions = exception.getResultMessages();
			
		}
	}
	
	public boolean isConnectionReferenced(DatabaseConnection databaseConnection){
		for(DatabaseConfig databaseConfig:configList){
			if(databaseConfig.getDatabaseConnection().equals(databaseConnection)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isConfigInUse(DatabaseConfig config) {
		
		if(dbConnectionFactory.getDatabaseConfig() != null && 
				dbConnectionFactory.getDatabaseConfig().equals(config)) {
			return true;
		}
		return false;
	}
	
	public void reset() {
		configList = configService.loadDatabaseConfigsForUser();
		schemaList = new ArrayList<>();
		databaseConfig = new DatabaseConfig();
		unsufficientPermissions = new ArrayList<>();
	}
	
	public List<DatabaseConfig> getConfigList() {
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

	public DatabaseConfig getDatabaseConfig(){
		return databaseConfig;
	}

	public void setDatabaseConfig(DatabaseConfig configBean) {
		this.databaseConfig = configBean;
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

	public DatabaseConnection getNewDatabaseConnection() {
		return newDatabaseConnection;
	}

	public void setNewDatabaseConnection(DatabaseConnection newDatabaseConnection) {
		this.newDatabaseConnection = newDatabaseConnection;
	}

	public DatabaseConnection getSelectedDatabaseConnection() {
		return selectedDatabaseConnection;
	}

	public void setSelectedDatabaseConnection(DatabaseConnection selectedDatabaseConnection) {
		this.selectedDatabaseConnection = selectedDatabaseConnection;
	}

	public List<DatabaseConnection> getDatabaseConnections() {
		return databaseConnections;
	}

	public void setDatabaseConnections(List<DatabaseConnection> databaseConnections) {
		this.databaseConnections = databaseConnections;
	}
	
}
