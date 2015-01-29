package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.RunMessage;
import org.springframework.jdbc.core.JdbcTemplate;

public class AnonymisationMethodEncryptMySql extends AnonymisationMethodEncrypt {

	public AnonymisationMethodEncryptMySql() {
		super(AnonymizationType.ENCRYPT);
		
		addSetupSqlFiles("mysql/MethodEncrypt_createFunc.sql", "mysql/MethodEncrypt_createProc.sql");
		addCleanupSqlFiles("mysql/MethodEncrypt_dropProc.sql", "mysql/MethodEncrypt_dropFunc.sql");
	}
	
	@Override
	protected <T> T anonymiseNum(Number exampleValue, Class<T> clazz) {
		try{	
			return new JdbcTemplate(dataSource).queryForObject("select "+exampleValue+" + round(rand("+hashmodint+") * "+exampleValue+")/10",clazz);
		}
		catch(RuntimeException e){
			logger.error("anonymiseNum failed", e);
			throw e;
		}
	}
	
	@Override
	protected String getAnonimiseStringSql() {
		return "select an_meth_enc_func(?, ?)";
	}

	
	@Override
	public RunMessage runOnColumn(AnonymisedColumnInfo col) {
		if(col.isJavaTypeString()) {
			int rowCount = update("call an_meth_enc_proc (?, ?, ?)", col.getName() , col.getTable().getName(), hashmodint);
			return new RunMessage("Updated Strings", rowCount);
		}
		else if(col.isJavaTypeDouble() || col.isJavaTypeLong()){
			int rowCount = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = " + col.getName() + " + round(rand("+hashmodint+") * "+ col.getName() +")/10");
			return new RunMessage("Updated Numbers", rowCount);
		}
		else {
			throw new RuntimeException("Unimplemented");
		}
		
	}

}
