package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.ExecutionMessage;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * adds hash(password) to each character or number or special char
 */
public class AnonymisationMethodEncryptSqlServer extends AnonymisationMethodEncrypt {
	
	
	public AnonymisationMethodEncryptSqlServer() {
		super(AnonymizationType.ENCRYPT);
		
		addSetupSqlFiles("sqlserver/MethodEncrypt_createFunc.sql", "sqlserver/MethodEncrypt_createProc.sql");
		addCleanupSqlFiles("sqlserver/MethodEncrypt_dropProc.sql", "sqlserver/MethodEncrypt_dropFunc.sql");
		
	}
	

	@Override
	protected <T> T anonymiseNum(Number exampleValue, Class<T> clazz) {
		try{

			
			return new JdbcTemplate(dataSource).queryForObject("select "+exampleValue+" + round((rand("+hashmodint+") * "+exampleValue+")/10, 0)",clazz);
		}
		catch(RuntimeException e){
			logger.error("anonymiseNum failed", e);
			throw e;
		}
	}
	
	@Override
	protected String getAnonimiseStringSql() {
		return "select dbo.an_meth_enc_func(?, ?)";
	}


	@Override
	public ExecutionMessage runOnColumn(AnonymisedColumnInfo col) {
		if(col.isJavaTypeString()){
			
			int rowCount = update("{call an_meth_enc_tbl (?, ?, ?, ?)}", col.getName() , col.getTable().getName(), hashmodint, createWhereClause(col));
			return new ExecutionMessage("Updated Strings", rowCount);

		}
		else if(col.isJavaTypeDouble() || col.isJavaTypeLong()){
			int rowCount = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = " +col.getName()+ "+ round((rand("+hashmodint+") * "+col.getName()+")/10, 0)");
			return new ExecutionMessage("Updated Numbers", rowCount);
		}
		else {
			throw new RuntimeException("Unimlemented");
		}
		
	}

	
	
}
