package org.anon.logic;

import java.util.Date;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;


public abstract class AnonymisationMethodDestory extends AnonymisationMethod {
	
	public AnonymisationMethodDestory() {
		super(AnonymizationType.DESTROY);
	}
	
	@Override
	public boolean supports(AnonymisedColumnInfo anonymizedColumn) {
		
		return true;
	}
	
	@Override
	protected String anonymiseString(String exampleValue) {
		return "x";
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
}

