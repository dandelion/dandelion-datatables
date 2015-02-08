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
package com.github.dandelion.datatables.core.web.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportUtils;

/**
 * Filter used to render DataTables exported files.
 * 
 * @author Thibault Duchateau
 * @since 0.7.0
 */
//@WebFilter(filterName = "DataTablesFilter", value = { "/*" })
public class DatatablesFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Nothing to do
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {

		if (servletRequest instanceof HttpServletRequest) {

			HttpServletRequest request = (HttpServletRequest) servletRequest;
			String exportInProgress = request.getParameter(ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_IN_PROGRESS);
			String exportType = request.getParameter(ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_TYPE);
			
			// Don't filter anything
			if (StringUtils.isBlank(exportInProgress) || StringUtils.isBlank(exportType) || !exportType.equals("f")) {

				chain.doFilter(servletRequest, servletResponse);

			} else {

				// Flag set in request to tell the taglib to export the table
				// instead of displaying it
				request.setAttribute(ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_IN_PROGRESS, "true");
				request.setAttribute(ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_ID,
						request.getParameter(ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_ID));

				HttpServletResponse response = (HttpServletResponse) servletResponse;
				DatatablesResponseWrapper resWrapper = new DatatablesResponseWrapper(response);

				chain.doFilter(request, resWrapper);

				ExportConf exportConf = (ExportConf) request
						.getAttribute(ExportUtils.DDL_DT_REQUESTATTR_EXPORT_CONF);

				String finalFileName = exportConf.getFileName() + "." + exportConf.getFileExtension();
				response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + "\"");
				response.setContentType(exportConf.getMimeType());

				byte[] content = (byte[]) servletRequest.getAttribute(ExportUtils.DDL_DT_REQUESTATTR_EXPORT_CONTENT);

				response.setContentLength(content.length);
	            OutputStream out = response.getOutputStream();
	            out.write(content);
	            out.close();
			}

		} else {
			chain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void destroy() {
		// Nothing to do
	}
}