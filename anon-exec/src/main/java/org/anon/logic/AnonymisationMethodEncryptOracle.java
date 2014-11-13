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

public class AnonymisationMethodEncryptOracle extends AnonymisationMethodEncrypt {

	public AnonymisationMethodEncryptOracle() {
		super(AnonymizationType.ENCRYPT);
		
		addSetupSqlFiles("oracle/MethodEncrypt_createFunc.sql", "oracle/MethodEncrypt_createProc.sql");
		addCleanupSqlFiles("oracle/MethodEncrypt_dropProc.sql", "oracle/MethodEncrypt_dropFunc.sql");
		
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

			
			return new JdbcTemplate(dataSource).queryForObject("select "+ exampleValue + " + ora_hash(" + exampleValue+ ", " + exampleValue+ "/10, "+hashmodint+") from dual",clazz);
		}
		catch(RuntimeException e){
			logger.error("anonymiseDouble failed", e);
			throw e;
		}
	}

	@Override
	protected String anonymiseString(final String exampleValue) {
		try{
			String setupSql = getFileContent("oracle/MethodEncrypt_createFunc.sql");
			execute(setupSql);
			
			return new JdbcTemplate(dataSource).execute(
				new CallableStatementCreator() {
			        public CallableStatement createCallableStatement(Connection con) throws SQLException{
			            CallableStatement cs = con.prepareCall("select an_meth_enc_func(?, ?) from dual");
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
			String setupSql = getFileContent("oracle/MethodEncrypt_dropFunc.sql");
			execute(setupSql);
		}
	}
	
	@Override
	public RunResult runOnColumn(AnonymisedColumnInfo col) {
		if(col.isJavaTypeString()){
			
			int rowCount = update("call an_meth_enc_proc (?, ?, ?)", col.getName() , col.getTable().getName(), hashmodint);
			return new RunResult("Updated Strings", rowCount);

		}
		else if(col.isJavaTypeDouble() || col.isJavaTypeLong()){
			int rowCount = update("update "+ col.getTable().getName()+ " set " + col.getName() + " = " + col.getName() + " + ora_hash(" + col.getName()+ ", " + col.getName()+ "/10, "+hashmodint+")");
			return new RunResult("Updated Numbers", rowCount);
		}
		else {
			throw new RuntimeException("Unimlemented");
		}
		
	}

}
