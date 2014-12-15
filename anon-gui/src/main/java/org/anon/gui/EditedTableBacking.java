package org.anon.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.AnonymisationMethodNone;
import org.anon.logic.MethodFactory;
import org.anon.service.DatabaseLoaderService;
import org.anon.service.EditedTableService;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;


@ManagedBean
@SessionScoped
public class EditedTableBacking extends BackingBase {
	
	private DatabaseTableInfo editedTable; 
	private DatabaseColumnInfo editedColumn;
	private List<RelatedTableColumnInfo> relatedTableColumns;
	private List<RelatedTableColumnInfo> filteredRelatedTableColumns;
	private List<RelatedTableColumnInfo> selectedRelatedTableColumns;
	private List<RelatedTableColumnInfo> selectedRelatedTableColumnsToRemove;
	private AnonymisationMethod anonymisationMethod = null;
	private List<AnonymisationMethod> supportedAnonymisationMethods;

	@ManagedProperty(value="#{methodFactory}")
	private MethodFactory methodFactory;
	
	@ManagedProperty(value="#{editedTableService}")
	private EditedTableService editedTableService;
	
	@ManagedProperty(value="#{databaseLoaderService}")
	private DatabaseLoaderService databaseLoaderService;
	
	
	public void onSaveColumnSettings(){
		logDebug("onSaveColumnSettings" + editedColumn);
		// no anoymisation selected
		if(anonymisationMethod.getType() == AnonymizationType.NONE){
			if(getAnonymizedColumn().getAnonymisationMethod() == null){
				// nothing to do here
				showInfoInGui("No change to save");
			}
			else {
				// remove existing method
				editedTableService.removeAnonymisation(editedTable, getAnonymizedColumn(), selectedRelatedTableColumnsToRemove);
				showInfoInGui("Anonymisation REMOVED for " + getAnonymizedColumn() + " and selected related tables");
			}
		}
		// anonymisation was selected
		else {
			// new anonymisation
			if(getAnonymizedColumn().getAnonymisationMethod() == null){
				editedTableService.addAnonymisation(editedTable, getAnonymizedColumn(), selectedRelatedTableColumns, anonymisationMethod);
				showInfoInGui("Anonymisation ADDED for " + getAnonymizedColumn() + " and selected related tables");
			}
			// change anonymisation
			else {
				editedTableService.changeAnonymisation(editedTable, getAnonymizedColumn(), selectedRelatedTableColumns, anonymisationMethod);
				showInfoInGui("Anonymisation CHANGED for " + getAnonymizedColumn() + " and selected related tables");
				
			}
			
		}
	}


	public void onTableSelect(SelectEvent event){
		 logDebug("Line Selected" + editedTable);
		 databaseLoaderService.fillExampleValues(editedTable);
		 editedColumn = null;
		 anonymisationMethod = null;	 
		 
		 redirectPageTo(NavigationCaseEnum.COLUMNS);
	}
	
	
	public void onColumnSelect(SelectEvent event) throws Exception{
		logDebug("column Selected" + editedColumn);
		
		relatedTableColumns = databaseLoaderService.findRelatedTables(editedTable, editedColumn);
		
		if(!(editedColumn instanceof AnonymisedColumnInfo)){
			//new column selected
			AnonymisedColumnInfo anonymisedColumn = new AnonymisedColumnInfo(editedColumn);
			editedColumn = anonymisedColumn;
			//selectedAnonymizationType = AnonymizationType.NONE;
			anonymisationMethod = AnonymisationMethodNone.INSTANCE;
			supportedAnonymisationMethods = methodFactory.getSupportedMethods(getAnonymizedColumn(), databaseLoaderService.getDatabaseSpecifics());
			selectedRelatedTableColumns = null;
			selectedRelatedTableColumnsToRemove = null;
			
		}else {
			// anonymised column clicked
			//selectedAnonymizationType = getAnonymizedColumn().getAnonymisationMethod().getType();
			anonymisationMethod = getAnonymizedColumn().getAnonymisationMethod();			
			selectedRelatedTableColumns = convertToRelatedTableInfoSelection(getAnonymizedColumn().getAnonymisationMethod().getApplyedToColumns());
			selectedRelatedTableColumnsToRemove = new LinkedList<RelatedTableColumnInfo>(selectedRelatedTableColumns);
			supportedAnonymisationMethods = methodFactory.getSupportedMethods(getAnonymizedColumn(), databaseLoaderService.getDatabaseSpecifics());
			replaceInSupported();
		}
		
		redirectPageTo(NavigationCaseEnum.ANONYMIZE);
	}
	


	private void replaceInSupported() {
		for(int index = 0; index < supportedAnonymisationMethods.size();index++){
			if(anonymisationMethod.getType() == supportedAnonymisationMethods.get(index).getType()){
				supportedAnonymisationMethods.remove(index);
				supportedAnonymisationMethods.add(index, anonymisationMethod);
				break;
			}
		}
		
	}



	private List<RelatedTableColumnInfo> convertToRelatedTableInfoSelection(
			List<AnonymisedColumnInfo> applyedToColumns) {
		List<RelatedTableColumnInfo> res = new ArrayList<>();
		for(AnonymisedColumnInfo anonCol : applyedToColumns){
			for(RelatedTableColumnInfo relatedCol:relatedTableColumns){
				if(relatedCol.sameAs(anonCol)){
					res.add(relatedCol);
				}
			}
		}
		
		return res;
	}



	public void onAnonymisatationTypeChanged(){
		logDebug("anonymisatationTypeChanged" + anonymisationMethod);
		anonymisationMethod.setDataSource(databaseLoaderService.getDataSource());
	}



	public Object anonymiseValue(Object exampleValue){
		anonymisationMethod.setDataSource(databaseLoaderService.getDataSource());
		if(exampleValue == null){
			return null;
		}
		else if(anonymisationMethod.getType() == AnonymizationType.NONE){
			return exampleValue;
		}
		try {
			return anonymisationMethod.anonymise(exampleValue, getAnonymizedColumn());
		} catch (Exception e) {
			Logger.getLogger(getClass()).log(Level.ERROR, "failed anonymiseValue: " + e.getLocalizedMessage(), e);
			return "FAILED:" + exampleValue;
		}
	}

	private AnonymisedColumnInfo getAnonymizedColumn() {
		return (AnonymisedColumnInfo) editedColumn;
	}

	public String concatExamples(List<?> exampleValues){
		if(exampleValues == null || exampleValues.isEmpty()){
			return null;
		}
		return exampleValues.toString();
	}

	
	public DatabaseTableInfo getEditedTable() {
		return editedTable;
	}


	public void setEditedTable(DatabaseTableInfo editedTable) {
		this.editedTable = editedTable;
	}


	public DatabaseColumnInfo getEditedColumn() {
		return editedColumn;
	}

	public void setEditedColumn(DatabaseColumnInfo editedColumn) {
		this.editedColumn = editedColumn;
	}


	public List<RelatedTableColumnInfo> getRelatedTableColumns() {
		return relatedTableColumns;
	}


	public void setRelatedTableColumns(
			List<RelatedTableColumnInfo> relatedTableColumns) {
		this.relatedTableColumns = relatedTableColumns;
	}


	public List<RelatedTableColumnInfo> getSelectedRelatedTableColumns() {
		return selectedRelatedTableColumns;
	}


	public void setSelectedRelatedTableColumns(
			List<RelatedTableColumnInfo> selectedRelatedTableColumns) {
		this.selectedRelatedTableColumns = selectedRelatedTableColumns;
	}



	public MethodFactory getMethodFactory() {
		return methodFactory;
	}



	public void setMethodFactory(MethodFactory methodFactory) {
		this.methodFactory = methodFactory;
	}




	public List<AnonymisationMethod> getSupportedAnonymisationMethods() {
		return supportedAnonymisationMethods;
	}



	public void setSupportedAnonymisationMethods(
			List<AnonymisationMethod> supportedAnonymisationMethods) {
		this.supportedAnonymisationMethods = supportedAnonymisationMethods;
	}

	public void setAnonymisationMethod(AnonymisationMethod anonymisationMethod) {
		this.anonymisationMethod = anonymisationMethod;
	}
	
	public AnonymisationMethod getAnonymisationMethod() {
		return anonymisationMethod;
	}



	public List<RelatedTableColumnInfo> getFilteredRelatedTableColumns() {
		return filteredRelatedTableColumns;
	}



	public void setFilteredRelatedTableColumns(List<RelatedTableColumnInfo> filteredRelatedTableColumns) {
		this.filteredRelatedTableColumns = filteredRelatedTableColumns;
	}



	public EditedTableService getEditedTableService() {
		return editedTableService;
	}



	public void setEditedTableService(EditedTableService editedTableService) {
		this.editedTableService = editedTableService;
	}

	public void setDatabaseLoaderService(DatabaseLoaderService databaseLoaderService) {
		this.databaseLoaderService = databaseLoaderService;
	}



	public List<RelatedTableColumnInfo> getSelectedRelatedTableColumnsToRemove() {
		return selectedRelatedTableColumnsToRemove;
	}



	public void setSelectedRelatedTableColumnsToRemove(List<RelatedTableColumnInfo> selectedRelatedTableColumnsToRemove) {
		this.selectedRelatedTableColumnsToRemove = selectedRelatedTableColumnsToRemove;
	}
	
	
}
