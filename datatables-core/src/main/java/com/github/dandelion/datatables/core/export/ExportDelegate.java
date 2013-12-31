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
package com.github.dandelion.datatables.core.export;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.ClassUtils;
import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Delegate class in charge of launching export.
 * 
 * @author Thibault Duchateau
 */
public class ExportDelegate {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ExportDelegate.class);

	private HtmlTable htmlTable;
	private HttpServletRequest request;

	public ExportDelegate(HtmlTable htmlTable, HttpServletRequest request) {
		this.htmlTable = htmlTable;
		this.request = request;
	}

	/**
	 * Launch the export using the right class depending on the current export
	 * type.
	 */
	public void launchExport() {

		OutputStream stream = new ByteArrayOutputStream();
		// String exportClass = null;

		// Get the current export type
		String exportFormat = htmlTable.getTableConfiguration().getCurrentExportFormat();

		ExportConf exportConf = htmlTable.getTableConfiguration().getExportConfiguration().get(exportFormat);

		String exportClass = exportConf.getExportClass();
		if (exportClass == null) {
			throw new ExportException("No export class has been configured for the '" + exportFormat
					+ "' format. Please configure it before exporting.");
		}
		logger.debug("Selected export class: {}", exportClass);
		
		// Check that the class can be instanciated
		if (!ClassUtils.canBeUsed(exportClass)) {
			logger.error("Did you forget to add an extra dependency?");
			throw new ExportException("Unable to export in " + exportFormat.toString() + " format");
		}

		// Get the class
		Class<?> klass = null;
		Object obj = null;
		try {
			klass = ClassUtils.getClass(exportClass);
			obj = ClassUtils.getNewInstance(klass);
		} catch (ClassNotFoundException e) {
			throw new ExportException("Unable to load the class '" + exportClass + "'", e);
		} catch (InstantiationException e) {
			throw new ExportException("Unable to instanciate the class '" + exportClass + "'", e);
		} catch (IllegalAccessException e) {
			throw new ExportException("Unable to access the class '" + exportClass + "'", e);
		}

		// Initialize and process export
		((DatatablesExport) obj).initExport(htmlTable);
		((DatatablesExport) obj).processExport(stream);

		// Fill the request so that the filter will intercept it and
		// override the response with the export configuration
		request.setAttribute(ExportConstants.DDL_DT_REQUESTATTR_EXPORT_CONTENT,
				((ByteArrayOutputStream) stream).toByteArray());
		request.setAttribute(ExportConstants.DDL_DT_REQUESTATTR_EXPORT_CONF, exportConf);
	}
}