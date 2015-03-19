package org.anon.free;

import java.util.List;

import org.anon.persistence.dao.UserDao;
import org.anon.persistence.data.SecurityRoleEnum;
import org.anon.persistence.data.SecurityUser;
import org.anon.service.admin.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserServiceFreeImpl implements UserService {
	
	private static final Logger logger = Logger.getLogger(UserServiceFreeImpl.class);
	
	@Autowired
	protected UserDao userDao;
	
	@Override
	public List<SecurityUser> getAllUser() {

		throw new RuntimeException("Functionality is not supported in the free version");
	}

	@Override
	public void updateUser(SecurityUser user, String newPassword) {
		
		throw new RuntimeException("Functionality is not supported in the free version");
	}

	@Override
	public void addNewUser(SecurityUser user) {
		
		throw new RuntimeException("Functionality is not supported in the free version");
	}

	@Override
	public void deleteUser(SecurityUser user){
		
		throw new RuntimeException("Functionality is not supported in the free version");
	}

	@Override
	public boolean userHasRole(SecurityRoleEnum role) {

		return true;
	}
	
	@Override
	public SecurityUser loadSecurityUser() {
		return userDao.loadUserByUsername(getUsername());
	}

	@Override
	public String getUsername() {
		return (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	private UserDetails getUserDetails() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@Override
	public boolean isHeadlessMode() {
		if(SecurityContextHolder.getContext().getAuthentication() == null) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public SecurityUser loadUser(String username) {
		Assert.notNull(username, "username must not be null");
		return userDao.loadUserByUsername(username);
	}

	@Override
	public SecurityUser loadAndAutheticateUser(String username, String password) {
		throw new RuntimeException("Functionality is not supported in the free version");
	}
	
}
