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
package com.github.dandelion.datatables.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.exception.DataTableProcessingException;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Helper class used for all HttpServletRequest stuff.
 * 
 * @author Thibault Duchateau
 */
public class RequestHelper {

	/**
	 * <p>
	 * Return the current URL, without query parameters.
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
	 * Return the current URL, with query parameters.
	 * 
	 * @param request
	 *            The current request.
	 * @return a String containing the current URL.
	 */
	public static String getCurrentUrlWithParameters(HttpServletRequest request) {

		String currentUrl = null;
		if (request.getAttribute("javax.servlet.forward.request_uri") != null) {
			currentUrl = (String) request.getAttribute("javax.servlet.forward.request_uri");
		} else {
			currentUrl = request.getRequestURL().toString();
		}
		if (currentUrl != null && request.getQueryString() != null) {
			currentUrl += "?" + request.getQueryString();
		}
		return currentUrl;
	}

	/**
	 * Generates and returns the asset source that will be displayed HTML-side
	 * either in a link tag or in a script tag.
	 * 
	 * @param resourceName
	 *            Name of the asset.
	 * @param tableId
	 *            Current table id.
	 * @param request
	 *            The HTTP request.
	 * @param isMainFile
	 *            Boolean used to determine the asset's type.
	 * @return the path to the asset that will be served by the Dandelion
	 *         servlet.
	 */
	public static String getAssetSource(String resourceName, HtmlTable table,
			HttpServletRequest request, boolean isMainFile) {
		StringBuilder buffer = new StringBuilder(getBaseUrl(request, table));
		buffer.append("/datatablesController/");
		buffer.append(resourceName);
		buffer.append("?id=");
		buffer.append(table.getId());
		if (isMainFile) {
			buffer.append("&t=main");
		}
		if (request.getQueryString() != null) {
			buffer.append("&");
			buffer.append(request.getQueryString());
		}
		buffer.append("&c=");
		try {
			buffer.append(URLEncoder.encode(RequestHelper.getCurrentUrlWithParameters(request),
					"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new DataTableProcessingException();
		}
		return buffer.toString();
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
	public static String getDatasourceUrl(String url, ServletRequest servletRequest, HtmlTable table) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		if (url.startsWith("/")) {
			return RequestHelper.getBaseUrl(request, table) + url;
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
	 * @param table
	 *            The table from which the URL may be extracted from the
	 *            properties.
	 * @return the base URL of the current JSP.
	 */
	public static String getBaseUrl(ServletRequest servletRequest, HtmlTable table) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String baseUrl = null;
		
		if(StringUtils.isNotBlank(table.getTableProperties().getBaseUrl())){
			String[] url = request.getRequestURL().toString().split("/");
			baseUrl = url[0] + "//" + table.getTableProperties().getBaseUrl();
		}
		else{
			baseUrl = request.getRequestURL().toString();
		}
		return baseUrl.replace(request.getRequestURI(), request.getContextPath());
	}

	/**
	 * <p>
	 * Return the base URL (context path included) with its parameters.
	 * 
	 * <p>
	 * Example : with an URL like
	 * http://domain.com:port/context/anything?param1=value1, this function
	 * returns http://domain.com:port/context.
	 * 
	 * @param pageContext
	 *            Context of the current JSP.
	 * @return the base URL of the current JSP.
	 */
	public static String getBaseUrlWithParameters(ServletRequest servletRequest, HtmlTable table) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String retval = getBaseUrl(request, table);
		if (request.getQueryString() != null) {
			retval += request.getQueryString();
		}
		return retval;
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
		return request.getAttribute(ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID) != null ? request
				.getAttribute(ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID).toString()
				.toLowerCase().equals(table.getId().toLowerCase())
				: false;
	}
}