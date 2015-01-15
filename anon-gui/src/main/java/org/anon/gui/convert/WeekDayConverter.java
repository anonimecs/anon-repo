package org.anon.gui.convert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang.time.DateUtils;


@FacesConverter("weekDayConverter")
public class WeekDayConverter extends DateTimeConverter {

    protected static final String PATTERN = "dd.MM.yyyy ";

    protected static final long serialVersionUID = 1L;
    
    protected static final String TODAY       = "Today";
    protected static final String YESTERDAY   = "Yesterday";
    
    protected final DateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE");
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        
       
       if(value instanceof Date) {      
           
           Calendar date = Calendar.getInstance(getTimeZone());
           date.setTime((Date) value);
         
           Calendar NOW  = Calendar.getInstance(getTimeZone());
           StringBuilder str = new StringBuilder();

           if(isToday(date, NOW)) {
               str.append(TODAY);
           }
           else if(isYesterday(date, NOW)) {
               str.append(YESTERDAY);
           }
           else if(wasWithinAWeek(date, NOW)) {
               str.append(dayOfWeekFormat.format(date.getTime()));
           }          
           else {
               // just let seam do the job
               super.setPattern(getSeamPattern());
               return super.getAsString(context, component, value);
           }

           return weekPattern(str, date);

       }
       
       return "";

    }
    
    protected String weekPattern(StringBuilder str,  Calendar date) {
        return str.toString();
    }

    protected String getSeamPattern() {
        return PATTERN;
    }

    protected boolean isYesterday(Calendar date, Calendar NOW) {
        
        if((date.get(Calendar.DATE)+1) == NOW.get(Calendar.DATE)
                && date.get(Calendar.MONTH) == NOW.get(Calendar.MONTH)
                && date.get(Calendar.YEAR) == NOW.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }
    
    protected boolean isToday(Calendar date, Calendar NOW) {
        
        return DateUtils.isSameDay(date, NOW);
    }
    
    protected boolean wasWithinAWeek(Calendar date, Calendar NOW) {
        
       return (NOW.get(Calendar.DAY_OF_YEAR) - date.get(Calendar.DAY_OF_YEAR)) < 7
               && date.get(Calendar.YEAR) == NOW.get(Calendar.YEAR);
    }

}
