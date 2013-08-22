/*
 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors 
 * may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.dandelion.datatables.extras.export.itext;

import java.io.OutputStream;
import java.util.Set;

import com.github.dandelion.datatables.core.asset.DisplayType;
import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.export.DatatablesExport;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Default PDF export implementation.
 * 
 * @author Thibault Duchateau
 */
public class PdfExport implements DatatablesExport {

	private HtmlTable htmlTable;
	private ExportConf exportConf;

	@Override
	public void initExport(HtmlTable table) {
		this.htmlTable = table;
		if (table.getTableConfiguration().getExportConfs() != null && table.getTableConfiguration().getExportConf(ExportType.PDF) != null) {
			this.exportConf = table.getTableConfiguration().getExportConf(ExportType.PDF);
		}
	}

	@Override
	public void processExport(OutputStream output) throws ExportException {

		Document document = new Document();

		PdfWriter pdfWriter;
		try {
			pdfWriter = PdfWriter.getInstance(document, output);
			pdfWriter.setViewerPreferences(PdfWriter.PageLayoutSinglePage);

			document.open();
			addTitle(document);
			addTable(document);

		} catch (DocumentException e) {
			throw new ExportException(e);
		} finally {
			document.close();
		}
	}

	private void addTitle(Document document) throws DocumentException{
		Paragraph title = new Paragraph("Export");
		title.add(new Paragraph(" ")); // empty line
		title.setAlignment(Element.ALIGN_CENTER);
	    document.add(title);
	}
	
	private void addTable(Document document) throws DocumentException {

		PdfPCell cell = null;

		// Compute the column count in order to initialize the iText table
		int columnCount = 0;
		for (HtmlRow htmlRow : htmlTable.getBodyRows()) {

			for (HtmlColumn column : htmlRow.getColumns()) {

				Set<DisplayType> enabledDisplayTypes = column.getEnabledDisplayTypes();
				if (enabledDisplayTypes != null
						&& (enabledDisplayTypes.contains(DisplayType.ALL)
						|| enabledDisplayTypes.contains(DisplayType.PDF))) {
					columnCount++;
				}
			}
			break;
		}

		if(columnCount != 0){
			
			// Creation d'une PdfPTable avec 3 colonnes
			PdfPTable table = new PdfPTable(columnCount);
			table.setWidthPercentage(100f);
			
			// Header
			if (exportConf != null && exportConf.getIncludeHeader()) {
				
				for (HtmlRow htmlRow : htmlTable.getHeadRows()) {
					
					for (HtmlColumn column : htmlRow.getColumns()) {
						
						Set<DisplayType> enabledDisplayTypes = column.getEnabledDisplayTypes();
						if (enabledDisplayTypes != null 
								&& (enabledDisplayTypes.contains(DisplayType.ALL)
										|| enabledDisplayTypes.contains(DisplayType.PDF))) {
							cell = new PdfPCell();
							cell.setPhrase(new Phrase(column.getContent().toString()));
							table.addCell(cell);
						}
					}
				}
			}
			
			for (HtmlRow htmlRow : htmlTable.getBodyRows()) {
				
				for (HtmlColumn column : htmlRow.getColumns()) {
					
					Set<DisplayType> enabledDisplayTypes = column.getEnabledDisplayTypes();
					if (enabledDisplayTypes != null
							&& (enabledDisplayTypes.contains(DisplayType.ALL)
									|| enabledDisplayTypes.contains(DisplayType.PDF))) {
						cell = new PdfPCell();
						cell.setPhrase(new Phrase(column.getContent().toString()));
						table.addCell(cell);
					}
				}
			}
			
			document.add(table);
		}
	}
}