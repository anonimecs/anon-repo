package org.anon.gui.pdf;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class ExporterController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Boolean customExporter;

	public ExporterController() {
		customExporter = false;
	}

	public Boolean getCustomExporter() {
		return customExporter;
	}

	public void setCustomExporter(Boolean customExporter) {
		this.customExporter = customExporter;
	}
}