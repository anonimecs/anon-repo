package org.anon.gui.convert;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("secondsAsRuntimeConverter")
public class SecondsAsRuntimeConverter implements Converter{

    private static NumberFormat FORMAT = new DecimalFormat("#.00");

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Number) {
            long seconds = ((Number) value).longValue();
            if (seconds < 120) {
                return seconds + " seconds";
            }
            if ((seconds / 60) < 120 ) {
                return (seconds / 60) + " minutes";
            }
            return FORMAT.format(seconds / (60.0 * 60.0)) + " hours";
        }

        return null;

	}


	
}
