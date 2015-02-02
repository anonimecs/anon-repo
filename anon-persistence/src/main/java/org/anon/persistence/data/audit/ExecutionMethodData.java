package org.anon.persistence.data.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.anon.persistence.data.AnonymisationMethodData;

@Entity
public class ExecutionMethodData extends ExecAuditBaseData{
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;

	@Column 
	protected String resultText;
	
	@Column 
	protected Long runtimeSec;
	
	@ManyToOne @JoinColumn(name="EXECUTION_ID")
	private ExecutionData executionData;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="METHOD_ID")
	private AnonymisationMethodData anonymisationMethodData;


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

	public ExecutionData getExecutionData() {
		return executionData;
	}

	public void setExecutionData(ExecutionData executionData) {
		this.executionData = executionData;
	}

	public AnonymisationMethodData getAnonymisationMethodData() {
		return anonymisationMethodData;
	}

	public void setAnonymisationMethodData(AnonymisationMethodData anonymisationMethodData) {
		this.anonymisationMethodData = anonymisationMethodData;
	}
	
	

	
}
