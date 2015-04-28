package org.anon.logic;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;


public abstract class AnonymisationMethodDestory extends AnonymisationMethod implements RowFilterSupport{
	
	public AnonymisationMethodDestory() {
		super(AnonymizationType.DESTROY);
	}
	
	@Override
	public boolean supports(AnonymisedColumnInfo anonymizedColumn) {
		
		return true;
	}
	
	@Override
	protected String anonymiseString(String exampleValue) {
		if(exampleValue == null){
			return null;
		}
		if(exampleValue.length() == 0){
			return "x";
		}
		return exampleValue.replaceAll(".", "x");
	}
	
	@Override
	protected Double anonymiseDouble(Double exampleValue) {
		return 0.0;
	}
	
	@Override
	protected Long anonymiseLong(Long exampleValue) {
		return 0l;
	}
	
	@Override
	protected Object anonymiseDate(Date exampleValue) {
		return new Date();
	}
	
	@Override
	public List<AnonymisedColumnInfo> getApplyedToColumnsInExecutionOrder() {
		List<AnonymisedColumnInfo> res = super.getApplyedToColumnsInExecutionOrder();
		if(applyedToColumns.get(0).getWhereCondition() != null){
			Collections.reverse(res);
		}
		return res;
	}
}

