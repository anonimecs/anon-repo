package org.anon.free;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuditUserLoginListener implements ServletRequestListener{

	@Override
	public void requestDestroyed(ServletRequestEvent arg0) {
		
	}

	@Override
	public void requestInitialized(ServletRequestEvent arg0) {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		
	}

	protected AnonFreeAuthenticationToken getAuthentication() {
		return new AnonFreeAuthenticationToken();
	}

}
