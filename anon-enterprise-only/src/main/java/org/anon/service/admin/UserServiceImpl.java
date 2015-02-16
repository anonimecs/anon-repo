package org.anon.service.admin;

import java.util.List;

import org.anon.persistence.dao.UserDao;
import org.anon.persistence.data.SecurityUser;
import org.anon.service.ServiceResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

		List<SecurityUser> users = userDao.loadAllUser();
		
		return users;
	}

	@Override
	public ServiceResult updateUser(SecurityUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public ServiceResult addNewUser(SecurityUser user) {
		ServiceResult result = new ServiceResult();
		
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setEnabled("Y");
			user.setEncrypted("Y");
			userDao.addUser(user);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			result.addErrorMessage("User not added", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage());
		}
		return result;
	}

	@Override
	@Transactional(readOnly = false)
	public ServiceResult deleteUser(SecurityUser user) {
		ServiceResult result = new ServiceResult();
		
		try {
			userDao.deleteUser(user);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			result.addErrorMessage("User not removed", 
					e.getCause() != null ?  e.getCause().getMessage() : e.getMessage());
		}
		return result;
	}

}
