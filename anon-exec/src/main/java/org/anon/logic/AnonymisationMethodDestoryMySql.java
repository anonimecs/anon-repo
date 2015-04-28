package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.ExecutionMessage;

public class AnonymisationMethodDestoryMySql extends AnonymisationMethodDestory {

	@Override
	public ExecutionMessage runOnColumn(AnonymisedColumnInfo col) {
		if(col.isJavaTypeString()){
			
			int rows = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = REPEAT('x', LENGTH(" + col.getName()+ ")) " + createWhereClause(col));
			return new ExecutionMessage("Updated String records", rows );
		}
		else if(col.isJavaTypeDouble() || col.isJavaTypeLong()){
			int rows = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = 0 " + createWhereClause(col));
			return new ExecutionMessage("Updated Numeric records", rows );
		}
		else if(col.isJavaTypeDate()){
			int rows = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = CURRENT_DATE() " + createWhereClause(col));
			return new ExecutionMessage("Updated Date records", rows );
		}
		
		throw new RuntimeException();
		
		
	}
}
