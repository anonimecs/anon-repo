package org.anon.logic;

import java.util.ArrayList;
import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.MySqlDbConnection;
import org.anon.vendor.OracleDbConnection;
import org.anon.vendor.SybaseDbConnection;
import org.springframework.stereotype.Service;

@Service
public class MethodFactory {
	

	@SuppressWarnings("unchecked")
	public static Class<AnonymisationMethod>[] SYBASE_METHODS = new Class[]{
		AnonymisationMethodNone.class
		,AnonymisationMethodEncryptSybase.class
		,AnonymisationMethodDestorySybase.class
		,AnonymisationMethodReshuffleSybase.class
	};
	
	@SuppressWarnings("unchecked")
	public static Class<AnonymisationMethod>[] ORACLE_METHODS = new Class[]{
		AnonymisationMethodNone.class
		,AnonymisationMethodEncryptOracle.class
		,AnonymisationMethodDestoryOracle.class
		,AnonymisationMethodReshuffleOracle.class
	};
	
	@SuppressWarnings("unchecked")
	public static Class<AnonymisationMethod>[] MYSQL_METHODS = new Class[]{
		AnonymisationMethodNone.class
		,AnonymisationMethodDestoryMySql.class
		,AnonymisationMethodEncryptMySql.class
	};

	public List<AnonymisationMethod> getSupportedMethods(AnonymisedColumnInfo anonymizedColumn, DatabaseSpecifics databaseSpecifics) throws Exception{
		List<AnonymisationMethod> supportedMethods = new ArrayList<>();
		
		Class<AnonymisationMethod>[] methods = null;
		
		if(databaseSpecifics == SybaseDbConnection.databaseSpecifics){
			methods = SYBASE_METHODS;
		}
		else if(databaseSpecifics == OracleDbConnection.databaseSpecifics){
			methods = ORACLE_METHODS;
		} 
		else if(databaseSpecifics == MySqlDbConnection.databaseSpecifics) {
			methods = MYSQL_METHODS;
		}
		
		for(Class<AnonymisationMethod> anonMethodClass:methods){
			AnonymisationMethod anonMethod = anonMethodClass.newInstance();
			if(anonMethod.supports(anonymizedColumn)){
				supportedMethods.add(anonMethod);				
			}
		}
		return supportedMethods;
	}
	
}
