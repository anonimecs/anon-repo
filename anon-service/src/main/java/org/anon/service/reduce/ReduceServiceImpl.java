package org.anon.service.reduce;

import java.util.List;

import org.anon.data.DatabaseTableInfo;
import org.anon.data.ReductionType;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.persistence.dao.EntitiesDao;
import org.anon.persistence.data.ReductionMethodData;
import org.anon.service.DbConnectionFactory;
import org.anon.service.RowFilterTestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="reduceService")
public class ReduceServiceImpl implements ReduceService {

	@Autowired EntitiesDao entitiesDao;
	@Autowired DbConnectionFactory dbConnectionFactory;

	
	@Override
	public RowFilterTestResult test(DatabaseTableInfo editedTable, String guiWhereCondition,
			ReductionType reductionType, List<RelatedTableColumnInfo> selectedRelatedTables) {
		RowFilterTestResult res = new RowFilterTestResult();

		return res;
	}

	@Override
	public ReductionMethodData save(DatabaseTableInfo editedTable, String guiWhereCondition, ReductionType reductionType, List<RelatedTableColumnInfo> selectedRelatedTables) {
		
		ReductionMethodData reductionMethodData = new ReductionMethodData();
		reductionMethodData.setDatabaseConfigId(dbConnectionFactory.getDatabaseConfig().getId());
		reductionMethodData.setReductionType(reductionType);
		reductionMethodData.setSchemaName(editedTable.getSchema());
		reductionMethodData.setTableName(editedTable.getName());
		reductionMethodData.setWhereCondition(guiWhereCondition);

		entitiesDao.save(reductionMethodData);
		
		return reductionMethodData;
		
	}
	
	@Override
	public void delete(ReductionMethodData reductionMethodData) {
		entitiesDao.removeReductionMethodData(reductionMethodData);
		
	}

}
