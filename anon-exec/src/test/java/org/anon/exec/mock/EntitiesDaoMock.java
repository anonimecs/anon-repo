package org.anon.exec.mock;

import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.persistence.dao.EntitiesDao;
import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.DatabaseConfig;

public class EntitiesDaoMock implements EntitiesDao {

	@Override
	public void save(AnonymisationMethodData anonymisationMethodData) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<AnonymisationMethodData> loadAllAnonMethods(DatabaseConfig databaseConfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int removeAnonymizedColumnData(String tableName, String columnName, String schemaName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeAnonymisationMethodData(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AnonymisationMethodData loadAnonymisationMethodData(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnonymisedColumnData loadAnonymisedColumnData(AnonymisedColumnInfo anonymisedColumnInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
