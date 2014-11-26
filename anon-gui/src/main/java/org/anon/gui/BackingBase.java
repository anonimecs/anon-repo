package org.anon.gui;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.service.ServiceResult;
import org.anon.service.ServiceResultMessage;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class BackingBase {
	
	public static final String INFO_MESSAGE_ID = "infoMessage";
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_FIELD_NAMES_STYLE);
	}
	
	protected void logDebug(Object message){
		Logger.getLogger(getClass()).log(Level.DEBUG, message);
	}
	
	protected void logInfo(Object message){
		Logger.getLogger(getClass()).log(Level.INFO, message);
	}
	
	protected void logError(Object message, Exception e){
		Logger.getLogger(getClass()).log(Level.ERROR, message, e);
	}
	
	protected void showInfoInGui(String guiMessage) {
		showExtInfoInGui(guiMessage, null);
	}
	
	protected void showExtInfoInGui(String head, String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, head, message);
	     FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	protected void showErrorInGui(String guiMessage) {
		showExtErrorInGui(guiMessage, null);
	}

	protected void showExtErrorInGui(String head, String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, head, message);
	     FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	protected void handleServiceResult(ServiceResult serviceResult) {
		for(ServiceResultMessage msg : serviceResult.getResultMessages()) {
			FacesContext.getCurrentInstance()
			.addMessage(null , new FacesMessage(msg.getSeverity(), msg.getHeadline(), msg.getDetails()));
		}
	}
	
	protected void handleServiceResultAsInfoMessage(ServiceResult serviceResult) {
		for(ServiceResultMessage msg : serviceResult.getResultMessages()) {
			FacesContext.getCurrentInstance()
			.addMessage(INFO_MESSAGE_ID, new FacesMessage(msg.getSeverity(), msg.getHeadline(), msg.getDetails()));
		}
	}
	
	protected void redirectPageTo(NavigationCaseEnum nav) {
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);
		
		ConfigurableNavigationHandler handler = (ConfigurableNavigationHandler) context
				.getApplication().getNavigationHandler();
		
		handler.performNavigation(nav.getUrl() + "?faces-redirect=true");
	}
}
