package org.anon.persistence.data.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.anon.persistence.data.ReductionMethodReferencingTableData;

@Entity
public class RefTableReductionExecutionData extends ExecAuditBaseData{
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;

	@Column 
	protected String resultText;
	
	@Column 
	protected Long runtimeSec;
	
	@ManyToOne @JoinColumn(name="REDUCTION_ID")
	private ReductionExecutionData reductionExecutionData;
	
	@ManyToOne @JoinColumn(name="REF_TABLE_ID")
	private ReductionMethodReferencingTableData reductionMethodReferencingTableData;
	

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

	public ReductionExecutionData getReductionExecutionData() {
		return reductionExecutionData;
	}
	public void setReductionExecutionData(ReductionExecutionData reductionExecutionData) {
		this.reductionExecutionData = reductionExecutionData;
	}
	

	public ReductionMethodReferencingTableData getReductionMethodReferencingTableData() {
		return reductionMethodReferencingTableData;
	}
	
	public void setReductionMethodReferencingTableData(
			ReductionMethodReferencingTableData reductionMethodReferencingTableData) {
		this.reductionMethodReferencingTableData = reductionMethodReferencingTableData;
	}
	
}
