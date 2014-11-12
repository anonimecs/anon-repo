package org.anon.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DBPasswordEncrypter extends JdbcDaoSupport {
	
	private String selectQuery;
	private String updateQuery;	
	private PasswordEncoder passwordEncoder;
	
	public void encryptDBPassword() {
		getJdbcTemplate().query(getSelectQuery(), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				final String username = rs.getString("USERNAME");
				final String encryptedPassword = passwordEncoder.encode(rs.getString("PASSWORD"));
				
				getJdbcTemplate().update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement preparedStatement = con.prepareStatement(getUpdateQuery());
						preparedStatement.setString(1, encryptedPassword);
						preparedStatement.setString(2, username);
						return preparedStatement;
					}
				});
			}
		});
	}
	
	public String getSelectQuery() {
		return selectQuery;
	}
	
	public void setSelectQuery(String selectQuery) {
		this.selectQuery = selectQuery;
	}
	
	public String getUpdateQuery() {
		return updateQuery;
	}
	
	public void setUpdateQuery(String updateQuery) {
		this.updateQuery = updateQuery;
	}
	
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}
	
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
}
