package org.anon.gui.navigation;

public enum NavigationCaseEnum {
	
	LOGIN("/pages/security/login.jsf"),
	CONNECT("/pages/cockpit/connect.jsf"),
	ADD_CONNECTION("/pages/connection/connectionAdd.jsf"),
	LIST_CONNECTION("/pages/connection/connectionList.jsf"),
	TABLES("/pages/cockpit/databaseTableList.jsf"),
	COLUMNS("/pages/cockpit/databaseColumnList.jsf"),
	ANONYMIZE("/pages/cockpit/anonymize.jsf");
	
	private NavigationCaseEnum(String url) {
		this.url = url;
	}
	
	private String url;
	
	public String getUrl() {
		return url;
	}
	
}
