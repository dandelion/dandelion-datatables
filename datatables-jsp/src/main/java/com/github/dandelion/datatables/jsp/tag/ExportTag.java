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
package com.github.dandelion.datatables.jsp.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.util.RequestHelper;

/**
 * Tag which allows to configure an export type for the current table.
 * 
 * @author Thibault Duchateau
 */
public class ExportTag extends TagSupport {
	private static final long serialVersionUID = -3453884184847355817L;

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ExportTag.class);

	// Tag attributes
	private String fileName;
	private String type;
	private String label;
	private String cssStyle;
	private String cssClass;
	private ExportLinkPosition position;
	private Boolean includeHeader;
	private String area;// TODO
	private Boolean autoSize;

	/**
	 * An ExportTag has no body but we test here that it is in the right place.
	 */
	public int doStartTag() throws JspException {

		if (!(getParent() instanceof AbstractTableTag)) {
			throw new JspException("ExportTag must be inside AbstractTableTag");
		}

		return SKIP_BODY;
	}

	/**
	 * Process the tag updating table properties.
	 */
	public int doEndTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		// Get parent tag
		AbstractTableTag parent = (AbstractTableTag) getParent();

		// Evaluate the tag only once using the parent's isFirstRow method
		if (parent.isFirstIteration()) {

			ExportType exportType = null;
			try {
				exportType = ExportType.valueOf(type.toUpperCase().trim());
			} catch (IllegalArgumentException e) {
				logger.error("The export cannot be activated for the table {}. ", parent.getTable()
						.getId());
				logger.error("{} is not a valid value among {}", type, ExportType.values());
				throw new JspException(e);
			}

			// Export URL build
			
			String url = RequestHelper.getCurrentUrlWithParameters(request);
			if(url.contains("?")){
				url += "&";
			}
			else{
				url += "?";
			}
			url += ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE + "="
					+ exportType.getUrlParameter() + "&"
					+ ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID + "="
					+ parent.getTable().getId();

			ExportConf conf = new ExportConf(exportType, url);

			// Other fields
			if(StringUtils.isNotBlank(fileName)){
				conf.setFileName(fileName);				
			}
			if(StringUtils.isNotBlank(label)){
				conf.setLabel(label);				
			}
			if(StringUtils.isNotBlank(cssClass)){
				conf.setCssClass(new StringBuffer(cssClass));				
			}
			if(StringUtils.isNotBlank(cssStyle)){
				conf.setCssStyle(new StringBuffer(cssStyle));				
			}
			if(includeHeader != null){
				conf.setIncludeHeader(includeHeader != null ? includeHeader : true);				
			}
			if(StringUtils.isNotBlank(area)){
				conf.setArea(area);				
			}
			if(autoSize != null){
				conf.setAutoSize(autoSize);				
			}

			parent.getTable().getExportConfMap().put(exportType, conf);

			logger.debug("Export conf added to table {}", conf);
		}

		return EVAL_PAGE;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCssStyle() {
		return cssStyle;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public ExportLinkPosition getPosition() {
		return position;
	}

	public void setPosition(ExportLinkPosition position) {
		this.position = position;
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
	
	public Boolean getAutoSize() {
		return autoSize;
	}

	public void setAutoSize(Boolean autoSize) {
		this.autoSize = autoSize;
	}
}