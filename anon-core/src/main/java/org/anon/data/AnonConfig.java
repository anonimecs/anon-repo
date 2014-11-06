package org.anon.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.anon.logic.AnonymisationMethod;


public class AnonConfig {

	private List<DatabaseTableInfo> tables;
	private List<AnonymisationMethod> anonMethods;
	private Map<AnonymisationMethod, MethodExecution> methodExecutions;  


	@PostConstruct
	public void init(){
		tables = new LinkedList<>();
		anonMethods = new LinkedList<>();
		methodExecutions = new HashMap<AnonymisationMethod, MethodExecution>();  
	}
	
	public List<DatabaseTableInfo> getTables() {
		return tables;
	}
	public void setTables(List<DatabaseTableInfo> tables) {
		this.tables = tables;
	}
	public List<AnonymisationMethod> getAnonMethods() {
		return anonMethods;
	}
	
	public void addTable(DatabaseTableInfo table){
		tables.add(table);
	}
	
	public void setAnonMethods(List<AnonymisationMethod> anonMethods) {
		this.anonMethods = anonMethods;
	}
	
	public void addAnonMethod(AnonymisationMethod anonymisationMethod) {
		anonMethods.add(anonymisationMethod);
		methodExecutions.put(anonymisationMethod, new MethodExecution(anonymisationMethod));
	}
	
	public void removeAnonMethod(AnonymisationMethod anonymisationMethod) {
		
		boolean removed = anonMethods.remove(anonymisationMethod);
		if(!removed){
			throw new RuntimeException("Failed to remove " + anonymisationMethod);
		}
		methodExecutions.remove(anonymisationMethod);

		
	}
	public DatabaseTableInfo findTable(String tableName) {
		for(DatabaseTableInfo aDatabaseTableInfo:getTables()){
			if(tableName.equals(aDatabaseTableInfo.getName())){
				return aDatabaseTableInfo;
			}
		}
		throw new RuntimeException("Table not found " + tableName);
	}
	public boolean emptyTables() {
		return tables == null || tables.isEmpty();
	}
	
	public MethodExecution getMethodExecution(AnonymisationMethod anonymisationMethod) {
		return methodExecutions.get(anonymisationMethod);
	}
	
	
}
