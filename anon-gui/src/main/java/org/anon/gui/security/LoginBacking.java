package org.anon.gui.security;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.anon.gui.BackingBase;
import org.anon.gui.navigation.NavigationCaseEnum;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;


@ManagedBean
@ApplicationScoped
public class LoginBacking extends BackingBase implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	Logger logger = Logger.getLogger(getClass());
	
	@ManagedProperty(value = "#{authenticationManager}")
    private AuthenticationManager authenticationManager;
	
	@ManagedProperty(value = "#{loginBean}")
	private LoginBean loginBean;
	
	public void doLogin() {
		
		try {			
			Authentication request = new UsernamePasswordAuthenticationToken(loginBean.getUsername(), loginBean.getPassword());
			Authentication result = authenticationManager.authenticate(request);
      
			SecurityContextHolder.getContext().setAuthentication(result);			
			redirectPageTo(NavigationCaseEnum.CONNECT);
        } 
		catch (AuthenticationException e) {
            logger.info(e.getMessage());
            showErrorInGui("Login Error", "Invalid credentials");
        }
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}
