package org.anon.gui.anonymise;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.anon.gui.BackingBase;
import org.anon.gui.ConfigContext;
import org.anon.service.EditedTableService;
import org.anon.service.RowFilterTestResult;
import org.anon.service.where.WhereConditionBuilder.Applicability;

@ManagedBean
@SessionScoped
public class RowFilterBacking extends BackingBase {
	
	
	@ManagedProperty(value="#{configContext}")
	private ConfigContext configContext;
	
	@ManagedProperty(value="#{editedTableService}")
	private EditedTableService editedTableService;

	private Applicability applicability;
	private String whereCondition;
	private RowFilterTestResult rowFilterTestResult;

	public void init() {
		whereCondition = "";
		rowFilterTestResult = null; 
		if(configContext.getAnonymisedColumnInfo().getGuiFieldWhereCondition() != null){
			whereCondition = configContext.getAnonymisedColumnInfo().getGuiFieldWhereCondition();
		}
		applicability = Applicability.APPLY;
		if(configContext.getAnonymisedColumnInfo().getGuiFieldApplicability() != null){
			applicability = Applicability.valueOf(configContext.getAnonymisedColumnInfo().getGuiFieldApplicability());
		}
		
	}
	
	public void onTestRowFilter() {
		try{
			rowFilterTestResult = editedTableService.testRowFilter(configContext.getAnonymisedColumnInfo(), whereCondition, applicability, configContext.getSelectedRelatedTableColumns(), configContext.getAnonymisationMethod());
			
		}
		catch(Exception e){
			logError("onTestRowFilter failed", e);
			showErrorInGui("Failed to test the specified where clause.");
			showExceptionInGui(e);
			rowFilterTestResult = null; 
		}
	}

	
	public void onSaveRowFilter() {
		try{
			editedTableService.saveRowFilter(configContext.getAnonymisedColumnInfo(), whereCondition, applicability, configContext.getSelectedRelatedTableColumns(), configContext.getAnonymisationMethod());
			showInfoInGui("Row filter saved for " + configContext.getAnonymisedColumnInfo());
			if(configContext.getSelectedRelatedTableColumns() != null){
				showInfoInGui(configContext.getSelectedRelatedTableColumns().size() + " related tables will also be filtered");
			}
		}
		catch(Exception e){
			logError("onSaveRowFilter failed", e);
			showErrorInGui("No changes were saved due to an error.");
			showExceptionInGui(e);			
		}
	}

	public void onDeleteRowFilter() {
		try{
			editedTableService.deleteRowFilter(configContext.getAnonymisedColumnInfo(), configContext.getSelectedRelatedTableColumns(), configContext.getAnonymisationMethod());
			showInfoInGui("Row filter deleted for " + configContext.getAnonymisedColumnInfo());
			if(configContext.getSelectedRelatedTableColumns() != null){
				showInfoInGui(configContext.getSelectedRelatedTableColumns().size() + " related table filters were also deleted");
			}
			whereCondition = "";
		}
		catch(Exception e){
			logError("onDeleteRowFilter failed", e);
			showErrorInGui("Delete failed due to an error.");
			showExceptionInGui(e);			
		}
	}


	public void setConfigContext(ConfigContext configContext) {
		this.configContext = configContext;
	}

	public Applicability getApplicability() {
		return applicability;
	}

	public void setApplicability(Applicability applicability) {
		this.applicability = applicability;
	}

	public String getWhereCondition() {
		return whereCondition;
	}

	public void setWhereCondition(String whereCondition) {
		this.whereCondition = whereCondition;
	}
	
	public void setEditedTableService(EditedTableService editedTableService) {
		this.editedTableService = editedTableService;
	}

	public RowFilterTestResult getRowFilterTestResult() {
		return rowFilterTestResult;
	}

}
