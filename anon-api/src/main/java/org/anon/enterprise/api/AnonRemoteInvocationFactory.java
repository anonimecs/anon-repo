package org.anon.enterprise.api;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.support.DefaultRemoteInvocationFactory;
import org.springframework.remoting.support.RemoteInvocation;

public class AnonRemoteInvocationFactory extends DefaultRemoteInvocationFactory{

	
	
	public static final String PASSWORD = "password";
	public static final String USER = "user";

	private String user;
	private String password;
	
	@Override
	public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
		RemoteInvocation remoteInvocation = super.createRemoteInvocation(methodInvocation);
		remoteInvocation.addAttribute(USER, user);
		remoteInvocation.addAttribute(PASSWORD, password);
		return remoteInvocation;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
