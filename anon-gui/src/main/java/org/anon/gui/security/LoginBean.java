package org.anon.gui.security;

import java.io.Serializable;

public class LoginBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private boolean rememberMe;
	
	public void reset(){
		username = "";
		password = "";
		rememberMe = false;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
}