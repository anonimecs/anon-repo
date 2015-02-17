package org.anon.data;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class DataObject implements Serializable{

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
	}
	
	protected void logDebug(Object message){
		Logger.getLogger(getClass()).log(Level.DEBUG, message);
	}
}
