package org.anon.vendor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.anon.data.DatabaseColumnInfo;

public abstract class DatabaseSpecifics {

	/**
	 * You will need to initialize all these in a subclass
	 */
	protected Set<String> javaTypeStringSet;
	protected Set<String> javaTypeDateSet;
	protected Set<String> javaTypeLongSet;
	protected Set<String> javaTypeDoubleSet;
	
	public boolean isJavaTypeString(DatabaseColumnInfo databaseColumnInfo){
		return test(javaTypeStringSet, databaseColumnInfo);
	}

	public boolean isJavaTypeDate(DatabaseColumnInfo databaseColumnInfo){
		return test(javaTypeDateSet, databaseColumnInfo);
		
	}

	public boolean isJavaTypeLong(DatabaseColumnInfo databaseColumnInfo){
		return test(javaTypeLongSet, databaseColumnInfo);
		
	}

	public boolean isJavaTypeDouble(DatabaseColumnInfo databaseColumnInfo){
		return test(javaTypeDoubleSet, databaseColumnInfo);
	}

	public boolean isSupportedDataType(DatabaseColumnInfo databaseColumnInfo){
		return 
				isJavaTypeString(databaseColumnInfo) ||
				isJavaTypeDate(databaseColumnInfo) ||
				isJavaTypeLong(databaseColumnInfo) ||
				isJavaTypeDouble(databaseColumnInfo);
		
	}
	
	@SuppressWarnings("unchecked")
	protected static Set<String> createSet(String ... values){
		if(values == null || values.length == 0){
			return Collections.EMPTY_SET; 
		}
		return new HashSet<>(Arrays.asList(values));
	}
	
	protected boolean test(Set<String> set, DatabaseColumnInfo databaseColumnInfo) {
		String upperCaseType = databaseColumnInfo.getType().toUpperCase();
		if (set.contains(upperCaseType)){
			return true;
		}
		for(String value:set){
			if(upperCaseType.startsWith(value)){
				return true;
			}
		}
		return false;
	}

}
