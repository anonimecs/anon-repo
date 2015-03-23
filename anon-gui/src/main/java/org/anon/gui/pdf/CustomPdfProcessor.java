package org.anon.gui.pdf;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.anon.gui.InfoBacking;
import org.anon.gui.ReportBacking;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;

@ManagedBean
@RequestScoped
public class CustomPdfProcessor {
	
	@ManagedProperty(value = "#{reportBacking}")
	private ReportBacking reportBacking;
	
	@ManagedProperty(value = "#{infoBacking}")
	private InfoBacking infoBacking;
	
	
	private static Font headerFont = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
	private static Font smallBold = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
	
	public void preProcessPDF(Object document) throws DocumentException, MalformedURLException, IOException {
		
		Document pdf = (Document) document;
		pdf.open();
		
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String logo = servletContext.getRealPath("") + File.separator + "resources" 
        		+ File.separator + "images" + File.separator + "anonimecsOrig.png";
		
		Image image = Image.getInstance(logo);
		image.scaleAbsolute(50, 50);
		image.setAlignment(Element.ALIGN_RIGHT);
		
		Paragraph preface = new Paragraph();
		preface.add(image);
		addEmptyLine(preface, 1);
		preface.add(new Paragraph("Anonimecs method report", headerFont));           
		addEmptyLine(preface, 1);
		preface.add(new Paragraph("Report generated by: " + infoBacking.getUserName() + " - " + new Date(), smallBold));
		preface.add(new Paragraph("For database: " + reportBacking.getSelectedDatabase().getDatabaseConnection().getUrl() + 
				" / " + reportBacking.getSelectedSchema(), smallBold));
		addEmptyLine(preface, 2);
		
		pdf.add(preface);
	}
	
	private static void addEmptyLine(Paragraph paragraph, int number) {
	    for (int i = 0; i < number; i++) {
	      paragraph.add(new Paragraph(" "));
	    }
	}

	public void setReportBacking(ReportBacking reportBacking) {
		this.reportBacking = reportBacking;
	}

	public void setInfoBacking(InfoBacking infoBacking) {
		this.infoBacking = infoBacking;
	}


}