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
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.export.ExportProperties;

/**
 * TODO
 * 
 * Problematique : si le tableau genere avec DT4J est en cours d'export, la
 * reponse ne doit pas renvoyer du HTML mais des donnees dans le format souhaite
 * par l'utilisateur (CSV, XML, ...).<br />
 * Si on surcharge la response avec les donnees (en mettant a jour le
 * content-type et en recuperant un writer pour reecrire le contenu de l'export
 * dedans, on tombe sur l'erreur java.lang.IllegalStateException:
 * getOutputStream()|getWriter() has already been called for this response.<br />
 * 
 * @author Thibault Duchateau
 * @since 0.7.0
 */
@WebFilter(filterName = "DataTablesFilter", value = { "/*" })
public class DatatablesFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {

		// Le param "exporting" est mis en request par la classe
		// AbstractTableTag si l'attribut export est a true dans la JSP
		// Si pas de parametre dans l'URL, le filtre ne doit rien faire

		if (servletRequest instanceof HttpServletRequest) {

			HttpServletRequest request = (HttpServletRequest) servletRequest;

			// Don't filter anything
			if (request.getParameter(ExportConstants.DT4J_REQUESTPARAM_EXPORT_ID) == null) {

				chain.doFilter(servletRequest, servletResponse);

			} else {

				// Flag set in request to tell the taglib to export the table
				// instead of displaying it
				request.setAttribute(ExportConstants.DT4J_REQUESTPARAM_EXPORT_ID,
						request.getParameter(ExportConstants.DT4J_REQUESTPARAM_EXPORT_ID));

				HttpServletResponse response = (HttpServletResponse) servletResponse;
				DatatablesResponseWrapper resWrapper = new DatatablesResponseWrapper(response);

				chain.doFilter(request, resWrapper);

				ExportProperties exportProperties = (ExportProperties) request
						.getAttribute(ExportConstants.DT4J_REQUESTATTR_EXPORT_PROPERTIES);
				String fileName = exportProperties.getFileName() + "."
						+ exportProperties.getCurrentExportType().getExtension();

				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName
						+ "\"");

				response.setContentType(exportProperties.getCurrentExportType().getMimeType());

				// Binary exports use an outpuStream 
				if(exportProperties.isBinaryExport()){
					byte[] content = (byte[]) servletRequest.getAttribute(ExportConstants.DT4J_REQUESTATTR_EXPORT_CONTENT);

					response.setContentLength(content.length);
		            OutputStream out = response.getOutputStream();
		            out.write(content);
		            out.flush();
		            out.close();
				}
				// Exports based in characters just use a writer
				else{
					String content = String.valueOf(servletRequest
							.getAttribute(ExportConstants.DT4J_REQUESTATTR_EXPORT_CONTENT));
					
					PrintWriter out = servletResponse.getWriter();
					response.setContentLength(content.length());
					out.write(content);
					out.flush();
					out.close();
				}
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