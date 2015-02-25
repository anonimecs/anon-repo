package org.anon.persistence.dao;

import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.DatabaseConfig;
import org.anon.persistence.data.DatabaseTableData;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<AnonymisationMethodData> loadAllAnonMethods(DatabaseConfig databaseConfig) {
		List<AnonymisationMethodData> res = sessionFactory.getCurrentSession().createCriteria(AnonymisationMethodData.class).add(Restrictions.eq("databaseConfig", databaseConfig)).list();
		return res;
	}

	@Override
	public AnonymisationMethodData loadAnonymisationMethodData(Long id ){
		return (AnonymisationMethodData)sessionFactory.getCurrentSession().load(AnonymisationMethodData.class, id);
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

	@Override
	public AnonymisedColumnData loadAnonymisedColumnData(AnonymisedColumnInfo anonymisedColumnInfo) {
		String hql = "select col from AnonymisedColumnData col inner join col.anonymisationMethodData method "
				+ " where COLUMNNAME= :COLUMNNAME and TABLENAME = :TABLENAME and SCHEMANAME = :SCHEMANAME "
				+ " and method.id = :METHOD_ID";
		return (AnonymisedColumnData)sessionFactory.getCurrentSession().createQuery(hql)
				.setString("TABLENAME", anonymisedColumnInfo.getTable().getName())
				.setString("COLUMNNAME", anonymisedColumnInfo.getName())
				.setString("SCHEMANAME", anonymisedColumnInfo.getTable().getSchema())
				.setLong("METHOD_ID", anonymisedColumnInfo.getAnonymisationMethod().getId())
				.uniqueResult();
	}
	
	
}
