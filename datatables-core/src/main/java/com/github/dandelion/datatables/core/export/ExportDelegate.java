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
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.model.HtmlTable;
import com.github.dandelion.datatables.core.util.ReflectHelper;

/**
 * Delegate class in charge of launching export.
 * 
 * @author Thibault Duchateau
 */
public class ExportDelegate {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ExportDelegate.class);

	private HtmlTable htmlTable;
	private ExportProperties exportProperties;
	private HttpServletRequest request;

	public ExportDelegate(HtmlTable htmlTable, ExportProperties exportProperties,
			HttpServletRequest request) {
		this.htmlTable = htmlTable;
		this.exportProperties = exportProperties;
		this.request = request;
	}

	/**
	 * Launch the export using the right class depending on the current export
	 * type.
	 * 
	 * @throws ExportException
	 *             if something went wront during export.
	 */
	public void launchExport() throws ExportException {

		OutputStream stream = null;
		StringWriter writer = null;
		String exportClass = null;

		// Get the current export type
		ExportType exportType = htmlTable.getExportProperties().getCurrentExportType();

		// Get as a string the class to use for the export, either using a
		// custom class
		// or using the default one
		exportClass = htmlTable.getTableProperties().getExportClass(exportType);
		logger.debug("Export class selected : {}", exportClass);

		// Check that the class can be instanciated
		if (!ReflectHelper.canBeUsed(exportClass)) {
			logger.error("Did you forget to add a dependency ?");
			throw new ExportException("Unable to export in " + exportType.toString() + " format");
		}

		// Text export
		if (exportType.equals(ExportType.CSV) || exportType.equals(ExportType.XML)) {

			// Init the export properties
			exportProperties.setIsBinaryExport(false);
			writer = new StringWriter();

			try {

				// Get the class
				Class<?> klass = ReflectHelper.getClass(exportClass);

				// Get new instance of this class
				Object obj = ReflectHelper.getNewInstance(klass);

				// Invoke methods that update the writer
				ReflectHelper.invokeMethod(obj, "initExport", new Object[] { htmlTable });
				ReflectHelper.invokeMethod(obj, "processExport", new Object[] { writer });

				// Fill the request so that the filter will intercept it and
				// override the response with the export configuration
				request.setAttribute(ExportConstants.DDL_DT_REQUESTATTR_EXPORT_CONTENT,
						writer.toString());

			} catch (BadConfigurationException e) {
				throw new ExportException(e);
			}

		}
		// Binary export
		else {

			// Init the export properties
			exportProperties.setIsBinaryExport(true);
			stream = new ByteArrayOutputStream();

			try {

				// Get the class
				Class<?> klass = ReflectHelper.getClass(exportClass);

				// Get new instance of this class
				Object obj = ReflectHelper.getNewInstance(klass);

				// Invoke methods that update the stream
				ReflectHelper.invokeMethod(obj, "initExport", new Object[] { htmlTable });
				ReflectHelper.invokeMethod(obj, "processExport", new Object[] { stream });

				// Fill the request so that the filter will intercept it and
				// override the response with the export configuration
				request.setAttribute(ExportConstants.DDL_DT_REQUESTATTR_EXPORT_CONTENT,
						((ByteArrayOutputStream) stream).toByteArray());

			} catch (BadConfigurationException e) {
				throw new ExportException(e);
			}
		}

		request.setAttribute(ExportConstants.DDL_DT_REQUESTATTR_EXPORT_PROPERTIES, exportProperties);
	}
}