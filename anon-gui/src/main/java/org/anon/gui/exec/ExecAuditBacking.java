package org.anon.gui.exec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.gui.BackingBase;
import org.anon.persistence.dao.AuditDao;
import org.anon.persistence.data.audit.ExecutionColumnData;
import org.anon.persistence.data.audit.ExecutionData;
import org.anon.persistence.data.audit.ExecutionMethodData;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

@ManagedBean
@ViewScoped
public class ExecAuditBacking extends BackingBase{

	@ManagedProperty(value="#{auditDaoImpl}")
	protected AuditDao auditDao;
	
	protected List<ExecAuditListTableRow> execAuditListTableRows = new ArrayList<>();
	protected List<ExecutionMethodDataPanelRow> executionMethodDataPanelRows = new ArrayList<>();
	
	@PostConstruct
	public void loadExecAuditListTableRows() {
		execAuditListTableRows = new ArrayList<>();
		List<ExecutionData> executionDatas = auditDao.loadExecutionDatas();
		for (ExecutionData executionData : executionDatas) {
			ExecAuditListTableRow auditListTableRow = new ExecAuditListTableRow(executionData);
			execAuditListTableRows.add(auditListTableRow);
		}
	}
	
	public void onAuditListTableRowSelect(ToggleEvent event) {
		
		if(event.getVisibility().equals(Visibility.VISIBLE)) {
			executionMethodDataPanelRows = new ArrayList<>();
			List<ExecutionMethodData> executionMethodDatas =auditDao.loadExecutionMethodDatas(((ExecAuditListTableRow)event.getData()).executionData);
			for (ExecutionMethodData executionMethodData : executionMethodDatas) {
				ExecutionMethodDataPanelRow executionMethodDataPanelRow = new ExecutionMethodDataPanelRow(executionMethodData);
				executionMethodDataPanelRow.setExecutionColumnDatas(auditDao.loadExecutionColumnDatas(executionMethodData));
				executionMethodDataPanelRows.add(executionMethodDataPanelRow);
			}
		}
	}


	public AuditDao getAuditDao() {
		return auditDao;
	}


	public void setAuditDao(AuditDao auditDao) {
		this.auditDao = auditDao;
	}


	public List<ExecAuditListTableRow> getExecAuditListTableRows() {
		return execAuditListTableRows;
	}


	public void setExecAuditListTableRows(List<ExecAuditListTableRow> execAuditListTableRows) {
		this.execAuditListTableRows = execAuditListTableRows;
	}

	public class ExecutionMethodDataPanelRow{
		ExecutionMethodData executionMethodData;
		List<ExecutionColumnData>executionColumnDatas;
		
		public ExecutionMethodDataPanelRow(ExecutionMethodData executionMethodData) {
			super();
			this.executionMethodData = executionMethodData;
		}

		public ExecutionMethodData getExecutionMethodData() {
			return executionMethodData;
		}

		public void setExecutionMethodData(ExecutionMethodData executionMethodData) {
			this.executionMethodData = executionMethodData;
		}

		public List<ExecutionColumnData> getExecutionColumnDatas() {
			return executionColumnDatas;
		}

		public void setExecutionColumnDatas(List<ExecutionColumnData> executionColumnDatas) {
			this.executionColumnDatas = executionColumnDatas;
		}
	}
	
	public class ExecAuditListTableRow{
		ExecutionData executionData;
	
		public ExecAuditListTableRow(ExecutionData executionData) {
			super();
			this.executionData = executionData;
		}
	
		public ExecutionData getExecutionData() {
			return executionData;
		}
	
		public void setExecutionData(ExecutionData executionData) {
			this.executionData = executionData;
		}
	
	}

	public List<ExecutionMethodDataPanelRow> getExecutionMethodDataPanelRows() {
		return executionMethodDataPanelRows;
	}

	public void setExecutionMethodDataPanelRows(List<ExecutionMethodDataPanelRow> executionMethodDataPanelRows) {
		this.executionMethodDataPanelRows = executionMethodDataPanelRows;
	}
}