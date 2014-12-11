package org.anon.persistence.dao;

import java.util.List;

import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseTableData;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class EntitiesDaoImpl implements EntitiesDao {

	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void insert(DatabaseTableData databaseTableData) {
		Long id = (Long)sessionFactory.getCurrentSession().save(databaseTableData);
		databaseTableData.setId(id);
	}

	@Override
	public List<DatabaseTableData> loadAllTables() {
		@SuppressWarnings("unchecked")
		List<DatabaseTableData> list = sessionFactory.getCurrentSession().createQuery("from DatabaseTableData as table left join fetch table.columns").list();
		for (DatabaseTableData databaseTableInfo : list) {
			Hibernate.initialize(databaseTableInfo.getColumns());
		}
		return list;
		
	}
	
	@Override
	public void save(AnonymisationMethodData anonymisationMethodData){
		Long id = (Long)sessionFactory.getCurrentSession().save(anonymisationMethodData);
		anonymisationMethodData.setId(id);
	}

	@Override
	public List<AnonymisationMethodData> loadAllAnonMethods(DatabaseConfig databaseConfig) {
		@SuppressWarnings("unchecked")
		List<AnonymisationMethodData> list = sessionFactory.getCurrentSession().createQuery("from AnonymisationMethodData as method left join fetch method.applyedToColumns where method.databaseConfigId=:ID").setLong("ID", databaseConfig.getId()).list();
		for (AnonymisationMethodData data : list) {
			Hibernate.initialize(data.getApplyedToColumns());
		}
		return list;
	}

	@Override
	public int removeAnonymizedColumnData(String tableName, String columnName, String schemaName) {
		String hql = "delete from AnonymisedColumnData where COLUMNNAME= :COLUMNNAME and TABLENAME = :TABLENAME and SCHEMANAME = :SCHEMANAME" ;
		return sessionFactory.getCurrentSession().createQuery(hql).setString("TABLENAME", tableName).setString("COLUMNNAME", columnName).setString("SCHEMANAME", schemaName).executeUpdate();
		
	}

	@Override
	public int removeAnonymisationMethodData(Long id) {
		String hql = "delete from AnonymisationMethodData where ID= :ID";
		return sessionFactory.getCurrentSession().createQuery(hql).setLong("ID", id).executeUpdate();
		
	}
	
	
}
