package org.anon.enterprise.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.anon.persistence.data.SecurityRole;
import org.anon.persistence.data.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class AnonRmiAuthenticationToken implements Authentication {
	
	private SecurityUser securityUser;
	private UserDetails userDetails;
	
	public AnonRmiAuthenticationToken(SecurityUser securityUser){
		this.securityUser = securityUser;
		List<GrantedAuthority> authorities = new ArrayList<>();
		for(SecurityRole securityRole :securityUser.getRoles()){
			authorities.add(new SimpleGrantedAuthority(securityRole.getRole().getRoleType()));
		}
		userDetails   = new User(securityUser.getUsername(), securityUser.getPassword(), authorities);
	}
	
	@Override
	public String getName() {
		return userDetails.getUsername();
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public Object getPrincipal() {
		return userDetails;
	}

	@Override
	public Object getDetails() {
		return securityUser;
	}

	@Override
	public Object getCredentials() {
		return securityUser.getPassword();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return userDetails.getAuthorities();
	}
	
	
}