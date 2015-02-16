package org.anon.service;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;

public class ServiceException extends Exception{

	private List<ServiceResultMessage> resultMessages;


	public ServiceException(String headline, String details) {
		this();
		addErrorMessage(headline, details);
	}
	
	public ServiceException() {
		resultMessages = new ArrayList<ServiceResultMessage>();
	}
	
	public void addInfoMessage(String headline, String details) {
		resultMessages.add(new ServiceResultMessage(FacesMessage.SEVERITY_INFO, headline, details));
	}
	
	public void addWarnMessage(String headline, String details) {
		resultMessages.add(new ServiceResultMessage(FacesMessage.SEVERITY_WARN, headline, details));
	}
	
	public void addErrorMessage(String headline, String details) {

		resultMessages.add(new ServiceResultMessage(FacesMessage.SEVERITY_ERROR, headline, details));
	}
	
	public void addFatalMessage(String headline, String details) {
		resultMessages.add(new ServiceResultMessage(FacesMessage.SEVERITY_FATAL, headline, details));
	}
	
	public List<ServiceResultMessage> getResultMessages() {
		return resultMessages;
	}

	public void setResultMessages(List<ServiceResultMessage> resultMessages) {
		this.resultMessages = resultMessages;
	}


}




