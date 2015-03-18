package org.anon.enterprise.api;

import java.lang.reflect.InvocationTargetException;

import org.anon.persistence.data.SecurityUser;
import org.anon.service.admin.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.support.DefaultRemoteInvocationExecutor;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This class ensures authentication in RMI API calls. 
 * 
 */
public class AnonRemoteInvocationExecutor extends DefaultRemoteInvocationExecutor {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	UserService userService;
	
	@Override
	public Object invoke(RemoteInvocation invocation, Object targetObject) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		AnonRmiAuthenticationToken anonRmiAuthenticationToken = createToken(invocation);
		try{
			SecurityContextHolder.getContext().setAuthentication(anonRmiAuthenticationToken);
			return super.invoke(invocation, targetObject);
		}
		finally{
			SecurityContextHolder.getContext().setAuthentication(null);
		}
	}

	private AnonRmiAuthenticationToken createToken(RemoteInvocation invocation) {
		String user = (String) invocation.getAttribute(AnonRemoteInvocationFactory.USER);
		String password = (String) invocation.getAttribute(AnonRemoteInvocationFactory.PASSWORD);
		logger.debug("createToken for user: " + user + " password length: " + password == null ? null:password.length());
		
		SecurityUser securityUser = userService.loadAndAutheticateUser(user, password);
		
		return new AnonRmiAuthenticationToken(securityUser);
	}

}
