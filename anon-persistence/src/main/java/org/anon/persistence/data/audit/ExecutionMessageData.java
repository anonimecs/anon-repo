package org.anon.persistence.data.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.anon.persistence.data.PersistentObject;

@Entity
public class ExecutionMessageData extends PersistentObject{
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;

	@Column 
	protected String messageText;
	
	
	@ManyToOne @JoinColumn(name="ExecutionColumn_ID")
	protected ExecutionColumnData executionColumnData;

	
	public ExecutionMessageData(String messageText) {
		super();
		this.messageText = messageText;
	}



	public ExecutionMessageData() {}

	
	
	public String getMessageText() {
		return messageText;
	}


	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}


	public ExecutionColumnData getExecutionColumnData() {
		return executionColumnData;
	}


	public void setExecutionColumnData(ExecutionColumnData executionColumnData) {
		this.executionColumnData = executionColumnData;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}
	

	
}
