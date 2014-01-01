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

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Default class used to export in the XML format.
 *
 * @author Thibault Duchateau
 */
public class XmlExport implements DatatablesExport {

	private HtmlTable table;
	private ExportConf exportConf;
	
	@Override
	public void initExport(HtmlTable table) {
		this.table = table;
		this.exportConf = table.getTableConfiguration().getExportConfiguration().get(ReservedFormat.XML);
	}

	@Override
	public void processExport(OutputStream output) {

		// Build headers list for attributes name
		List<String> headers = new ArrayList<String>();
		
		for(HtmlRow row : table.getHeadRows()){
			for(HtmlColumn column : row.getColumns(ReservedFormat.ALL, ReservedFormat.XML)){
				headers.add(StringUtils.uncapitalize(column.getContent().toString()));
			}
		}

		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = null;
		String objectType = TableConfig.INTERNAL_OBJECTTYPE.valueFrom(table.getTableConfiguration()).toLowerCase();
		
		try {
			writer = outputFactory.createXMLStreamWriter(output);
			writer.writeStartDocument("1.0");
			
			writer.writeStartElement(objectType + "s");

			for (HtmlRow row : table.getBodyRows()) {
				writer.writeStartElement(objectType);

				int i = 0;
				for (HtmlColumn column : row.getColumns(ReservedFormat.ALL, ReservedFormat.XML)) {
					writer.writeAttribute(headers.get(i), column.getContent().toString());
					i++;
				}

				writer.writeEndElement();
			}

			writer.writeEndElement();
			writer.writeEndDocument();
			writer.flush();

		} catch (XMLStreamException e) {
			StringBuilder sb = new StringBuilder("Something went wrong during the XML generation of the table '");
			sb.append(table.getOriginalId());
			sb.append("' and with the following export configuration: ");
			sb.append(exportConf.toString());
			throw new ExportException(sb.toString(), e);
		} 
		finally {
			try {
				writer.close();
			} catch (XMLStreamException e) {
				StringBuilder sb = new StringBuilder("Something went wrong during the XML generation of the table '");
				sb.append(table.getOriginalId());
				sb.append("' and with the following export configuration: ");
				sb.append(exportConf.toString());
				throw new ExportException(sb.toString(), e);
			}
		}
	}
}