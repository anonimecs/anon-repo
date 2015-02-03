package org.anon.gui;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.service.DatabaseConfigService;
import org.anon.service.DatabaseLoaderService;

@ManagedBean
@SessionScoped
public class DatabasePanelBacking extends BackingBase{

	
	@ManagedProperty(value = "#{databaseConfigServiceImpl}")
	private DatabaseConfigService dbConfigService;
	
	@ManagedProperty(value = "#{editedTableBacking}")
	private EditedTableBacking editedTableBacking;

	@ManagedProperty(value="#{databaseLoaderService}")
	private DatabaseLoaderService databaseLoaderService;
	
	private int tableCount;
	private List<DatabaseTableInfo> tableList;
	private boolean connected;
	
	private List<DatabaseTableInfo> filteredTables;
	
	private List<DatabaseConfig> dbConfigs;	
	private Long selectedConnectionId;
	
    private String selectedSchema;  
    private List<String> schemas;

	@PostConstruct
	public void init(){
		
		if(connected){
			disconnectDb();
		}
		
		tableCount = 0;
		connected = false;
		
		dbConfigs = dbConfigService.loadConnectionConfigs();
		if(!dbConfigs.isEmpty()) {
			selectedConnectionId = dbConfigs.get(0).getId();
		}
	}

	public void connectDb(){
		
		DatabaseConfig databaseConfig = getDatabaseConfig();
		logDebug("connecting to "  + databaseConfig.toString());
		databaseLoaderService.connectDb(databaseConfig);

		selectedSchema = databaseConfig.getDefaultSchema();
		schemas = databaseLoaderService.getSchemas();
		loadDatabaseTableList();
		connected = true;
	}
	
	public void disconnectDb() {
		
		logDebug("disconnecting database " + getDatabaseConfig().toString());
		databaseLoaderService.disconnectDb();
		tableCount = 0;
		tableList = null;
		connected = false;
		editedTableBacking.setEditedTable(null);
		editedTableBacking.setEditedColumn(null);
	}
	
	public void connectRedirect() {
		connectDb();		
		redirectPageTo(NavigationCaseEnum.TABLES);
	}
	
	public void disconnectRedirect() {
		disconnectDb();
		redirectPageTo(NavigationCaseEnum.CONNECT);
	}
	
	public void onSchemaChanged(){
		logDebug("selected schema " + selectedSchema);
		databaseLoaderService.schemaChanged();
		loadDatabaseTableList();
		editedTableBacking.setEditedTable(null);
	}

	public void loadDatabaseTableList() {
		tableCount = databaseLoaderService.getTableCount(selectedSchema); 
		tableList = databaseLoaderService.loadTableListFromTargetDb(selectedSchema);
		List<String> errors = databaseLoaderService.getLoadErrors();
		if(errors!=null && !errors.isEmpty()){
			for (String error : errors) {
				showErrorInGui(error);
			}
		}
	}
	
	public String concatColumns(List<AnonymisedColumnInfo> anonymisedColumns){
		if(anonymisedColumns == null || anonymisedColumns.isEmpty()){
			return null;
		}
		else{
			StringBuffer sb = new StringBuffer();
			for(AnonymisedColumnInfo anonymisedColumnInfo:anonymisedColumns){
				sb.append(anonymisedColumnInfo.getName())
				.append(" ");
			}
			return sb.toString();
		}
	}
	

	public int getTableCount() {
		return tableCount;
	}

	public void setTableCount(int tableCount) {
		this.tableCount = tableCount;
	}
	
	public DatabaseConfig getDatabaseConfig() {		
		for(DatabaseConfig conf : dbConfigs){
			if(conf.getId() == selectedConnectionId)
				return conf;
		}
		return null;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	
	public void setDbConfigService(DatabaseConfigService dbConfigService) {
		this.dbConfigService = dbConfigService;
	}

	public void setEditedTableBacking(EditedTableBacking editedTableBacking) {
		this.editedTableBacking = editedTableBacking;
	}
	
	public EditedTableBacking getEditedTableBacking() {
		return editedTableBacking;
	}

	public List<DatabaseTableInfo> getFilteredTables() {
		return filteredTables;
	}

	public void setFilteredTables(List<DatabaseTableInfo> filteredTables) {
		this.filteredTables = filteredTables;
	}

	public List<DatabaseConfig> getDbConfigs() {
		return dbConfigs;
	}

	public Long getSelectedConnectionId() {
		return selectedConnectionId;
	}

	public void setSelectedConnectionId(Long selectedConnectionId) {
		this.selectedConnectionId = selectedConnectionId;
	}
	
	public void setDatabaseLoaderService(DatabaseLoaderService databaseLoaderService) {
		this.databaseLoaderService = databaseLoaderService;
	}
	public List<String> getSchemas() {
		return schemas;
	}

	public String getSelectedSchema() {
		return selectedSchema;
	}
	
	public void setSelectedSchema(String selectedSchema) {
		this.selectedSchema = selectedSchema;
	}
	
	public List<DatabaseTableInfo> getTableList() {
		return tableList;
	}
	
	public void setTableList(List<DatabaseTableInfo> tableList) {
		this.tableList = tableList;
	}
}
