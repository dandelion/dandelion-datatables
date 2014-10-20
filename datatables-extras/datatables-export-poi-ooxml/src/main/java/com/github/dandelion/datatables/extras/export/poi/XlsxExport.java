/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2014 Dandelion
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
package com.github.dandelion.datatables.extras.export.poi;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.datatables.core.export.DatatablesExport;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ReservedFormat;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * XLSX implementation of {@link DatatablesExport} that exports data in XLSX
 * format using Apache POI.
 * </p>
 * 
 * @author Thibault Duchateau
 */
public class XlsxExport implements DatatablesExport {

	private HtmlTable table;
	private ExportConf exportConf;

	@Override
	public void initExport(HtmlTable table) {
		this.table = table;
		this.exportConf = table.getTableConfiguration().getExportConfiguration().get(ReservedFormat.XLSX);
	}

	@Override
	public void processExport(OutputStream output) {

		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(exportConf.getFileName());
			Row row = null;
			Cell cell = null;
			int rowIndex = 0;
			int columnIndex;

			// Header
			if (exportConf.getIncludeHeader()) {

				for (HtmlRow htmlRow : table.getHeadRows()) {

					row = sheet.createRow(rowIndex++);
					columnIndex = 0;

					for (HtmlColumn column : htmlRow.getColumns(ReservedFormat.ALL, ReservedFormat.XLSX)) {
						cell = row.createCell(columnIndex++);
						cell.setCellValue(column.getContent().toString());
					}
				}
			}

			// Body
			for (HtmlRow htmlRow : table.getBodyRows()) {

				row = sheet.createRow(rowIndex++);
				columnIndex = 0;

				for (HtmlColumn column : htmlRow.getColumns(ReservedFormat.ALL, ReservedFormat.XLSX)) {
					cell = row.createCell(columnIndex++);
					cell.setCellValue(column.getContent().toString());
				}
			}

			// Column auto-sizing
			for (columnIndex = 0; columnIndex < table.getLastHeaderRow().getColumns(ReservedFormat.ALL, ReservedFormat.XLSX).size(); columnIndex++) {
				if (exportConf.getAutoSize()) {
					sheet.autoSizeColumn(columnIndex);
				}
			}

			workbook.write(output);
		} catch (IOException e) {
			StringBuilder sb = new StringBuilder("Something went wrong during the XLSX generation of the table '");
			sb.append(table.getOriginalId());
			sb.append("' and with the following export configuration: ");
			sb.append(exportConf.toString());
			throw new DandelionException(sb.toString(), e);
		}
	}
}