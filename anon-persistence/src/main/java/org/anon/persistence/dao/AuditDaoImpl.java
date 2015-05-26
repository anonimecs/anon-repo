package org.anon.persistence.dao;

import java.util.List;

import org.anon.data.ReductionMethod;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.audit.ExecutionColumnData;
import org.anon.persistence.data.audit.ExecutionData;
import org.anon.persistence.data.audit.ExecutionMethodData;
import org.anon.persistence.data.audit.ReductionExecutionData;
import org.anon.persistence.data.audit.RefTableReductionExecutionData;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class AuditDaoImpl implements AuditDao {

	@Autowired
	private SessionFactory sessionFactory;


	
	@Override
	public void save(ExecutionData execution) {
		Long id = (Long)sessionFactory.getCurrentSession().save(execution);
		execution.setId(id);
	}



	@Override
	public void merge(ExecutionData executionData) {
		sessionFactory.getCurrentSession().merge(executionData);
		
	}



	@Override
	public void save(ExecutionMethodData executionMethodData) {
		Long id = (Long)sessionFactory.getCurrentSession().save(executionMethodData);
		executionMethodData.setId(id);
	}



	@Override
	public void save(ExecutionColumnData executionColumnData) {
		Long id = (Long)sessionFactory.getCurrentSession().save(executionColumnData);
		executionColumnData.setId(id);
	}


	
	@Override
	public ReductionExecutionData loadReductionExecutionData(ReductionMethod reductionMethod, ExecutionData executionData) {
		return (ReductionExecutionData)sessionFactory.getCurrentSession()
				.createQuery("from ReductionExecutionData where reductionMethodData = ? and executionData = ?")
				.setEntity(0, reductionMethod).setEntity(1, executionData)
				.uniqueResult();
	}

	@Override
	public ReductionExecutionData loadLastReductionExecutionData(ReductionMethod reductionMethod) {
		List<ReductionExecutionData> list = sessionFactory.getCurrentSession()
				.createQuery("from ReductionExecutionData where reductionMethodData = ? order by id desc")
				.setEntity(0, reductionMethod)
				.list();
		if(! list.isEmpty()){
			return list.get(0);
		}
		
		return null;
	}



	@Override
	public List<ExecutionData> loadExecutionDatas() {
		List<ExecutionData> list = sessionFactory.getCurrentSession().createCriteria(ExecutionData.class).list();
		for (ExecutionData executionData : list) {
			Hibernate.initialize(executionData.getDatabaseConfig());
		}
		return list;
	}



	@Override
	public List<ExecutionMethodData> loadExecutionMethodDatas(ExecutionData executionData) {
		List<ExecutionMethodData> list = sessionFactory.getCurrentSession().createQuery("from ExecutionMethodData where executionData = ?").setEntity(0, executionData).list();
		for (ExecutionMethodData executionMethodData : list) {
			Hibernate.initialize(executionMethodData.getAnonymisationMethodData());
		}
		return list;
	}
	
	@Override
	public List<ExecutionColumnData> loadExecutionColumnDatas(ExecutionMethodData executionMethodData) {
		List<ExecutionColumnData> list = sessionFactory.getCurrentSession().createQuery("from ExecutionColumnData where executionMethodData = ?").setEntity(0, executionMethodData).list();
		return list;
	}



	@Override
	public ExecutionColumnData getLastExecutionColumnData(AnonymisedColumnData anonymisedColumnData) {
		try {
			return (ExecutionColumnData)sessionFactory.getCurrentSession()
					.createQuery("from ExecutionColumnData where STATUS = 'ANONYMISED' and anonymisedColumnData = ? order by ID desc")
					.setEntity(0, anonymisedColumnData).list().get(0);
		} catch (Exception e) {
			return null;
		}
	}



	@Override
	public void save(ReductionExecutionData reductionExecutionData) {
		Long id = (Long)sessionFactory.getCurrentSession().save(reductionExecutionData);
		reductionExecutionData.setId(id);
		
	}



	@Override
	public void save(RefTableReductionExecutionData refTableReductionExecutionData) {
		Long id = (Long)sessionFactory.getCurrentSession().save(refTableReductionExecutionData);
		refTableReductionExecutionData.setId(id);
		
	}

}
