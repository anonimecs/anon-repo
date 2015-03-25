package org.anon.gui;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.logic.AnonymisationMethodEncrypt;

@ManagedBean
@ViewScoped
public class ConfigureEncryptionPopupBacking extends BackingBase {
	
	@ManagedProperty(value="#{configContext}")
	private ConfigContext configContext;
	
	private String password;
	private String generatedPassword;

	@PostConstruct
	public void init() {
		password = "";
		generatedPassword = "";
	}
	
	public void onSubmitClicked() {
		
		if(password.length() > 4) {
			getAnonymisationMethodMapping().setPassword(password);
			showInfoInGui("Manual passwort set.");
		}
		else if(password.isEmpty() && !generatedPassword.isEmpty()) {
			getAnonymisationMethodMapping().setPassword(generatedPassword);
			showInfoInGui("Generated passwort set.");
		}
		else {
			showErrorInGui("Password to short, mininum of 5 chars.");
		}
		init();
	}
	
	public void generateRandomPassword() {
		
		SecureRandom random = new SecureRandom();
		generatedPassword = new BigInteger(130, random).toString(32);
	}
	
	public AnonymisationMethodEncrypt getAnonymisationMethodMapping(){
		return (AnonymisationMethodEncrypt) configContext.getAnonymisationMethod();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGeneratedPassword() {
		return generatedPassword;
	}

	public void setGeneratedPassword(String generatedPassword) {
		this.generatedPassword = generatedPassword;
	}

	public void setConfigContext(ConfigContext configContext) {
		this.configContext = configContext;
	}
}
