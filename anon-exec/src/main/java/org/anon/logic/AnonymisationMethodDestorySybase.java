package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.RunMessage;

public class AnonymisationMethodDestorySybase extends AnonymisationMethodDestory {

	@Override
	public RunMessage runOnColumn(AnonymisedColumnInfo col) {
		if(col.isJavaTypeString()){
			
			int rows = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = 'x'");
			return new RunMessage("Updated String records", rows );
		}
		else if(col.isJavaTypeDouble() || col.isJavaTypeLong()){
			int rows = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = 0");
			return new RunMessage("Updated Numeric records", rows );
		}
		else if(col.isJavaTypeDate()){
			int rows = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = getDate()");
			return new RunMessage("Updated Date records", rows );
		}
		
		throw new RuntimeException();
		
		
	}
}
