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
package com.github.dandelion.datatables.extras.export.poi;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.dandelion.datatables.core.asset.DisplayType;
import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.export.DatatablesExport;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.processor.export.ExportFormatProcessor;

/**
 * Default Excel (XLSX) export class.
 * 
 * @author Thibault Duchateau
 */
public class XlsxExport implements DatatablesExport {

	private HtmlTable table;
	private ExportConf exportConf;

	@Override
	public void initExport(HtmlTable table) {
		this.table = table;
		this.exportConf = table.getTableConfiguration().getExportConfiguration()
				.get(ExportFormatProcessor.RESERVED_XLSX_FORMAT);
	}

	@Override
	public void processExport(OutputStream output) {

		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Sample sheet");
			Row row = null;
			Cell cell = null;
			int rownum = 0;
			int cellnum;

			// Header
			if (exportConf.getIncludeHeader()) {

				for (HtmlRow htmlRow : table.getHeadRows()) {

					row = sheet.createRow(rownum++);
					cellnum = 0;

					for (HtmlColumn column : htmlRow.getColumns()) {

						Set<DisplayType> enabledDisplayTypes = column.getEnabledDisplayTypes();
						if (enabledDisplayTypes.contains(DisplayType.ALL)
								|| enabledDisplayTypes.contains(DisplayType.XLSX)) {

							cell = row.createCell(cellnum++);
							cell.setCellValue(column.getContent().toString());

							if (exportConf.getAutoSize()) {
								sheet.autoSizeColumn(cellnum);
							}
						}
					}
				}
			}

			// Body
			for (HtmlRow htmlRow : table.getBodyRows()) {

				row = sheet.createRow(rownum++);
				cellnum = 0;
				
				for (HtmlColumn column : htmlRow.getColumns()) {

					Set<DisplayType> enabledDisplayTypes = column.getEnabledDisplayTypes();
					if (enabledDisplayTypes.contains(DisplayType.ALL)
							|| enabledDisplayTypes.contains(DisplayType.XLSX)) {

						cell = row.createCell(cellnum++);
						cell.setCellValue(column.getContent().toString());

						if (exportConf.getAutoSize()) {
							sheet.autoSizeColumn(cellnum);
						}
					}
				}
			}

			workbook.write(output);

		} catch (IOException e) {
			throw new ExportException(e);
		}
	}
}