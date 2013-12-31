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

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.constants.HttpMethod;

/**
 * POJO that stores an export type configuration.
 * 
 * @author Thibault Duchateau
 */
public class ExportConf {

	public static final String DEFAULT_CSV_CLASS = "com.github.dandelion.datatables.core.export.CsvExport";
	public static final String DEFAULT_XML_CLASS = "com.github.dandelion.datatables.core.export.XmlExport";
	public static final String DEFAULT_PDF_CLASS = "com.github.dandelion.datatables.extras.export.itext.PdfExport";
	public static final String DEFAULT_XLS_CLASS = "com.github.dandelion.datatables.extras.export.poi.XlsExport";
	public static final String DEFAULT_XLSX_CLASS = "com.github.dandelion.datatables.extras.export.poi.XlsxExport";
	
	private String format;
	private String fileName;
	private String mimeType;
	private String label;
	private StringBuilder cssStyle;
	private StringBuilder cssClass;
	private Boolean includeHeader;
	private String url;
	private HttpMethod method;
	private Boolean autoSize;
	private Boolean hasCustomUrl = false;
	private String exportClass;
	private String extension;
	private Orientation orientation;
	
	public enum Orientation {
		PORTRAIT, LANDSCAPE;
	}
	
	public ExportConf(String format){
		this.format = format;
		init();
	}
	
	public ExportConf(String format, String exportUrl){
		this.format = format;
		this.url = exportUrl;
		init();
	}
	
	/**
	 * Initialize the default values.
	 */
	private void init() {
		this.fileName = "export";
		this.label = this.format.toUpperCase();
		this.includeHeader = true;
		this.autoSize = true;
		this.method = HttpMethod.GET;
		if(StringUtils.isBlank(this.extension)){
			this.extension = this.format;
		}
		
		if(this.format.equals(Format.CSV)){
			this.exportClass = DEFAULT_CSV_CLASS;
			this.mimeType = "text/csv";
		}
		if(this.format.equals(Format.XML)){
			this.exportClass = DEFAULT_XML_CLASS;
			this.mimeType = "text/xml";
		}
		if(this.format.equals(Format.PDF)){
			this.exportClass = DEFAULT_PDF_CLASS;
			this.mimeType = "application/pdf";
			this.orientation = Orientation.LANDSCAPE;
		}
		if(this.format.equals(Format.XLS)){
			this.exportClass = DEFAULT_XLS_CLASS;
			this.mimeType = "application/vnd.ms-excel";
		}
		if(this.format.equals(Format.XLSX)){
			this.exportClass = DEFAULT_XLSX_CLASS;
			this.mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public StringBuilder getCssStyle() {
		return cssStyle;
	}

	public void setCssStyle(StringBuilder cssStyle) {
		this.cssStyle = cssStyle;
	}

	public StringBuilder getCssClass() {
		return cssClass;
	}

	public void setCssClass(StringBuilder cssClass) {
		this.cssClass = cssClass;
	}

	public Boolean getIncludeHeader() {
		return includeHeader;
	}

	public void setIncludeHeader(Boolean includeHeader) {
		this.includeHeader = includeHeader;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Boolean hasCustomUrl() {
		return hasCustomUrl;
	}

	public void setHasCustomUrl(Boolean custom) {
		this.hasCustomUrl = custom;
	}

	public Boolean getAutoSize() {
		return autoSize;
	}

	public void setAutoSize(Boolean autoSize) {
		this.autoSize = autoSize;
	}

	public String getExportClass() {
		return exportClass;
	}

	public void setExportClass(String exportClass) {
		this.exportClass = exportClass;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}


	/**
	 * HtmlTable builder, allowing you to build a table in an export controller
	 * for example.
	 */
	public static class Builder {

		private ExportConf exportConf;
		
		public Builder(String format) {
			exportConf = new ExportConf(format);
			exportConf.init();
		}

		public Builder mimeType(String mimeType) {
			exportConf.setMimeType(mimeType);
			return this;
		}
		
		public Builder extension(String extension) {
			exportConf.setExtension(extension);
			return this;
		}
		
		public Builder fileName(String fileName) {
			exportConf.setFileName(fileName);
			return this;
		}
		
		public Builder header(Boolean header) {
			exportConf.setIncludeHeader(header);
			return this;
		}

		public Builder autoSize(Boolean autoSize) {
			exportConf.setAutoSize(autoSize);
			return this;
		}
		
		public Builder exportClass(String exportClass) {
			exportConf.setExportClass(exportClass);
			return this;
		}
		
		public Builder orientation(Orientation orientation) {
			exportConf.setOrientation(orientation);
			return this;
		}
		
		public ExportConf build() {
			return exportConf;
		}
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	@Override
	public String toString() {
		return "ExportConf [format=" + format + ", fileName=" + fileName + ", mimeType=" + mimeType + ", label="
				+ label + ", cssStyle=" + cssStyle + ", cssClass=" + cssClass + ", includeHeader=" + includeHeader
				+ ", url=" + url + ", method=" + method + ", autoSize=" + autoSize + ", custom=" + hasCustomUrl
				+ ", exportClass=" + exportClass + ", extension=" + extension + ", orientation=" + orientation + "]";
	}
}