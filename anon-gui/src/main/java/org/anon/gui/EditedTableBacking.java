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
import org.anon.logic.AnonymisationMethodMapping;
import org.anon.logic.AnonymisationMethodNone;
import org.anon.logic.MethodFactory;
import org.anon.logic.map.MappingDefault;
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
	private List<AnonymisationMethod> supportedAnonymisationMethods;
	
	@ManagedProperty(value="#{configContext}")
	private ConfigContext configContext;

	@ManagedProperty(value="#{methodFactory}")
	private MethodFactory methodFactory;
	
	@ManagedProperty(value="#{editedTableService}")
	private EditedTableService editedTableService;
	
	@ManagedProperty(value="#{databaseLoaderService}")
	private DatabaseLoaderService databaseLoaderService;
	
	
	public void onSaveColumnSettings(){
		logDebug("onSaveColumnSettings" + editedColumn);
		// no anoymisation selected
		if(configContext.getAnonymisationMethod().getType() == AnonymizationType.NONE){
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
				editedTableService.addAnonymisation(editedTable, getAnonymizedColumn(), selectedRelatedTableColumns, configContext.getAnonymisationMethod());
				showInfoInGui("Anonymisation ADDED for " + getAnonymizedColumn() + " and selected related tables");
			}
			// change anonymisation
			else {
				editedTableService.changeAnonymisation(editedTable, getAnonymizedColumn(), selectedRelatedTableColumns, configContext.getAnonymisationMethod());
				showInfoInGui("Anonymisation CHANGED for " + getAnonymizedColumn() + " and selected related tables");
				
			}
			
		}
	}


	public void onTableSelect(SelectEvent event){
		 logDebug("Line Selected" + editedTable);
		 databaseLoaderService.fillExampleValues(editedTable);
		 editedColumn = null;
		 configContext.setAnonymisationMethod(null);	 
		 
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
			configContext.setAnonymisationMethod(AnonymisationMethodNone.INSTANCE);
			supportedAnonymisationMethods = methodFactory.getSupportedMethods(getAnonymizedColumn(), databaseLoaderService.getDatabaseSpecifics());
			selectedRelatedTableColumns = null;
			selectedRelatedTableColumnsToRemove = null;
			
		}else {
			// anonymised column clicked
			//selectedAnonymizationType = getAnonymizedColumn().getAnonymisationMethod().getType();
			configContext.setAnonymisationMethod(getAnonymizedColumn().getAnonymisationMethod());			
			selectedRelatedTableColumns = convertToRelatedTableInfoSelection(getAnonymizedColumn().getAnonymisationMethod().getApplyedToColumns());
			selectedRelatedTableColumnsToRemove = new LinkedList<RelatedTableColumnInfo>(selectedRelatedTableColumns);
			supportedAnonymisationMethods = methodFactory.getSupportedMethods(getAnonymizedColumn(), databaseLoaderService.getDatabaseSpecifics());
			replaceInSupported();
		}
		
		redirectPageTo(NavigationCaseEnum.ANONYMIZE);
	}
	


	private void replaceInSupported() {
		for(int index = 0; index < supportedAnonymisationMethods.size();index++){
			if(configContext.getAnonymisationMethod().getType() == supportedAnonymisationMethods.get(index).getType()){
				supportedAnonymisationMethods.remove(index);
				supportedAnonymisationMethods.add(index, configContext.getAnonymisationMethod());
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
		logDebug("anonymisatationTypeChanged" + configContext.getAnonymisationMethod());
		configContext.getAnonymisationMethod().setDataSource(databaseLoaderService.getDataSource());
		if(configContext.getAnonymisationMethod() instanceof AnonymisationMethodMapping){
			MappingDefault mappingDefault = new MappingDefault("");
			((AnonymisationMethodMapping)configContext.getAnonymisationMethod()).setMappingDefault(mappingDefault);
		}
	}



	public Object anonymiseValue(Object exampleValue){
		configContext.getAnonymisationMethod().setDataSource(databaseLoaderService.getDataSource());
		if(exampleValue == null){
			return null;
		}
		else if(configContext.getAnonymisationMethod().getType() == AnonymizationType.NONE){
			return exampleValue;
		}
		try {
			return configContext.getAnonymisationMethod().anonymise(exampleValue, getAnonymizedColumn());
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
		this.configContext.setAnonymisationMethod(anonymisationMethod);
	}
	
	public AnonymisationMethod getAnonymisationMethod() {
		return configContext.getAnonymisationMethod();
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
	
	public ConfigContext getConfigContext() {
		return configContext;
	}


	public void setConfigContext(ConfigContext configContext) {
		this.configContext = configContext;
	}

	
}
