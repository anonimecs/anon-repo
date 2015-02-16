package org.anon;

import java.util.List;

import org.anon.persistence.dao.UserDao;
import org.anon.persistence.data.SecurityRole;
import org.anon.persistence.data.SecurityRoleEnum;
import org.anon.persistence.data.SecurityUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class UserDaoTest extends DbEntitiesTest {

	@Autowired
	UserDao userDao;
	
	@Test
	public void testLoadAll() {
		List<SecurityUser> users = userDao.loadAllUser();
		
		Assert.notNull(users);
		Assert.isTrue(users.size() > 0);
		Assert.isTrue(users.get(0).getRoles().size() > 0);
	}
	
	@Test
	public void testInsertNew() {
		
		SecurityRole roleGuest = new SecurityRole();
		roleGuest.setRole(SecurityRoleEnum.ROLE_GUEST);
		
		SecurityRole roleUser = new SecurityRole();
		roleUser.setRole(SecurityRoleEnum.ROLE_USER);
		
		SecurityUser user = new SecurityUser();
		user.setEnabled("N");
		user.setEncrypted("N");
		user.setUsername("Test");
		user.setPassword("Test");
		user.setSurname("Dieter");
		user.setName("JUnit");
		
		user.getRoles().add(roleUser);
		user.getRoles().add(roleGuest);
		
		userDao.addUser(user);

		Assert.isTrue(userDao.loadAllUser().size() == 2);
		Assert.isTrue(userDao.loadUserById(2L).getUsername().equals("Test"));
		Assert.isTrue(userDao.loadUserById(2L).getRoles().size() == 2);
	}
	
	@Test
	public void testDelete() {
		
		SecurityUser user = userDao.loadUserById(2L);
		Assert.notNull(user);
	
		userDao.deleteUser(user);
		Assert.isNull(userDao.loadUserById(2L));
	}
}
