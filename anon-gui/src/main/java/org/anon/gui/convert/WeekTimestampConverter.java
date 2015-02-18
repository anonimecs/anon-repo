package org.anon.gui.convert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.faces.convert.FacesConverter;

@FacesConverter("weekTimestampConverter")
public class WeekTimestampConverter extends WeekDayConverter {

    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String TIMESTAMP_PATTERN = "dd.MM.yyyy "+ TIME_PATTERN;

    private final DateFormat timeFormat = new SimpleDateFormat(TIME_PATTERN);
    
    @Override
    protected String weekPattern(StringBuilder str, Calendar date) {
        str.append(" ");
        str.append(timeFormat.format(date.getTime()));
        return str.toString();
    }
    
    @Override
    protected String getSeamPattern() {
        return TIMESTAMP_PATTERN;
    }


}
