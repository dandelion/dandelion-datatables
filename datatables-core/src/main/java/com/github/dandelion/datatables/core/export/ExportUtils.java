package com.github.dandelion.datatables.core.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.html.HtmlTable;

public class ExportUtils {

	public static void renderExport(HtmlTable table, ExportConf exportConf, HttpServletResponse response) throws ExportException {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DatatablesExport exportClass = exportConf.getExportClass();
		
		exportClass.initExport(table);

		try {
			exportClass.processExport(stream);
		} catch (ExportException e) {
			throw e;
		}

		try {
			writeToResponse(response, stream, exportConf.getFileName(), exportConf.getType().getMimeType());
		} catch (IOException e) {
			throw new ExportException("Unable to write to response", e);
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
	 * @throws IOException
	 *             if writing/flushing failed
	 */
	protected static void writeToResponse(HttpServletResponse response, ByteArrayOutputStream baos, String title,
			String contentType) throws IOException {
		response.setContentType(contentType);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + title + "\"");

		// Flush byte array to servlet output stream.
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
	}
}
