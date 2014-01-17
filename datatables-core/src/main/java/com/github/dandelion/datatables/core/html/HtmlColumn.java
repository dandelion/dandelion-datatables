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
package com.github.dandelion.datatables.core.html;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.export.ReservedFormat;

/**
 * Plain old HTML <code>td</code> and <code>th</code> tags.
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public class HtmlColumn extends HtmlTagWithContent {

	private ColumnConfiguration columnConfiguration;
	private Boolean isHeaderColumn;
	protected StringBuilder cssCellStyle;
	protected StringBuilder cssCellClass;
	private Set<String> enabledDisplayTypes = new HashSet<String>();
	
	public HtmlColumn() {
		setHeaderColumn(false);
		enabledDisplayTypes.add(ReservedFormat.ALL);
	};

	public HtmlColumn(String displayFormat) {
		setHeaderColumn(false);
		enabledDisplayTypes.add(displayFormat);
	};

	public HtmlColumn(Boolean isHeader) {
		setHeaderColumn(isHeader);
		enabledDisplayTypes.add(ReservedFormat.ALL);
		if(isHeader){
			this.columnConfiguration = new ColumnConfiguration();
		}
	};

	public HtmlColumn(Boolean isHeader, String content) {
		setHeaderColumn(isHeader);
		enabledDisplayTypes.add(ReservedFormat.ALL);
		if(isHeader){
			this.columnConfiguration = new ColumnConfiguration();
		}
		if (content != null) {
			setContent(new StringBuilder(content));
		}
	}

	public HtmlColumn(Boolean isHeader, String content, Map<String, String> dynamicAttributes) {
		setHeaderColumn(isHeader);
		enabledDisplayTypes.add(ReservedFormat.ALL);
		if(isHeader){
			this.columnConfiguration = new ColumnConfiguration();
		}
		if (content != null) {
			setContent(new StringBuilder(content));
		}
		this.dynamicAttributes = dynamicAttributes;
	}

	public HtmlColumn(Boolean isHeader, String content, Map<String, String> dynamicAttributes, String displayTypes) {
		setHeaderColumn(isHeader);
		if(isHeader){
			this.columnConfiguration = new ColumnConfiguration();
		}
		if (content != null) {
			setContent(new StringBuilder(content));
		}
		this.dynamicAttributes = dynamicAttributes;
		if (StringUtils.isNotBlank(displayTypes)) {
			String[] displayTypesTab = displayTypes.trim().split(",");
			for (String displayType : displayTypesTab) {
				this.enabledDisplayTypes.add(displayType.toLowerCase().trim());
			}
		}
		else{
			enabledDisplayTypes.add(ReservedFormat.ALL);
		}
	}
	
	@Override
	protected StringBuilder getHtmlAttributes() {
		StringBuilder html = new StringBuilder();
		if (this.isHeaderColumn) {
			html.append(writeAttribute("class", ColumnConfig.CSSCLASS.valueFrom(this.getColumnConfiguration())));
			html.append(writeAttribute("style", ColumnConfig.CSSSTYLE.valueFrom(this.getColumnConfiguration())));
			
			String columnId = ColumnConfig.ID.valueFrom(this.getColumnConfiguration());
			if (StringUtils.isNotBlank(columnId)) {
				html.append(writeAttribute("id", columnId));
			}
		} else {
			html.append(writeAttribute("class", this.cssCellClass));
			html.append(writeAttribute("style", this.cssCellStyle));
		}
		return html;
	}

	private void setHeaderColumn(Boolean isHeaderColumn) {
		this.isHeaderColumn = isHeaderColumn;
		if (this.isHeaderColumn) {
			this.tag = "th";
		} else {
			this.tag = "td";
		}
	}

	public Boolean isHeaderColumn() {
		return isHeaderColumn;
	}

	public ColumnConfiguration getColumnConfiguration() {
		return columnConfiguration;
	}

	public void setColumnConfiguration(ColumnConfiguration columnConfiguration) {
		this.columnConfiguration = columnConfiguration;
	}
	
	public void addCssClass(String cssClass) {
		if (this.isHeaderColumn) {
			StringBuilder cssClassSb = ColumnConfig.CSSCLASS.valueFrom(this.columnConfiguration);
			if (cssClassSb != null && cssClassSb.length() != 0) {
				ColumnConfig.CSSCLASS.appendIn(this.columnConfiguration, CLASS_SEPARATOR);
			}
			ColumnConfig.CSSCLASS.appendIn(this.columnConfiguration, cssClass);
		}
		else {
			if (this.cssClass == null) {
				this.cssClass = new StringBuilder();
			}
			else {
				this.cssClass.append(CLASS_SEPARATOR);
			}
			this.cssClass.append(cssClass);
		}
	}
	
	public void addCssCellClass(String cssCellClass) {
		if(this.cssCellClass == null) {
			this.cssCellClass = new StringBuilder();
		} else {
			this.cssCellClass.append(CLASS_SEPARATOR);
		}
		this.cssCellClass.append(cssCellClass);
	}

	public void addCssCellStyle(String cssCellStyle) {
		if(this.cssCellStyle == null) {
			this.cssCellStyle = new StringBuilder();
		} else {
			this.cssCellStyle.append(STYLE_SEPARATOR);
		}
		this.cssCellStyle.append(cssCellStyle);
	}

	public Set<String> getEnabledDisplayTypes() {
		return enabledDisplayTypes;
	}

	public void setEnabledDisplayTypes(Set<String> enabledDisplayTypes) {
		this.enabledDisplayTypes = enabledDisplayTypes;
	}
	
	public StringBuilder getCssCellStyle() {
		return cssCellStyle;
	}

	public StringBuilder getCssCellClass() {
		return cssCellClass;
	}
}