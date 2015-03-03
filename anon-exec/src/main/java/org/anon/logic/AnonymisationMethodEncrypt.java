package org.anon.logic;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * adds hash(password) to each character or number or special char
 */
public abstract class AnonymisationMethodEncrypt extends AnonymisationMethod {

	
	
	public AnonymisationMethodEncrypt(AnonymizationType type) {
		super(type);
		
		setPassword("defaultpassword");
		
		
	}
	@Override
	protected Long anonymiseLong(Long exampleValue) {
		return anonymiseNum(exampleValue, Long.class);
	}

	@Override
	protected Double anonymiseDouble(Double exampleValue) {
		return anonymiseNum(exampleValue, Double.class);
		
	}


	@Override
	protected Object anonymiseDate(Date exampleValue) {
		throw new RuntimeException("Anonimising date is not supported");
	}


	protected abstract <T> T  anonymiseNum(Number exampleValue, Class<T> clazz);
	
	@Override
	public boolean supports(AnonymisedColumnInfo anonymizedColumn){
		if(anonymizedColumn.isJavaTypeDate()){
			return false;
		}
		
		return true;
	}

	
	protected abstract String getAnonimiseStringSql();
	
	@Override
	protected String anonymiseString(final String exampleValue) {
		try{
			setupInDb();
			
			return new JdbcTemplate(dataSource).execute(
				new CallableStatementCreator() {
			        public CallableStatement createCallableStatement(Connection con) throws SQLException{
			            CallableStatement cs = con.prepareCall(getAnonimiseStringSql());
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
			cleanupInDb();
		}
	}


	




	
	
}
