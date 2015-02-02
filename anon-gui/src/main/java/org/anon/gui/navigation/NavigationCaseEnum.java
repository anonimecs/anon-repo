package org.anon.gui.navigation;

public enum NavigationCaseEnum {
	
	HOME				("/",										"/app/"),
	LOGIN				("/pages/security/login.jsf",				"/login/"),
	CONNECT				("/pages/cockpit/connect.jsf",				"/app/connect/"),
	ADD_CONNECTION		("/pages/connection/connectionAdd.jsf",		"/app/connection-add/"),
	MODIFY_CONNECTION	("/pages/connection/connectionEdit.jsf",	"/app/connection-modify/"),
	LIST_CONNECTION		("/pages/connection/connectionList.jsf",	"/app/connection-list/"),
	TABLES				("/pages/cockpit/databaseTableList.jsf",	"/app/tables-list/"),
	COLUMNS				("/pages/cockpit/databaseColumnList.jsf",	"/app/columns-list/"),
	ANONYMIZE			("/pages/cockpit/anonymize.jsf",			"/app/method/"),
	EXECUTE				("/pages/exec/exec.jsf",					"/app/execute/"),
	EXE_AUDIT			("/pages/exec/execAudit.jsf",				"/app/execaudit/");
	
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
