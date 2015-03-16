package org.anon.persistence.dao;

import java.util.List;

import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseConnection;
import org.anon.persistence.data.SecurityUser;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class DatabaseConfigDaoImpl implements DatabaseConfigDao {

	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DatabaseConfig> findAllDatabaseConfigs() {
		List<DatabaseConfig> list = sessionFactory.getCurrentSession().createQuery("from DatabaseConfig").list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DatabaseConnection> findAllDatabaseConnections() {
		List<DatabaseConnection> list = sessionFactory.getCurrentSession().createQuery("from DatabaseConnection").list();
		return list;
	}

	@Override
	public void addDatabaseConfig(DatabaseConfig config) {
		Long id =  (Long) sessionFactory.getCurrentSession().save(config);	
		config.setId(id);
	}

	@Override
	public void addDatabaseConnection(DatabaseConnection databaseConnection) {
		Long id =  (Long) sessionFactory.getCurrentSession().save(databaseConnection);	
		databaseConnection.setId(id);
	}

	@Override
	public void removeDatabaseConnection(DatabaseConnection databaseConnection) {
		sessionFactory.getCurrentSession().delete(databaseConnection);
	}
	
	@Override
	public void removeDatabaseConfig(DatabaseConfig config) {
		config.setDatabaseConnection(null); // do not delete the connection
		sessionFactory.getCurrentSession().delete(config);
	}
	
	@Override
	public void removeDatabaseConfig(String configurationName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DatabaseConfig.class).add(Restrictions.eq("configurationName", configurationName));
		DatabaseConfig config = (DatabaseConfig)criteria.uniqueResult();
		removeDatabaseConfig(config);
		
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public DatabaseConfig loadDatabaseConfig(String configurationName) {
		return (DatabaseConfig) sessionFactory.getCurrentSession().
				createQuery("from DatabaseConfig where configurationName=:configurationName").setString("configurationName", configurationName).uniqueResult();
	}

	@Override
	public DatabaseConnection loadDatabaseConnection(String guiName) {
		return (DatabaseConnection) sessionFactory.getCurrentSession().
				createQuery("from DatabaseConnection where guiName=:guiName").setString("guiName", guiName).uniqueResult();
	}

	@Override
	public void updateDatabaseConfig(DatabaseConfig config) {
		sessionFactory.getCurrentSession().update(config);
	}

	@Override
	public void updateDatabaseConnection(DatabaseConnection databaseConnection) {
		sessionFactory.getCurrentSession().update(databaseConnection);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DatabaseConfig> findConfigForUser(String username) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DatabaseConfig.class, "config");
		criteria.createAlias("config.assingendUser", "user");
		criteria.add(Restrictions.eq("user.username", username));
		
		return criteria.list();
	}

	@Override
	public boolean isConfigNameUnique(String configurationName, Long id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DatabaseConfig.class);
		criteria.add(Restrictions.eq("configurationName", configurationName));
		if(id != null) {
			criteria.add(Restrictions.ne("id", id));
		}
		return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult() > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DatabaseConnection> findAllDatabaseConnectionsForUser(SecurityUser securityUser) {
		return sessionFactory.getCurrentSession().
			createQuery("from DatabaseConnection c where c.securityUser=:securityUser").setEntity("securityUser", securityUser).list();
	}
}
