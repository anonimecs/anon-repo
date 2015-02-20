package org.anon.service.admin;

import java.util.List;

import org.anon.persistence.data.SecurityRoleEnum;
import org.anon.persistence.data.SecurityUser;
import org.anon.service.ServiceException;

public interface UserService {
	
	List<SecurityUser> getAllUser();
	
	void updateUser(SecurityUser user, String newPassword) throws ServiceException;
	
	void addNewUser(SecurityUser user) throws ServiceException;
	
	void deleteUser(SecurityUser user) throws ServiceException;
	
	boolean userHasRole(SecurityRoleEnum role);
	
	String getUsername();
}
