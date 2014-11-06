package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;

/**
 * adds hash(password) to each character or number or special char
 */
public abstract class AnonymisationMethodEncrypt extends AnonymisationMethod {

	
	
	public AnonymisationMethodEncrypt(AnonymizationType type) {
		super(type);
		
		setPassword("defaultpassword");
		
		
	}



	@Override
	public boolean supports(AnonymisedColumnInfo anonymizedColumn){
		if(anonymizedColumn.isJavaTypeDate()){
			return false;
		}
		
		return true;
	}

	
	@Override
	protected String anonymiseString(String exampleValue) {
		char[] toEncode = exampleValue.toCharArray();
		
		for (int i = 0; i < toEncode.length; i++) {
	        if (Character.isLetter(toEncode[i])) {
	            toEncode[i] = (char)((toEncode[i] + hashmod - 'a') % 25 + 'a');
	        }
	        else if (Character.isDigit(toEncode[i])){
	        	toEncode[i] = (char)((toEncode[i] + hashmod) % 10); 
	        }
	        else {
	        	toEncode[i] = (char)(toEncode[i] + hashmod);
	        }
	    }
		
		return new String(toEncode);
	}
	
	@Override
	protected Double anonymiseDouble(Double exampleValue) {
		Double exampleDouble = (Double) exampleValue; 
		
		return exampleDouble + hashmod;
	}
	
	@Override
	protected Long anonymiseLong(Long exampleValue) {
		Long exampleLong = (Long) exampleValue; 
		
		return exampleLong + hashmod;
	}

	




	
	
}
