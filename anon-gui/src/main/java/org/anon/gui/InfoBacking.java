package org.anon.gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session")
public class InfoBacking extends BackingBase {

	@Autowired
	transient ApplicationContext appContext;
	
	
	public String getUserName() {
		if(isEnterprise()) {
			try {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				return  auth.getName();
			} catch (Exception e) {
				logError("Cannot get authenticated user. ", e);
				return "AUTH_ERROR";
			}
		} else {
			return "FREE_EDITION";
		}
	}
	
	public boolean isEnterprise() {
		for(String profile : appContext.getEnvironment().getActiveProfiles()) {
			if(profile.equals("enterprise_edition")) {
				return true;
			}
		}
		return false;
	}

	public void setAppContext(ApplicationContext appContext) {
		this.appContext = appContext;
	}
}
