package org.anon.exec;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.vendor.DatabaseSpecifics;

public abstract class BaseExecTest extends BaseDbTest{
	protected AnonConfig getTestAnonConfig(String colName, String colType, String tableName, AnonymisationMethod anonymisationMethod, DatabaseSpecifics databaseSpecifics) {
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		DatabaseTableInfo table = new DatabaseTableInfo();
		DatabaseColumnInfo databaseColumnInfo = new DatabaseColumnInfo(colName, colType, databaseSpecifics);
		table.addColumn(databaseColumnInfo);
		
		anonConfig.addTable(table);
		table.setName(tableName);
		AnonymisedColumnInfo anonymizedColumn = new AnonymisedColumnInfo(databaseColumnInfo);
		anonymisationMethod.addColumn(anonymizedColumn);
		table.addAnonymisedColumn(anonymizedColumn);
		
		anonConfig.addAnonMethod(anonymisationMethod);
		
		return anonConfig;
	}

    

}
