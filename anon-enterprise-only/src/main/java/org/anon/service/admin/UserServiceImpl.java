package org.anon.service.admin;

import java.util.ArrayList;
import java.util.List;

import org.anon.persistence.dao.UserDao;
import org.anon.persistence.data.SecurityRoleEnum;
import org.anon.persistence.data.SecurityUser;
import org.anon.service.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	protected UserDao userDao;
	
	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Override
	public List<SecurityUser> getAllUser() {

		List<SecurityUser> users;
		
		if(userHasRole(SecurityRoleEnum.ROLE_ADMIN)) {
			users = userDao.loadAllUser();
		}
		else {
			users = new ArrayList<>();
			users.add(userDao.loadUserByUsername(getUsername()));
		}
		return users;
	}

	@Override
	@Transactional(readOnly = false)
	public void updateUser(SecurityUser user, String newPassword) throws ServiceException {
		
		try {
			if(newPassword!=null && !newPassword.isEmpty()) {
				user.setPassword(passwordEncoder.encode(newPassword));
				user.setEncrypted("Y");
			}
			userDao.updateUser(user);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new ServiceException("User not changed", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void addNewUser(SecurityUser user) throws ServiceException {
		
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setEnabled("Y");
			user.setEncrypted("Y");
			userDao.addUser(user);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new ServiceException("User not added", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteUser(SecurityUser user) throws ServiceException {
		
		try {
			userDao.deleteUser(user);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new ServiceException("User not removed", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage(), e);
		}
	}

	@Override
	public boolean userHasRole(SecurityRoleEnum role) {

		for(GrantedAuthority auth : getUserDetails().getAuthorities()) {
			if(role.getRoleType().equals(auth.getAuthority())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public SecurityUser loadSecurityUser() {
		return userDao.loadUserByUsername(getUsername());
	}

	@Override
	public String getUsername() {
		return getUserDetails().getUsername();
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
	
}
