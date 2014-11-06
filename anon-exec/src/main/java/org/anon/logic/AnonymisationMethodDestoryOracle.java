package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.RunResult;

public class AnonymisationMethodDestoryOracle extends AnonymisationMethodDestory {

	@Override
	public RunResult runOnColumn(AnonymisedColumnInfo col) {
		if(col.isJavaTypeString()){
			
			int rows = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = 'x'");
			return new RunResult("Updated String records", rows );
		}
		else if(col.isJavaTypeDouble() || col.isJavaTypeLong()){
			int rows = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = 0");
			return new RunResult("Updated Numeric records", rows );
		}
		else if(col.isJavaTypeDate()){
			int rows = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = getDate()");
			return new RunResult("Updated Date records", rows );
		}
		
		throw new RuntimeException();
		
		
	}
}
