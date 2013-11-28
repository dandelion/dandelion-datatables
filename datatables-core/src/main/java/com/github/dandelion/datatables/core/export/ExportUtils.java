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
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Utilities used when exporting data.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class ExportUtils {

	/**
	 * Renders the passed table by writing the data to the response.
	 * 
	 * @param table
	 *            The table to export.
	 * @param exportConf
	 *            The export configuration (e.g. the export class to use).
	 * @param response
	 *            The response to update.
	 */
	public static void renderExport(HtmlTable table, ExportConf exportConf, HttpServletResponse response) {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DatatablesExport exportClass = exportConf.getExportClass();

		exportClass.initExport(table);
		exportClass.processExport(stream);

		try {
			writeToResponse(response, stream, exportConf.getFileName() + "." + exportConf.getType().getExtension(),
					exportConf.getType().getMimeType());
		} catch (IOException e) {
			throw new ExportException(
					"Unable to write to response using the " + exportClass.getClass().getSimpleName(), e);
		}
	}

	/**
	 * Write the given temporary OutputStream to the HTTP response as an
	 * Attachment with the given title.
	 * 
	 * @param response
	 *            current HTTP response
	 * @param baos
	 *            the temporary OutputStream to write
	 * @param title
	 *            the title of the attachment
	 * @param contentType
	 *            the MIME type
	 * 
	 * @throws IOException
	 *             if writing/flushing failed
	 */
	public static void writeToResponse(HttpServletResponse response, ByteArrayOutputStream baos, String title,
			String contentType) throws IOException {
		response.setContentType(contentType);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + title + "\"");

		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
	}
}
