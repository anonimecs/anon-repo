package org.anon.logic;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.RunResult;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * adds hash(password) to each character or number or special char
 */
public class AnonymisationMethodEncryptSybase extends AnonymisationMethodEncrypt {
	
	
	public AnonymisationMethodEncryptSybase() {
		super(AnonymizationType.ENCRYPT);
		
		addSetupSqlFiles("MethodEncrypt_create.sql", "MethodEncrypt_create2.sql");
		addCleanupSqlFiles("MethodEncrypt_drop.sql", "MethodEncrypt_drop2.sql");
		
	}
	
	@Override
	protected Long anonymiseLong(Long exampleValue) {
		return anonymiseNum(exampleValue, Long.class);
	}

	@Override
	protected Double anonymiseDouble(Double exampleValue) {
		return anonymiseNum(exampleValue, Double.class);
		
	}

	private <T> T anonymiseNum(Number exampleValue, Class<T> clazz) {
		try{

			
			return new JdbcTemplate(dataSource).queryForObject("select "+exampleValue+" + round((rand("+hashmodint+") * "+exampleValue+")/10, 0)",clazz);
		}
		catch(RuntimeException e){
			logger.error("anonymiseDouble failed", e);
			throw e;
		}
	}
	
	@Override
	protected String anonymiseString(final String exampleValue) {
		try{
			String setupSql = getFileContent("MethodEncrypt_create.sql");
			execute(setupSql);
			
			return new JdbcTemplate(dataSource).execute(
				new CallableStatementCreator() {
			        public CallableStatement createCallableStatement(Connection con) throws SQLException{
			            CallableStatement cs = con.prepareCall("select dbo.cs_meth_enc_func(?, ?)");
			            cs.setString(1, exampleValue);
			            cs.setInt(2, hashmodint);
			            return cs;
			        }
			    }, 
				new CallableStatementCallback<String>() {

					@Override
					public String doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
						cs.execute();
						ResultSet rs = cs.getResultSet();
						rs.next();
						return rs.getString(1);
					}
			    }
			);
		}
		catch(RuntimeException e){
			logger.error("anonymiseString failed", e);
			throw e;
		}
		finally{
			String setupSql = getFileContent("MethodEncrypt_drop2.sql");
			execute(setupSql);
		}
	}




	@Override
	public RunResult runOnColumn(AnonymisedColumnInfo col) {
		if(col.isJavaTypeString()){
			
			int rowCount = update("exec cs_meth_enc_tbl (?, ?, ?)'", col.getName() , col.getTable().getName(), hashmodint);
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
