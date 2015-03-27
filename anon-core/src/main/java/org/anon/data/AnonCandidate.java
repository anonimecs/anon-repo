package org.anon.data;

import java.util.ArrayList;
import java.util.List;

public class AnonCandidate {
	private String message;
	private boolean active = true;
	private List<DatabaseColumnInfo> columns = new ArrayList<>();

	public AnonCandidate() {}
	
	public AnonCandidate(String message) {
		super();
		this.message = message;
	}
	
	public List<DatabaseColumnInfo> getColumns() {
		return columns;
	}
	
	public void addColumn(DatabaseColumnInfo col){
		columns.add(col);
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
	
	@Override
	public int hashCode() {
		return message.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		AnonCandidate other = (AnonCandidate)obj;
		return message.equals(other.message);
	}
	
	
	public int getOcurrances() {
		return columns.size();
	}
	
}
