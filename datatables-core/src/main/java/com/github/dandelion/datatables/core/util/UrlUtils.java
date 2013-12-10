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
package com.github.dandelion.datatables.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.dandelion.core.asset.web.AssetFilter;
import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Utilites that deal with URLs.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public class UrlUtils {

	private static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
	private static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";
	private static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";

	/**
	 * <p>
	 * Get the current URI with potentiel request parameters.
	 * 
	 * @param request
	 *            the request to extract the URI and parameters from
	 * @return the current URI.
	 */
	public static StringBuilder getCurrentUri(HttpServletRequest request) {

		StringBuilder currentUrl = new StringBuilder();

		// Get request URI
		if (request.getAttribute(FORWARD_REQUEST_URI_ATTRIBUTE) != null) {
			currentUrl.append(request.getAttribute(FORWARD_REQUEST_URI_ATTRIBUTE));
		} else if (request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE) != null) {
			currentUrl.append(request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE));
		} else {
			currentUrl.append(request.getRequestURI());
		}

		// Get request parameters
		if (request.getAttribute(FORWARD_QUERY_STRING_ATTRIBUTE) != null) {
			currentUrl.append("?").append(request.getAttribute(FORWARD_QUERY_STRING_ATTRIBUTE));
		} else if (request.getQueryString() != null) {
			currentUrl.append("?").append(request.getQueryString());
		}

		return currentUrl;
	}

	public static String getProcessedUrl(String url, HttpServletRequest request, HttpServletResponse response) {
		String processedUrl = null;

		if (isContextRelative(url)) {
			processedUrl = request.getContextPath() + url;
		} else if (isServerRelative(url)) {
			// remove the "~" from the link base
			processedUrl = url.substring(1) ;
		} else if (isAbsolute(url)) {
			processedUrl = url ;
		} else {
			// Link base is current-URL-relative
			processedUrl = url;
		}

		return response != null ? response.encodeURL(processedUrl) : processedUrl;
	}

	/**
	 * <p>
	 * Check whether the passed URL is absolute.
	 */
	private static boolean isAbsolute(String url) {
		return (url.contains("://") || url.toLowerCase().startsWith("mailto:") || url.startsWith("//"));
	}

	private static boolean isContextRelative(String url) {
		return url.startsWith("/") && !url.startsWith("//");
	}

	private static boolean isServerRelative(String url) {
		return url.startsWith("~/");
	}

	public static String getExportUrl(HttpServletRequest request, HttpServletResponse response, String exportFormat,
			String tableId) {

		StringBuilder exportUrl = getCurrentUri(request);
		addParameter(exportUrl, ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE, exportFormat);
		addParameter(exportUrl, ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID, tableId);
		addParameter(exportUrl, AssetFilter.DANDELION_ASSET_FILTER_STATE, false);
		return response != null ? response.encodeURL(exportUrl.toString()) : exportUrl.toString();
	}

	public static String getCustomExportUrl(HttpServletRequest request, HttpServletResponse response, String customUrl) {
		StringBuilder exportUrl = new StringBuilder(customUrl);
		addParameter(exportUrl, AssetFilter.DANDELION_ASSET_FILTER_STATE, false);
		return response != null ? response.encodeURL(exportUrl.toString()) : exportUrl.toString();
	}

	public static void addParameter(StringBuilder url, String name, Object value) {
		if (url.indexOf("?") == -1) {
			url.append("?");
		} else {
			url.append("&");
		}
		try {
			url.append(name).append("=").append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationProcessingException("Exception while building URL");
		}
	}

	/**
	 * <p>
	 * Check whether the table if being exported using the request
	 * {@link ExportConstants#DDL_DT_REQUESTPARAM_EXPORT_ID} attribute.
	 * 
	 * <p>
	 * The table's id must be tested in case of multiple tables are displayed on
	 * the same page and exportable.
	 * 
	 * @return true if the table is being exported, false otherwise.
	 */
	public static Boolean isTableBeingExported(ServletRequest servletRequest, HtmlTable table) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		return request.getAttribute(ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID) != null ? request
				.getAttribute(ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID).toString().toLowerCase()
				.equals(table.getId().toLowerCase()) : false;
	}
}