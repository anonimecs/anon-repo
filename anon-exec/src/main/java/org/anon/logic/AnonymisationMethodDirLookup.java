package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;

/**
 * adds hash(password) to each character or number or special char
 */
public class AnonymisationMethodDirLookup extends AnonymisationMethod {

	public AnonymisationMethodDirLookup(AnonymisedColumnInfo anonymizedColumn) {
		super(AnonymizationType.DIR_LOOKUP);
	}
	
	
	@Override
	protected String anonymiseString(String exampleValue) {
		return "TODO: Lookup Result from a directory";
	}
}
