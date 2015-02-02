package org.anon.persistence.dao;

import java.util.List;

import org.anon.persistence.data.DatabaseConfig;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class DatabaseConfigDaoImpl implements DatabaseConfigDao {

	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DatabaseConfig> findAll() {
		List<DatabaseConfig> list = sessionFactory.getCurrentSession()
				.createQuery("from DatabaseConfig").list();
		return list;
	}

	@Override
	public void addDatabaseConfig(DatabaseConfig config) {
		Long id = (Long)sessionFactory.getCurrentSession().save(config);	
		config.setId(id);
	}

	@Override
	public void removeDatabaseConfig(DatabaseConfig config) {
		sessionFactory.getCurrentSession().delete(config);
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public DatabaseConfig loadConnectionConfig(String guiName) {
		return (DatabaseConfig) sessionFactory.getCurrentSession().
				createQuery("from DatabaseConfig where guiName=:guiName").setString("guiName", guiName).uniqueResult();
	}

	@Override
	public void updateDatabaseConifg(DatabaseConfig config) {
		sessionFactory.getCurrentSession().update(config);
	}
}
