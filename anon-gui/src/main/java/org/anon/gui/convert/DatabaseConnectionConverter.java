package org.anon.gui.convert;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.anon.gui.DatabaseConfigBacking;
import org.anon.persistence.data.DatabaseConnection;

@FacesConverter("databaseConnectionConverter")
public class DatabaseConnectionConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
        	Long id = Long.valueOf(value);
       
            for(DatabaseConnection databaseConnection: getList(fc)){
            	if(id == databaseConnection.getId()){
            		return databaseConnection;
            	}
            }
        }
       
        return null;
        
    }
 
    private List<DatabaseConnection> getList(FacesContext fc) {
    	DatabaseConfigBacking databaseConfigBacking  = (DatabaseConfigBacking) fc.getApplication().
    			evaluateExpressionGet(fc, "#{databaseConfigBacking}", DatabaseConfigBacking.class);
    	
		return databaseConfigBacking.getDatabaseConnections();
	}

	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((DatabaseConnection) object).getId());
        }
        else {
            return null;
        }
    }   

}