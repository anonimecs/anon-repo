package org.anon.service;

import javax.faces.application.FacesMessage.Severity;

public class ServiceResultMessage {

	private Severity severity;
	
	private String headline;
	
	private String details;
	
	public ServiceResultMessage(Severity severity, String headline, String details) {
		this.severity = severity;
		this.headline = headline;
		this.details = details;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
}
