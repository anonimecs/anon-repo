package org.anon.logic;

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
}
