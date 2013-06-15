package com.github.dandelion.datatables.core.export;

import java.io.OutputStream;

import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.html.HtmlTable;

public interface PdfExport {
	public void initExport(HtmlTable table);
	public void processExport(OutputStream output) throws ExportException;
}
