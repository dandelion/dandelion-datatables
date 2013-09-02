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
package com.github.dandelion.datatables.extras.servlet2.filter;

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

import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.export.ExportProperties;
import com.github.dandelion.datatables.core.web.filter.DatatablesResponseWrapper;

/**
 * Filter used to render DataTables exported files. This one is compatible with the Servlet 2.x API.
 * 
 * @author Thibault Duchateau
 */
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

			// Don't filter anything
			if (request.getParameter(ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID) == null) {

				chain.doFilter(servletRequest, servletResponse);

			} else {

				// Flag set in request to tell the taglib to export the table
				// instead of displaying it
				request.setAttribute(ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID,
						request.getParameter(ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID));

				HttpServletResponse response = (HttpServletResponse) servletResponse;
				DatatablesResponseWrapper resWrapper = new DatatablesResponseWrapper(response);

				chain.doFilter(request, resWrapper);

				ExportProperties exportProperties = (ExportProperties) request
						.getAttribute(ExportConstants.DDL_DT_REQUESTATTR_EXPORT_PROPERTIES);

				String finalFileName = exportProperties.getExportConf().getFileName() + "."
						+ exportProperties.getExportConf().getType().getExtension();
				
				response.setHeader(
						"Content-Disposition",
						"attachment; filename=\"" + finalFileName + "\"");

				response.setContentType(exportProperties.getCurrentExportType().getMimeType());

				byte[] content = (byte[]) servletRequest
						.getAttribute(ExportConstants.DDL_DT_REQUESTATTR_EXPORT_CONTENT);

				response.setContentLength(content.length);
				OutputStream out = response.getOutputStream();
				out.write(content);
				out.flush();
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