package org.anon.gui.navigation;

public enum NavigationCaseEnum {
	
	LOGIN			("/pages/security/login.jsf",				"/login/"),
	CONNECT			("/pages/cockpit/connect.jsf",				"/connect/"),
	ADD_CONNECTION	("/pages/connection/connectionAdd.jsf",		"/connection-add/"),
	LIST_CONNECTION	("/pages/connection/connectionList.jsf",	"/connection-list/"),
	TABLES			("/pages/cockpit/databaseTableList.jsf",	"/tables-list/"),
	COLUMNS			("/pages/cockpit/databaseColumnList.jsf",	"/columns-list/"),
	ANONYMIZE		("/pages/cockpit/anonymize.jsf",			"/method/"),
	EXECUTE			("/pages/exec.jsf",							"/execute/");
	
	private NavigationCaseEnum(String url, String path) {
		this.url = url;
		this.path = path;
	}
	
	private String url;
	private String path;
	
	public String getUrl() {
		return url;
	}
	
	public String getPath() {
		return path;
	}
	
}
