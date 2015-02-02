package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.ExecutionMessage;
import org.springframework.jdbc.core.JdbcTemplate;

public class AnonymisationMethodEncryptOracle extends AnonymisationMethodEncrypt {

	public AnonymisationMethodEncryptOracle() {
		super(AnonymizationType.ENCRYPT);
		
		addSetupSqlFiles("oracle/MethodEncrypt_createFunc.sql", "oracle/MethodEncrypt_createProc.sql");
		addCleanupSqlFiles("oracle/MethodEncrypt_dropProc.sql", "oracle/MethodEncrypt_dropFunc.sql");
		
	}
	
	@Override
	protected <T> T anonymiseNum(Number exampleValue, Class<T> clazz) {
		try{

			
			return new JdbcTemplate(dataSource).queryForObject("select "+ exampleValue + " + ora_hash(" + exampleValue+ ", " + exampleValue+ "/10, "+hashmodint+") from dual",clazz);
		}
		catch(RuntimeException e){
			logger.error("anonymiseNum failed", e);
			throw e;
		}
	}
	
	@Override
	protected String getAnonimiseStringSql() {
		return "select an_meth_enc_func(?, ?) from dual";
	}

	
	@Override
	public ExecutionMessage runOnColumn(AnonymisedColumnInfo col) {
		if(col.isJavaTypeString()){
			
			int rowCount = update("call an_meth_enc_proc (?, ?, ?)", col.getName() , col.getTable().getName(), hashmodint);
			return new ExecutionMessage("Updated Strings", rowCount);

		}
		else if(col.isJavaTypeDouble() || col.isJavaTypeLong()){
			int rowCount = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = " + col.getName() + " + ora_hash(" + col.getName()+ ", " + col.getName()+ "/10, "+hashmodint+")");
			return new ExecutionMessage("Updated Numbers", rowCount);
		}
		else {
			throw new RuntimeException("Unimlemented");
		}
		
	}

}
