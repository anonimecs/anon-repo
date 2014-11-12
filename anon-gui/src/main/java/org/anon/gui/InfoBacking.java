package org.anon.gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session")
public class InfoBacking {

	@Autowired
	transient ApplicationContext appContext;
	
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
