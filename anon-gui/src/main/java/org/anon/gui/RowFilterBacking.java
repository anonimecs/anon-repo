package org.anon.gui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.anon.service.EditedTableService;
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

	public void init() {
		whereCondition = "";
		if(configContext.getAnonymisedColumnInfo().getGuiFieldWhereCondition() != null){
			whereCondition = configContext.getAnonymisedColumnInfo().getGuiFieldWhereCondition();
		}
		applicability = Applicability.APPLY;
		if(configContext.getAnonymisedColumnInfo().getGuiFieldApplicability() != null){
			applicability = Applicability.valueOf(configContext.getAnonymisedColumnInfo().getGuiFieldApplicability());
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
			logError("onSaveRowFilter failed", e);
			showErrorInGui("No changes were saved due to an error.");
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

}
