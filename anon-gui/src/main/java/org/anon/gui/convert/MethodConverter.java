package org.anon.gui.convert;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.anon.data.AnonymizationType;
import org.anon.gui.anonymise.AnonymiseBacking;
import org.anon.logic.AnonymisationMethod;

@FacesConverter("methodConverter")
public class MethodConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
        	AnonymizationType type = AnonymizationType.valueOf(value);
       
            for(AnonymisationMethod anonymisationMethod: getSupportedAnonymisationMethods(fc)){
            	if(type == anonymisationMethod.getType()){
            		return anonymisationMethod;
            	}
            }
        }
       
        return null;
        
    }
 
    private List<AnonymisationMethod> getSupportedAnonymisationMethods(FacesContext fc) {
    	AnonymiseBacking anonymiseBacking = (AnonymiseBacking) fc.getApplication().
    			evaluateExpressionGet(fc, "#{anonymiseBacking}", AnonymiseBacking.class);
    	
		return anonymiseBacking.getSupportedAnonymisationMethods();
	}

	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((AnonymisationMethod) object).getType());
        }
        else {
            return null;
        }
    }   

}