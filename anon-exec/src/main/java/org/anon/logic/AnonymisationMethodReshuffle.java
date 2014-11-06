package org.anon.logic;

import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;

public abstract class AnonymisationMethodReshuffle extends AnonymisationMethod {

	
	
	AnonymisationMethodReshuffle(){
		super(AnonymizationType.RESHUFFLE);

	}
	
	@Override
	public Object anonymise(Object exampleValue, AnonymisedColumnInfo anonymizedColumn) {
		List<?> allValues = anonymizedColumn.getExampleValues();
		int index = allValues.indexOf(exampleValue);
		int newIndex = (index + hashmodint) % allValues.size();
		if(newIndex == index){
			newIndex++;
		}
		if(newIndex >= allValues.size()){
			newIndex = 0;
		}
		return allValues.get(newIndex);
	}


}
