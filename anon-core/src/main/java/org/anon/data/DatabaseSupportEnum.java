package org.anon.data;

public enum DatabaseSupportEnum {

	ORACLE("Oracle", new String[]{"11g", "10x", "9y"}), 
	SYBASE("Sybase", new String[]{"15.7", "16.0"});

	private String name;
	private String[] versions;
	
	private DatabaseSupportEnum(String name, String[] versions) {
		this.name = name;
		this.versions = versions;
	}

	public String getName() {
		return name;
	}
	
	public String[] getVersions() {
		return versions;
	}
	
	public static String[] getVersionsByName(String name){
		for(DatabaseSupportEnum db : values()) {
			if(name.equals(db.name)){
				return db.versions;
			}
		}
		return null;
	}
}
