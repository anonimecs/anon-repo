package org.anon.persistence.data.audit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ExecutionData extends ExecAuditBaseData{
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;
	
	@Column 
	protected String description;
	
	@Column
	protected Date startTime;
	
	@Column 
	protected Date endTime;
	
	@Column 
	protected String userName;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public void addDescription(String message) {
		if(message != null && !message.isEmpty()){
			description = description + " | " + message;
		}
		
	}



	
	
	
}
