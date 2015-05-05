package org.anon.gui;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.anon.data.DatabaseTableInfo;
import org.anon.data.ReductionType;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.persistence.data.ReductionMethodData;
import org.anon.service.RowFilterTestResult;
import org.anon.service.reduce.ReduceService;


@ManagedBean
@SessionScoped
public class ReduceBacking extends BackingBase {
	
	@ManagedProperty(value="#{reduceService}")
	private ReduceService reduceService;
	
	private DatabaseTableInfo editedTable;
	private ReductionMethodData reductionMethodData;
	private ReductionType reductionType = ReductionType.TRUNCATE; 
	private int applicableRowCount;
	private String guiWhereCondition;

	private List<RelatedTableColumnInfo> selectedRelatedTables;

	private RowFilterTestResult rowFilterTestResult;

	
	
	
	
	public void onTableSelect(){
		 logDebug("Line Selected" + editedTable);

		 applicableRowCount = editedTable.getRowCount();
		 rowFilterTestResult = null;
		 // TODO
		 
		 redirectPageTo(NavigationCaseEnum.REDUCE);
	}
	
	public void onReductionTypeChanged(){
		logDebug("onReductionTypeChanged" + reductionType);

		// TODO
	}
	
	public void onTest(){
		try{
			rowFilterTestResult = reduceService.test(editedTable, guiWhereCondition, reductionType, selectedRelatedTables);
			
		}
		catch(Exception e){
			logError("onTest failed", e);
			showErrorInGui("Failed to test the specified where clause.");
			showExceptionInGui(e);
			//rowFilterTestResult = null; 
		}

	}
	
	public void onSave(){
		try{
			reductionMethodData = reduceService.save(editedTable, guiWhereCondition, reductionType, selectedRelatedTables);
			showInfoInGui("Reduction saved for " +editedTable.getName());
			if(selectedRelatedTables != null){
				showInfoInGui(selectedRelatedTables.size() + " related tables will also be reduced");
			}
		}
		catch(Exception e){
			logError("onSave failed", e);
			showErrorInGui("No changes were saved due to an error.");
			showExceptionInGui(e);			
		}
		
	}
	
	public void onDelete(){
		try{
			reduceService.delete(reductionMethodData);
			showInfoInGui("Reduction rule deleted for " +editedTable.getName());
			reductionMethodData = null;
			guiWhereCondition = "";
		}
		catch(Exception e){
			logError("onDelete failed", e);
			showErrorInGui("Delete failed due to an error.");
			showExceptionInGui(e);			
		}		
	}
	
	
	public DatabaseTableInfo getEditedTable() {
		return editedTable;
	}

	public void setEditedTable(DatabaseTableInfo editedTable) {
		this.editedTable = editedTable;
	}

	public ReductionType getReductionType() {
		return reductionType;
	}


	public void setReductionType(ReductionType reductionType) {
		this.reductionType = reductionType;
	}

	public int getApplicableRowCount() {
		return applicableRowCount;
	}

	public String getGuiWhereCondition() {
		return guiWhereCondition;
	}

	public void setGuiWhereCondition(String guiWhereCondition) {
		this.guiWhereCondition = guiWhereCondition;
	}

	public ReductionMethodData getReductionMethodData() {
		return reductionMethodData;
	}
	
	public void setReduceService(ReduceService reduceService) {
		this.reduceService = reduceService;
	}
	
	public RowFilterTestResult getRowFilterTestResult() {
		return rowFilterTestResult;
	}
	
}
