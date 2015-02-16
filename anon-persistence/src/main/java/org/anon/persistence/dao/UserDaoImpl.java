package org.anon.persistence.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.anon.persistence.data.SecurityRole;
import org.anon.persistence.data.SecurityUser;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SecurityUser> loadAllUser() {
		List<SecurityUser> users = sessionFactory.getCurrentSession()
				.createQuery("from SecurityUser").list();
		return users;
	}

	@Override
	public SecurityUser loadUserById(Long id) {
		return (SecurityUser) sessionFactory.getCurrentSession()
				.createQuery("from SecurityUser where id=:id").setLong("id", id).uniqueResult();
	}

	@Override
	public void addUser(SecurityUser user) {
		
		Set<SecurityRole> roles = user.getRoles();
		user.setRoles(new HashSet<SecurityRole>(0));
		
		Long id = (Long) sessionFactory.getCurrentSession().save(user);
		user.setId(id);
		
		for(SecurityRole role : roles) {
			role.setUserId(id);
			role.setUser(user);
		}
		user.setRoles(roles);
		updateUser(user);
	}
	
	@Override
	public void updateUser(SecurityUser user) {
		sessionFactory.getCurrentSession().update(user);
	}

	@Override
	public void deleteUser(SecurityUser user) {
		sessionFactory.getCurrentSession().delete(user);
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
