package org.anon.gui.navigation;

public enum NavigationCaseEnum {
	
	HOME				("/",										"/app/"),
	LOGIN				("/pages/security/login.jsf",				"/login/"),
	
	CONNECT				("/pages/connection/connect.jsf",			"/app/connect/"),
	ADD_CONNECTION		("/pages/connection/connectionAdd.jsf",		"/app/connection-add/"),
	LIST_CONNECTION		("/pages/connection/connectionList.jsf",	"/app/connection-list/"),
	
	TABLES				("/pages/anonymise/databaseTableList.jsf",	"/app/tables-list/"),
	COLUMNS				("/pages/anonymise/databaseColumnList.jsf",	"/app/columns-list/"),
	ANONYMIZE			("/pages/anonymise/anonymize.jsf",			"/app/method/"),
	ROWFILTER			("/pages/anonymise/rowFilter.jsf",			"/app/row-filter/"),
	
	REDUCE_TABLES		("/pages/reduce/reduceTableList.jsf",		"/app/reduce-table-list/"),
	REDUCE				("/pages/reduce/reduce.jsf",				"/app/reduce/"),

	
	EXECUTE_REDUCTION	("/pages/exec/reductionExec.jsf",			"/app/reductionexec/"),
	EXECUTE				("/pages/exec/exec.jsf",					"/app/execute/"),
	EXE_AUDIT			("/pages/exec/execAudit.jsf",				"/app/execaudit/"),
	REPORT				("/pages/exec/report.jsf",					"/app/report/"),
	BACKUP				("/pages/admin/backup.jsf",					"/app/backup/"),
	
	ADD_USER			("/pages/admin/usersAdd.jsf",				"/app/user-add/"),
	EDIT_USER			("/pages/admin/usersEdit.jsf",				"/app/user-edit/"),
	USERS				("/pages/admin/users.jsf",					"/app/users/");
	
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
