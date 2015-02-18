package org.anon.logic;

import java.util.ArrayList;
import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.vendor.DatabaseSpecifics;
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
		,AnonymisationMethodReshuffleMySql.class
	};

	@SuppressWarnings("unchecked")
	public static Class<AnonymisationMethod>[] SQLSERVER_METHODS = new Class[]{
		AnonymisationMethodNone.class
		,AnonymisationMethodDestorySqlServer.class
		,AnonymisationMethodEncryptSqlServer.class
		,AnonymisationMethodReshuffleMySql.class
	};

	public List<AnonymisationMethod> getSupportedMethods(AnonymisedColumnInfo anonymizedColumn, DatabaseSpecifics databaseSpecifics) throws Exception{
		List<AnonymisationMethod> supportedMethods = new ArrayList<>();
		
		Class<AnonymisationMethod>[] methods = null;
		
		if(databaseSpecifics == DatabaseSpecifics.SybaseSpecific){
			methods = SYBASE_METHODS;
		}
		else if(databaseSpecifics == DatabaseSpecifics.OracleSpecific){
			methods = ORACLE_METHODS;
		} 
		else if(databaseSpecifics == DatabaseSpecifics.MySqlSpecific) {
			methods = MYSQL_METHODS;
		}
		else if(databaseSpecifics == DatabaseSpecifics.SqlServerSpecific) {
			methods = SQLSERVER_METHODS;
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
