/*
 * [The "BSD licence"]
 * Copyright (c) 2012 DataTables4j
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
 * 3. Neither the name of DataTables4j nor the names of its contributors 
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
package com.github.dandelion.datatables.core.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.model.HtmlTable;

/**
 * Helper class used for all HttpServletRequest stuff.
 * 
 * @author Thibault Duchateau
 */
public class RequestHelper {

	/**
	 * <p>
	 * Return the current URL, used to generate the export links.
	 * 
	 * @param request
	 *            The current request.
	 * @return a String containing the current URL.
	 */
	public static String getCurrentUrl(HttpServletRequest request) {
		String currentUrl = null;
		if (request.getAttribute("javax.servlet.forward.request_uri") != null) {
			currentUrl = (String) request.getAttribute("javax.servlet.forward.request_uri");
		} else {
			currentUrl = request.getRequestURL().toString();
		}
		if (currentUrl != null
				&& request.getAttribute("javax.servlet.include.query_string") != null) {
			currentUrl += "?" + request.getQueryString();
		}
		return currentUrl;
	}

	/**
	 * <p>
	 * Computes and returns the data source URL, using the following rules :
	 * <ul>
	 * <li>If the URL starts with a leading slash, the full base URL will be
	 * prepended (local data source)</li>
	 * <li>Otherwise, the URL is left unchanged</li>
	 * </ul>
	 * 
	 * @param url
	 *            The URL entered by the user.
	 * @param servletRequest
	 *            The HttpServletRequest.
	 * @return the data source URL.
	 */
	public static String getDatasourceUrl(String url, ServletRequest servletRequest) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		if (url.startsWith("/")) {
			return RequestHelper.getBaseUrl(request) + url;
		} else {
			return url;
		}
	}

	/**
	 * <p>
	 * Return the base URL (context path included).
	 * 
	 * <p>
	 * Example : with an URL like http://domain.com:port/context/anything, this
	 * function returns http://domain.com:port/context.
	 * 
	 * @param pageContext
	 *            Context of the current JSP.
	 * @return the base URL of the current JSP.
	 */
	public static String getBaseUrl(ServletRequest servletRequest) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		return request.getRequestURL().toString()
				.replace(request.getRequestURI(), request.getContextPath());
	}

	/**
	 * <p>
	 * Test if the table if being exported using the request
	 * ExportConstants.DT4J_EXPORT_ID attribute.
	 * 
	 * <p>
	 * The table's id must be tested in case of multiple tables are displayed on
	 * the same page and exportables.
	 * 
	 * @return true if the table is being exported, false otherwise.
	 */
	public static Boolean isTableBeingExported(ServletRequest servletRequest, HtmlTable table) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		return request.getAttribute(ExportConstants.DT4J_REQUESTPARAM_EXPORT_ID) != null ? request
				.getAttribute(ExportConstants.DT4J_REQUESTPARAM_EXPORT_ID).toString().toLowerCase()
				.equals(table.getId().toLowerCase()) : false;
	}
}