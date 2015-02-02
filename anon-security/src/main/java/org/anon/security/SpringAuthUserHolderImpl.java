package org.anon.security;

import org.anon.UserHolder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpringAuthUserHolderImpl implements UserHolder{

	protected Logger logger = Logger.getLogger(getClass());
	
	private String userName;
	
	@Override
	public String getExecutingUser() {
		if(userName == null){
			try {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				userName = auth.getName();
			} catch (Exception e) {
				logger.error("Cannot get authenticated user. ", e);
				userName = "AUTH_ERROR";
			}
		}
		return userName;
	}
	
	
}
