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

/**
 * POJO that stores an export type configuration.
 * 
 * @author Thibault Duchateau
 */
public class ExportConf {

	private String fileName;
	private ExportType type;
	private String label;
	private StringBuilder cssStyle;
	private StringBuilder cssClass;
	private Boolean includeHeader;
	private String area;
	private String url;
	private Boolean autoSize;
	private Boolean custom = false;

	public ExportConf(ExportType type){
		this.type = type;
		init();
	}
	
	public ExportConf(ExportType type, String url) {
		this.type = type;
		this.url = url;
		init();
	}

	/**
	 * Initialize the default values.
	 */
	private void init() {
		this.fileName = "export";
		this.label = this.type.toString();
		this.includeHeader = true;
		this.area = "ALL";
		this.autoSize = false;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ExportType getType() {
		return type;
	}

	public void setType(ExportType type) {
		this.type = type;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean isCustom() {
		return custom;
	}

	public void setCustom(Boolean custom) {
		this.custom = custom;
	}

	public Boolean getAutoSize() {
		return autoSize;
	}

	public void setAutoSize(Boolean autoSize) {
		this.autoSize = autoSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExportConf other = (ExportConf) obj;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExportConf [fileName=" + fileName + ", type=" + type + ", label=" + label + ", cssStyle=" + cssStyle
				+ ", cssClass=" + cssClass + ", includeHeader=" + includeHeader + ", area=" + area + ", url=" + url
				+ ", autoSize=" + autoSize + ", custom=" + custom + "]";
	}
}