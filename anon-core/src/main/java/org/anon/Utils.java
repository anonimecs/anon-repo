package org.anon;

import java.util.Date;

public class Utils {

	public static Long getDiffInSeconds(Date timeStart, Date timeEnd){
		try {
			return (timeEnd.getTime() - timeStart.getTime()) / 1000;
		} catch (Exception e) {
			return null;
		}
	}
}
