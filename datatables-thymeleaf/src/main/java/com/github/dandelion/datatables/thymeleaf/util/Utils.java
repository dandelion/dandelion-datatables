package com.github.dandelion.datatables.thymeleaf.util;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;

import com.github.dandelion.datatables.core.model.HtmlTable;

public class Utils {

	public static HtmlTable getTable(Arguments arguments){
		return (HtmlTable)((IWebContext) arguments.getContext()).getHttpServletRequest().getAttribute("htmlTable");
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
	public static String getBaseUrl(HttpServletRequest request) {
		return request.getRequestURL().toString()
				.replace(request.getRequestURI(), request.getContextPath());
	}
}
