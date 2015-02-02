package org.anon.persistence.data.audit;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.anon.data.MethodExecution.Status;
import org.anon.persistence.data.PersistentObject;

@MappedSuperclass
public abstract class ExecAuditBaseData extends PersistentObject {
	
	@Column 
	protected String status;
	
	public Status getStatusEnum() {
		return status == null ?
				null:
				Status.valueOf(status);
	}

	public void setStatusEnum(Status status) {
		this.status = status == null ?
					null:
					status.toString();
	}



	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
