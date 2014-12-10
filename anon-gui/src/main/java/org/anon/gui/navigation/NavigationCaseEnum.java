package org.anon.gui.navigation;

public enum NavigationCaseEnum {
	
	HOME			("/",										"/app/"),
	LOGIN			("/pages/security/login.jsf",				"/login/"),
	CONNECT			("/pages/cockpit/connect.jsf",				"/app/connect/"),
	ADD_CONNECTION	("/pages/connection/connectionAdd.jsf",		"/app/connection-add/"),
	LIST_CONNECTION	("/pages/connection/connectionList.jsf",	"/app/connection-list/"),
	TABLES			("/pages/cockpit/databaseTableList.jsf",	"/app/tables-list/"),
	COLUMNS			("/pages/cockpit/databaseColumnList.jsf",	"/app/columns-list/"),
	ANONYMIZE		("/pages/cockpit/anonymize.jsf",			"/app/method/"),
	EXECUTE			("/pages/exec.jsf",							"/app/execute/");
	
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
