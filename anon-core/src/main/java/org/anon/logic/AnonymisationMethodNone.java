package org.anon.logic;

import java.util.Date;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;

public class AnonymisationMethodNone extends AnonymisationMethod {

	public static AnonymisationMethodNone INSTANCE = new AnonymisationMethodNone();
	
	protected AnonymisationMethodNone() {
		super(AnonymizationType.NONE);
		
	}

	@Override
	public boolean supports(AnonymisedColumnInfo anonymizedColumn) {
		return true;
	}
	
	protected Object anonymiseDate(Date exampleValue) {
        return exampleValue;
    }



    protected Long anonymiseLong(Long exampleValue) {
        return exampleValue;
    }



    protected Double anonymiseDouble(Double exampleValue) {
        return exampleValue;
    }



    protected String anonymiseString(String exampleValue) {
        return exampleValue;
    }
}
