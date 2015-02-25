package org.anon.gui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.anon.logic.AnonymisationMethod;

@ManagedBean
@SessionScoped
public class ConfigContext {


	private AnonymisationMethod anonymisationMethod = null;
	
	public AnonymisationMethod getAnonymisationMethod() {
		return anonymisationMethod;
	}
	public void setAnonymisationMethod(AnonymisationMethod anonymisationMethod) {
		this.anonymisationMethod = anonymisationMethod;
	}

	
	
}
