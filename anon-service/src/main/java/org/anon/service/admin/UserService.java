package org.anon.service.admin;

import java.util.List;

import org.anon.persistence.data.SecurityRoleEnum;
import org.anon.persistence.data.SecurityUser;

public interface UserService {
	
	public static final String BUILT_IN_ADMIN_USERNAME = "admin";
	
	List<SecurityUser> getAllUser();
	
	void updateUser(SecurityUser user, String newPassword);
	
	void addNewUser(SecurityUser user);
	
	void deleteUser(SecurityUser user);
	
	boolean userHasRole(SecurityRoleEnum role);
	
	SecurityUser loadSecurityUser();
	
	String getUsername();
	
	SecurityUser loadUser(String username);

	SecurityUser loadAndAutheticateUser(String username, String password);
}
