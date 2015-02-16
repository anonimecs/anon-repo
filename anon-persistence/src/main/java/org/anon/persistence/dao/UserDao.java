package org.anon.persistence.dao;

import java.util.List;

import org.anon.persistence.data.SecurityUser;

public interface UserDao {

	List<SecurityUser> loadAllUser();
	
	SecurityUser loadUserById(Long id);
	
	void addUser(SecurityUser user);
	
	void updateUser(SecurityUser user);
	
	void deleteUser(SecurityUser user);
}
