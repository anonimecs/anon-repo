package org.anon.persistence.data;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

public abstract class PersistentObject  {

	@Override
	public String toString() {
		try{
			return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
		}
		catch(Exception e){
			String message = "toString failed on class " + getClass().getSimpleName();
			Logger.getLogger(getClass()).error(message, e);
			return message;
		}
	}
}
