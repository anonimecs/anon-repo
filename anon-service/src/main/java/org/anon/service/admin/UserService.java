package org.anon.service.admin;

import java.util.List;

import org.anon.persistence.data.SecurityRoleEnum;
import org.anon.persistence.data.SecurityUser;

public interface UserService {
	
	List<SecurityUser> getAllUser();
	
	void updateUser(SecurityUser user, String newPassword);
	
	void addNewUser(SecurityUser user);
	
	void deleteUser(SecurityUser user);
	
	boolean userHasRole(SecurityRoleEnum role);
	
	SecurityUser loadSecurityUser();
	
	String getUsername();
	
	boolean isHeadlessMode();

	SecurityUser loadUser(String username);

	SecurityUser loadAndAutheticateUser(String username, String password);
}
