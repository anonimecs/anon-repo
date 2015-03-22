package org.anon.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.logic.AnonymisationMethod;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.service.DatabaseConfigService;
import org.anon.service.DatabaseLoaderService;

@ManagedBean
@SessionScoped
public class ReportBacking extends BackingBase {

	private List<ReportItem> reportList;
	
	private List<DatabaseConfig> dbConfigs;
	
	private Long selectedConnectionId;
	
	private DatabaseConfig selectedDatabase;
	
	private List<String> schemas;
	
	private String selectedSchema;
	
	@ManagedProperty(value="#{anonConfig}")
	private AnonConfig anonConfig;
	
	@ManagedProperty(value="#{databasePanelBacking}")
	private DatabasePanelBacking databasePanelBacking;
	
	@ManagedProperty(value="#{databaseLoaderService}")
	private DatabaseLoaderService databaseLoaderService;
	
	@ManagedProperty(value = "#{databaseConfigServiceImpl}")
	private DatabaseConfigService dbConfigService;
	
	public void init() {
		dbConfigs = dbConfigService.loadDatabaseConfigsForUser();
		if(!dbConfigs.isEmpty()) {
			selectedConnectionId = dbConfigs.get(0).getId();
		}
		databaseSelected();
	}
	
	public void databaseSelected() {
		databaseLoaderService.disconnectDb();
		
		for(DatabaseConfig config : dbConfigs) {
			if(config.getId().equals(selectedConnectionId)) {
				selectedDatabase = config;
				databaseLoaderService.connectDb(config);
				schemas = databaseLoaderService.getSchemas();
				databaseLoaderService.disconnectDb();
			}
		}
	}
	
	public void generateReport() {
		
		HashMap<String, ReportItem> reportMap = new HashMap<>();
		
		reportList = new ArrayList<>();
		
		databaseLoaderService.connectDb(selectedDatabase);
		databaseLoaderService.loadTableListFromTargetDb(selectedSchema);
		
		for(AnonymisationMethod method : anonConfig.getAnonMethods()) {
			for(AnonymisedColumnInfo col : method.getApplyedToColumns()) {
				
				String tabName = col.getTable().getName();
				if(reportMap.containsKey(tabName)) {
					reportMap.get(tabName).getCols().add(col);
				} else {
					ReportItem newItem = new ReportItem(col.getTable().getName());
					newItem.getCols().add(col);
					reportMap.put(tabName, newItem);
				}
				
			}
		}
		
		for(String key : reportMap.keySet()) {
			
			reportList.add(reportMap.get(key));
			
		}
		
		databaseLoaderService.disconnectDb();
		
	}
	
	public void onClickReport() {
		databasePanelBacking.disconnectDb();
		init();
		redirectPageTo(NavigationCaseEnum.REPORT);
	}

	public DatabaseConfig getSelectedDatabase() {
		return selectedDatabase;
	}

	public List<String> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}

	public String getSelectedSchema() {
		return selectedSchema;
	}

	public void setSelectedSchema(String selectedSchema) {
		this.selectedSchema = selectedSchema;
	}

	public List<DatabaseConfig> getDbConfigs() {
		return dbConfigs;
	}

	public void setDbConfigs(List<DatabaseConfig> dbConfigs) {
		this.dbConfigs = dbConfigs;
	}

	public Long getSelectedConnectionId() {
		return selectedConnectionId;
	}

	public void setSelectedConnectionId(Long selectedConnectionId) {
		this.selectedConnectionId = selectedConnectionId;
	}

	public List<ReportItem> getReportList() {
		return reportList;
	}

	public void setReportList(List<ReportItem> reportList) {
		this.reportList = reportList;
	}
	
	public void setDbConfigService(DatabaseConfigService dbConfigService) {
		this.dbConfigService = dbConfigService;
	}

	public void setDatabaseLoaderService(DatabaseLoaderService databaseLoaderService) {
		this.databaseLoaderService = databaseLoaderService;
	}

	public void setDatabasePanelBacking(DatabasePanelBacking databasePanelBacking) {
		this.databasePanelBacking = databasePanelBacking;
	}

	public void setAnonConfig(AnonConfig anonConfig) {
		this.anonConfig = anonConfig;
	}
	
	public class ReportItem  {
		
		public ReportItem(String tableName) {
			this.tableName = tableName;
			this.cols = new ArrayList<AnonymisedColumnInfo>();
		}
		
		private String tableName;
		
		private List<AnonymisedColumnInfo> cols;

		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		
		public List<AnonymisedColumnInfo> getCols() {
			return cols;
		}

		public void setCols(List<AnonymisedColumnInfo> cols) {
			this.cols = cols;
		}

		@Override
		public boolean equals(Object obj) {
		    if (obj == null) {
		        return false;
		    }
		    if (getClass() != obj.getClass()) {
		        return false;
		    }
		    final ReportItem other = (ReportItem) obj;
		    if ((this.tableName == null) ? (other.tableName != null) : !this.tableName.equals(other.tableName)) {
		        return false;
		    }
		    return true;
		}
	}
}
