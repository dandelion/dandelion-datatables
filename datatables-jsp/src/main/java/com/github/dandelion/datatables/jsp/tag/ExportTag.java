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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.util.RequestHelper;
import com.github.dandelion.datatables.core.util.StringUtils;

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
	private String url;

	/**
	 * An ExportTag has no body but we test here that it is in the right place.
	 */
	public int doStartTag() throws JspException {

		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);	    

		// There isn't an ancestor of given class
		if (parent == null) {
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
		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);

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
			ExportConf conf = null;
			
			if (parent.getTable().getTableConfiguration().getExportConf(exportType) != null) {
				conf = parent.getTable().getTableConfiguration().getExportConf(exportType);
			}
			else{
				conf = new ExportConf(exportType);
				parent.getTable().getTableConfiguration().getExportConfs().add(conf);
			}
			
			// Default mode
			String exportUrl = null;
			if(StringUtils.isBlank(url)){
				exportUrl = RequestHelper.getCurrentURIWithParameters(request);
				if(exportUrl.contains("?")){
					exportUrl += "&";
				}
				else{
					exportUrl += "?";
				}
				exportUrl += ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE + "="
						+ exportType.getUrlParameter() + "&"
						+ ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID + "="
						+ parent.getTable().getId();
				
				conf.setCustom(false);
			}
			// Custom mode
			else{
				exportUrl = url;
				conf.setCustom(true);
			}
			
			conf.setUrl(exportUrl);

			// Other fields
			if(StringUtils.isNotBlank(fileName)){
				conf.setFileName(fileName);				
			}
			if(StringUtils.isNotBlank(label)){
				conf.setLabel(label);				
			}
			if(StringUtils.isNotBlank(cssClass)){
				conf.setCssClass(new StringBuilder(cssClass));				
			}
			if(StringUtils.isNotBlank(cssStyle)){
				conf.setCssStyle(new StringBuilder(cssStyle));				
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
			
			logger.debug("Export configuration for the type {} has been updated", exportType);
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}