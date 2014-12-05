package org.anon.logic;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.RunResult;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * adds hash(password) to each character or number or special char
 */
public class AnonymisationMethodEncryptSybase extends AnonymisationMethodEncrypt {
	
	
	public AnonymisationMethodEncryptSybase() {
		super(AnonymizationType.ENCRYPT);
		
		addSetupSqlFiles("sybase/MethodEncrypt_createFunc.sql", "sybase/MethodEncrypt_createProc.sql");
		addCleanupSqlFiles("sybase/MethodEncrypt_dropProc.sql", "sybase/MethodEncrypt_dropFunc.sql");
		
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
		return "select an_meth_enc_func(?, ?)";
	}


	@Override
	public RunResult runOnColumn(AnonymisedColumnInfo col) {
		if(col.isJavaTypeString()){
			
			int rowCount = update("exec an_meth_enc_tbl (?, ?, ?)", col.getName() , col.getTable().getName(), hashmodint);
			return new RunResult("Updated Strings", rowCount);

		}
		else if(col.isJavaTypeDouble() || col.isJavaTypeLong()){
			int rowCount = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = " +col.getName()+ "+ round((rand("+hashmodint+") * "+col.getName()+")/10, 0)");
			return new RunResult("Updated Numbers", rowCount);
		}
		else {
			throw new RuntimeException("Unimlemented");
		}
		
	}

	
	
}
