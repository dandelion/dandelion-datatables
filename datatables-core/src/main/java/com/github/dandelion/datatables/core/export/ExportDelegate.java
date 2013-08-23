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
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.ClassUtils;

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
	 * @throws BadConfigurationException 
	 */
	public void launchExport() throws ExportException, BadConfigurationException {

		OutputStream stream = new ByteArrayOutputStream();
		String exportClass = null;

		// Get the current export type
		ExportType exportType = htmlTable.getTableConfiguration().getExportProperties().getCurrentExportType();

		// Get as a string the class to use for the export, either using a
		// custom class
		// or using the default one
		exportClass = htmlTable.getTableConfiguration().getExportClass(exportType);
		logger.debug("Export class selected : {}", exportClass);

		// Check that the class can be instanciated
		if (!ClassUtils.canBeUsed(exportClass)) {
			logger.error("Did you forget to add a dependency ?");
			throw new ExportException("Unable to export in " + exportType.toString() + " format");
		}

		// Get the class
		Class<?> klass = null;
		Object obj = null;
		try {
			klass = ClassUtils.getClass(exportClass);
			obj = ClassUtils.getNewInstance(klass);
		} catch (ClassNotFoundException e) {
			throw new ExportException("Unable to load the class '" + exportClass + "'");
		} catch (InstantiationException e) {
			throw new ExportException("Unable to instanciate the class '" + exportClass + "'");
		} catch (IllegalAccessException e) {
			throw new ExportException("Unable to access the class '" + exportClass + "'");
		}

		// Invoke methods that update the stream
		try {
			ClassUtils.invokeMethod(obj, "initExport", new Object[] { htmlTable });
		} catch (NoSuchMethodException e) {
			throw new ExportException("Unable to invoke the method initExport", e);
		} catch (IllegalAccessException e) {
			throw new ExportException("Unable to invoke the method initExport", e);
		} catch (InvocationTargetException e) {
			throw new ExportException("Unable to invoke the method initExport", e);
		}
		
		try {
			ClassUtils.invokeMethod(obj, "processExport", new Object[] { stream });
		} catch (NoSuchMethodException e) {
			throw new ExportException("Unable to invoke the method processExport", e);
		} catch (IllegalAccessException e) {
			throw new ExportException("Unable to invoke the method processExport", e);
		} catch (InvocationTargetException e) {
			throw new ExportException("Unable to invoke the method processExport", e);
		}

		// Fill the request so that the filter will intercept it and
		// override the response with the export configuration
		request.setAttribute(ExportConstants.DDL_DT_REQUESTATTR_EXPORT_CONTENT,
				((ByteArrayOutputStream) stream).toByteArray());

		request.setAttribute(ExportConstants.DDL_DT_REQUESTATTR_EXPORT_PROPERTIES, exportProperties);
	}
}