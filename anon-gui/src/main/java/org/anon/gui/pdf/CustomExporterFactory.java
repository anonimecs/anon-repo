package org.anon.gui.pdf;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.component.exporter.Exporter;
import org.primefaces.extensions.component.exporter.ExporterFactory;
import org.primefaces.extensions.component.exporter.PDFExporter;

public class CustomExporterFactory implements ExporterFactory {

	static public enum ExporterType {
		PDF
	}
	
	@Override
	public Exporter getExporterForType(String type) {
		
		Exporter exporter = null;
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExporterController bean = (ExporterController) context.getApplication().evaluateExpressionGet(context, "#{exporterController}", ExporterController.class);
		Boolean customExport = bean.getCustomExporter();
		
		try {
			ExporterType exporterType = ExporterType.valueOf(type.toUpperCase());
			
			switch (exporterType) {
			case PDF:
				if(customExport) {
					exporter = new PDFCustomExporter();
				}
				else {
					exporter = new PDFExporter();
				}
				
				break;

			default:
				break;
			}
			
		} catch (IllegalArgumentException e) {
			throw new FacesException(e);
		}
		
		return exporter;
	}

}
