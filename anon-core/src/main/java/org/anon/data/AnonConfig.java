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
		clearMethodExecutions();
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
	
	/**
	 * gets the list of anonimised tables
	 */
	public int getAnonTableCount(){
		int res = 0;
		for(AnonymisationMethod anonymisationMethod:getAnonMethods()){
			res+= anonymisationMethod.getApplyedToColumns().size();
		}
		return res;
	}
	
	public void addTable(DatabaseTableInfo table){
		tables.add(table);
	}
	
	public void setAnonMethods(List<AnonymisationMethod> anonMethods) {
		this.anonMethods = anonMethods;
	}
	
	public void addAnonMethod(List<AnonymisationMethod> anonMethodList) {
		for(AnonymisationMethod method : anonMethodList) {
			addAnonMethod(method);
		}
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
		return Lookups.findTable(tableName, getTables());
	}
	
	public boolean emptyTables() {
		return tables == null || tables.isEmpty();
	}
	
	public MethodExecution getMethodExecution(AnonymisationMethod anonymisationMethod) {
		return methodExecutions.get(anonymisationMethod);
	}
	
	public void clearMethodExecutions(){
		methodExecutions = new HashMap<AnonymisationMethod, MethodExecution>();  
		for(AnonymisationMethod anonymisationMethod:anonMethods){
			methodExecutions.put(anonymisationMethod, new MethodExecution(anonymisationMethod));
		}
	}
}
