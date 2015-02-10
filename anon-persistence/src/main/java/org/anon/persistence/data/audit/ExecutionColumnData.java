package org.anon.persistence.data.audit;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.anon.persistence.data.AnonymisedColumnData;

@Entity
public class ExecutionColumnData extends ExecAuditBaseData{
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;

	@Column 
	protected String resultText;
	
	@Column 
	protected Long runtimeSec;
	
	@ManyToOne @JoinColumn(name="ExecutionMethod_ID")
	private ExecutionMethodData executionMethodData;
	
	@ManyToOne @JoinColumn(name="COLUMN_ID")
	private AnonymisedColumnData anonymisedColumnData;
	
	@OneToMany(mappedBy="executionColumnData", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true) 
	protected List<ExecutionMessageData> messages = new LinkedList<>();


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResultText() {
		return resultText;
	}

	public void setResultText(String resultText) {
		this.resultText = resultText;
	}

	public Long getRuntimeSec() {
		return runtimeSec;
	}

	public void setRuntimeSec(Long runtimeSec) {
		this.runtimeSec = runtimeSec;
	}

	public ExecutionMethodData getExecutionMethodData() {
		return executionMethodData;
	}

	public void setExecutionMethodData(ExecutionMethodData executionMethodData) {
		this.executionMethodData = executionMethodData;
	}

	public AnonymisedColumnData getAnonymisedColumnData() {
		return anonymisedColumnData;
	}

	public void setAnonymisedColumnData(AnonymisedColumnData anonymisedColumnData) {
		this.anonymisedColumnData = anonymisedColumnData;
	}

	public List<ExecutionMessageData> getMessages() {
		return messages;
	}

	public void setMessages(List<ExecutionMessageData> messages) {
		this.messages = messages;
	}
	
	public void addMessage(ExecutionMessageData messageData){
		messages.add(messageData);
		messageData.setExecutionColumnData(this);
	}
	

	
	
	

	
}
