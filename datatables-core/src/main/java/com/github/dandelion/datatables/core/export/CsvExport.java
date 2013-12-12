/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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

import java.io.IOException;
import java.io.OutputStream;

import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Default class used to export in the CSV format.
 * 
 * @author Thibault Duchateau
 */
public class CsvExport implements DatatablesExport {

	private static final String SEPARATOR_CHAR = ";";
	private HtmlTable table;
	private ExportConf exportConf;

	@Override
	public void initExport(HtmlTable table) {
		this.table = table;
		this.exportConf = table.getTableConfiguration().getExportConfiguration().get(Format.CSV);
	}

	@Override
	public void processExport(OutputStream output) {
		StringBuilder buffer = new StringBuilder();

		if (exportConf.getIncludeHeader()) {
			for (HtmlRow row : table.getHeadRows()) {
				for (HtmlColumn column : row.getColumns(Format.ALL, Format.CSV)) {
					buffer.append(column.getContent()).append(SEPARATOR_CHAR);
				}
				buffer.append("\n");
			}
		}
		for (HtmlRow row : table.getBodyRows()) {
			for (HtmlColumn column : row.getColumns(Format.ALL, Format.CSV)) {
				buffer.append(column.getContent()).append(SEPARATOR_CHAR);
			}

			buffer.append("\n");
		}

		try {
			output.write(buffer.toString().getBytes());
		} catch (IOException e) {
			StringBuilder sb = new StringBuilder("Something went wrong during the CSV generation of the table '");
			sb.append(table.getOriginalId());
			sb.append("' and with the following export configuration: ");
			sb.append(exportConf.toString());
			throw new ExportException(sb.toString(), e);
		}
	}
}