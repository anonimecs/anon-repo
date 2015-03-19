package org.anon.free;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class AnonFreeAuthenticationToken implements Authentication{

	@Override
	public String getName() {
		throw new RuntimeException("Not available in Free version");

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		throw new RuntimeException("Not available in Free version");
	}

	@Override
	public Object getCredentials() {
		throw new RuntimeException("Not available in Free version");

	}

	@Override
	public Object getDetails() {
		throw new RuntimeException("Not available in Free version");

	}

	@Override
	public Object getPrincipal() {
		return "admin";

	}

	@Override
	public boolean isAuthenticated() {
		return true;

	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
	}

}
