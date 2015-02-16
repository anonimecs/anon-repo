package org.anon.gui.security;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@ManagedBean(name = "secBean")
@RequestScoped
public class SecurityBean {

	public boolean hasRole(String role) {
		
		 Authentication authentication = SecurityContextHolder.getContext()
	                .getAuthentication();
		 
		 for(GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
			 if(role.equals(grantedAuthority.getAuthority())) {
				 return true;
			 }
		 }
		 return false;
	}
}
