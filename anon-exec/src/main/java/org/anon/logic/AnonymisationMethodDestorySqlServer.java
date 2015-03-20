package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.ExecutionMessage;

public class AnonymisationMethodDestorySqlServer extends AnonymisationMethodDestory {

	@Override
	public ExecutionMessage runOnColumn(AnonymisedColumnInfo col) {
		if(col.isJavaTypeString()){
			
			int rows = update("update "+ col.getTable().getName()+ " set " + col.getName() + " =  REPLICATE('x', LEN(" + col.getName()+ "))");
			return new ExecutionMessage("Updated String records", rows );
		}
		else if(col.isJavaTypeDouble() || col.isJavaTypeLong()){
			int rows = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = 0");
			return new ExecutionMessage("Updated Numeric records", rows );
		}
		else if(col.isJavaTypeDate()){
			int rows = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = getDate()");
			return new ExecutionMessage("Updated Date records", rows );
		}
		
		throw new RuntimeException();
		
		
	}
}
