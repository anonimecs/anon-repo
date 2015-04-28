package org.anon.logic;

import java.text.MessageFormat;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.ExecutionMessage;
import org.springframework.jdbc.core.JdbcTemplate;

public class AnonymisationMethodEncryptMySql extends AnonymisationMethodEncrypt {

	public AnonymisationMethodEncryptMySql() {
		super(AnonymizationType.ENCRYPT);
		
		addSetupSqlStatements("USE {0};");
		addSetupSqlFiles("mysql/MethodEncrypt_createFunc.sql", "mysql/MethodEncrypt_createProc.sql");
		addCleanupSqlStatements("USE {0};");
		addCleanupSqlFiles("mysql/MethodEncrypt_dropProc.sql", "mysql/MethodEncrypt_dropFunc.sql");
	}
	
	@Override
	public void setupInDb(){
		for(String setupSql:setupSqls){
			execute(MessageFormat.format(setupSql, schema));
		}
	}
	
	@Override
	public void cleanupInDb(){
		for(String cleanupSql:cleanupSqls){
			execute(MessageFormat.format(cleanupSql, schema));
		}
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
		execute(MessageFormat.format("USE {0}", schema));
		return "select an_meth_enc_func(?, ?)";
	}
	
	@Override
	public ExecutionMessage runOnColumn(AnonymisedColumnInfo col) {
		execute(MessageFormat.format("USE {0}", schema));
		if(col.isJavaTypeString()) {
			int rowCount = update("call an_meth_enc_proc (?, ?, ?, ?)", col.getName() , col.getTable().getName(), hashmodint, createWhereClause(col));
			return new ExecutionMessage("Updated Strings", rowCount);

		}
		else if(col.isJavaTypeDouble() || col.isJavaTypeLong()){
			int rowCount = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = " + col.getName() + " + round(rand("+hashmodint+") * "+ col.getName() +")/10");
			return new ExecutionMessage("Updated Numbers", rowCount);
		}
		else {
			throw new RuntimeException("Unimplemented");
		}
		
	}

}
