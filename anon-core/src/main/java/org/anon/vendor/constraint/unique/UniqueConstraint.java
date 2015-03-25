package org.anon.vendor.constraint.unique;

import java.util.List;

import org.anon.vendor.constraint.NamedConstraint;
import org.apache.commons.lang.StringUtils;

public abstract class UniqueConstraint extends NamedConstraint {

	protected List<String> columnNames;
	
	public boolean isMultiCol(){
		return columnNames.size() > 1;
	}
	
	public List<String> getColumNames() {
		return columnNames;
	}

	public String getColumNamesAsCommaSeparatedList() {
		return StringUtils.join(columnNames, ',');
	}

	
	public String getColumnName(){
		if(isMultiCol()){
			throw new RuntimeException("This is a multi col constraint");
		}
		return columnNames.get(0);
	}

	public boolean containsColumn(String columnName){
		return columnNames.contains(columnName);
	}
	
	@Override
	public String toString() {
		return "Unique " + getColumNamesAsCommaSeparatedList();
	}
}
