package org.anon.service;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

public class ServiceResultMessage implements Serializable {

	/** This is needed to cater for serializable */
	private enum ServiceResultMessageSeverity {

		INFO, WARN, ERROR, FATAL;

		static ServiceResultMessageSeverity fromSeverity(Severity severity) {
			if (severity == FacesMessage.SEVERITY_ERROR) {
				return ERROR;
			} else if (severity == FacesMessage.SEVERITY_INFO) {
				return INFO;
			} else if (severity == FacesMessage.SEVERITY_WARN) {
				return WARN;
			} else if (severity == FacesMessage.SEVERITY_FATAL) {
				return FATAL;
			} else
				return null;
		}
		
		Severity toSeverity(){
			if (this == ERROR) {
				return FacesMessage.SEVERITY_ERROR;
			} else if (this == INFO) {
				return FacesMessage.SEVERITY_INFO;
			} else if (this == WARN) {
				return FacesMessage.SEVERITY_WARN;
			} else if (this == FATAL) {
				return FacesMessage.SEVERITY_FATAL;
			} else
				return null;
		}
	}

	private ServiceResultMessageSeverity serviceResultMessageSeverity;

	private String headline;

	private String details;

	public ServiceResultMessage(Severity severity, String headline, String details) {
		setSeverity(severity);
		this.headline = headline;
		this.details = details;
	}

	public Severity getSeverity() {
		return serviceResultMessageSeverity.toSeverity();
	}

	public void setSeverity(Severity severity) {
		this.serviceResultMessageSeverity = ServiceResultMessageSeverity.fromSeverity(severity);
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
