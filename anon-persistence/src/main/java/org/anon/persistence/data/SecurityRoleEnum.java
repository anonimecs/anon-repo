package org.anon.persistence.data;

public enum SecurityRoleEnum {

	ROLE_ADMIN(1,"ROLE_ADMIN","Administrator","Admin"),
	ROLE_USER(2,"ROLE_USER","User","User"),
	ROLE_GUEST(3,"ROLE_GUEST","Guest","Guest");
	
	private int role;
	private String roleType;
	private String roleName;
	private String shortName;
	
	SecurityRoleEnum(int role, String roleType, String roleName, String shortName) {
		this.role = role;
		this.roleName = roleName;
		this.roleType = roleType;
		this.shortName = shortName;
	}

	public int getRole() {
		return role;
	}

	public String getRoleType() {
		return roleType;
	}

	public String getRoleName() {
		return roleName;
	}

	public String getShortName() {
		return shortName;
	}
	
	public boolean equals(SecurityRoleEnum compare) {
		if(this.role == compare.getRole()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static SecurityRoleEnum getRoleByType(String type) {
		
		for(SecurityRoleEnum role : SecurityRoleEnum.values()) {
			if(role.getRoleType().equals(type)) {
				return role;
			}
		}
		return null;
	}
}
