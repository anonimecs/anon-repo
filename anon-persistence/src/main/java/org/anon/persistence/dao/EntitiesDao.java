package org.anon.persistence.dao;

import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.DatabaseConfig;

public interface EntitiesDao {

	void save(AnonymisationMethodData anonymisationMethodData);

	void save(AnonymisedColumnData anonymisedColumnData);

	List<AnonymisationMethodData> loadAllAnonMethods(DatabaseConfig databaseConfig);

	int removeAnonymizedColumnData(Long id);

	int removeAnonymisationMethodData(Long id);

	AnonymisationMethodData loadAnonymisationMethodData(Long id);

	AnonymisedColumnData loadAnonymisedColumnData(AnonymisedColumnInfo anonymisedColumnInfo);

	boolean isEmptyAnonymisationMethod(Long id);

}
