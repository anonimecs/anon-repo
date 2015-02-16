package org.anon.service.admin;

import java.util.List;

import org.anon.persistence.data.SecurityUser;
import org.anon.service.ServiceResult;

public interface UserService {
	
	List<SecurityUser> getAllUser();
	
	ServiceResult updateUser(SecurityUser user);
	
	ServiceResult addNewUser(SecurityUser user);
	
	ServiceResult deleteUser(SecurityUser user);
}
