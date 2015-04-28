package org.anon.gui;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.Lookups;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.exec.where.WhereConditionBuilder;
import org.anon.exec.where.WhereConditionBuilder.Applicability;

@ManagedBean
@ViewScoped
public class ConfigureRowFilterPopupBacking extends BackingBase {
	
	
	@ManagedProperty(value="#{configContext}")
	private ConfigContext configContext;
	
	@ManagedProperty(value="#{whereConditionBuilder}")
	private WhereConditionBuilder whereConditionBuilder;
	
	private Applicability applicability;
	private String whereCondition;

	@PostConstruct
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
	
	public void onSubmitClicked() {
		AnonymisedColumnInfo col = configContext.getAnonymisedColumnInfo();
		col.setGuiFieldWhereCondition(whereCondition);
		col.setGuiFieldApplicability(applicability.name());
		col.setWhereCondition(whereConditionBuilder.build(applicability, whereCondition));
		
		// handle related tables
		for(RelatedTableColumnInfo relatedTableColumnInfo : configContext.getSelectedRelatedTableColumns()){
			
			AnonymisedColumnInfo relatedCol = Lookups.findTableColumn(relatedTableColumnInfo.getColumnName(), relatedTableColumnInfo.getTableName(), configContext.getAnonymisationMethod().getApplyedToColumns());
			relatedCol.setWhereCondition(whereConditionBuilder.buildForRelatedTable(applicability, whereCondition, relatedCol, col));
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

	public void setWhereConditionBuilder(WhereConditionBuilder whereConditionBuilder) {
		this.whereConditionBuilder = whereConditionBuilder;
	}

}
