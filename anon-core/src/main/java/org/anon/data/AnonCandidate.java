package org.anon.data;

public class AnonCandidate {
	private String message;
	private int ocurrances = 0;
	private boolean active = true;

	public AnonCandidate(String message) {
		super();
		this.message = message;
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
	
	public void incOcurrances(){
		ocurrances++;
	}
	
	public int getOcurrances() {
		return ocurrances;
	}
	
	public void setOcurrances(int ocurrances) {
		this.ocurrances = ocurrances;
	}
}
