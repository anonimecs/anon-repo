package org.anon.gui;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.logic.AnonymisationMethod;

@ManagedBean
@SessionScoped
public class ConfigContext {


	private AnonymisationMethod anonymisationMethod;
	private AnonymisedColumnInfo anonymisedColumnInfo;
	private List<RelatedTableColumnInfo> selectedRelatedTableColumns;

	
	public AnonymisationMethod getAnonymisationMethod() {
		return anonymisationMethod;
	}
	public void setAnonymisationMethod(AnonymisationMethod anonymisationMethod) {
		this.anonymisationMethod = anonymisationMethod;
	}
	public AnonymisedColumnInfo getAnonymisedColumnInfo() {
		return anonymisedColumnInfo;
	}
	public void setAnonymisedColumnInfo(AnonymisedColumnInfo anonymisedColumnInfo) {
		this.anonymisedColumnInfo = anonymisedColumnInfo;
	}
	public List<RelatedTableColumnInfo> getSelectedRelatedTableColumns() {
		return selectedRelatedTableColumns;
	}
	public void setSelectedRelatedTableColumns(List<RelatedTableColumnInfo> selectedRelatedTableColumns) {
		this.selectedRelatedTableColumns = selectedRelatedTableColumns;
	}

	
	
}
