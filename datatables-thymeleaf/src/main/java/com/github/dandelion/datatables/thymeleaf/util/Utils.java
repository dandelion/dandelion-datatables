package com.github.dandelion.datatables.thymeleaf.util;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;

import com.github.dandelion.datatables.core.html.HtmlTable;

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
	
	/**
	 * Use standard expression processor to selected value. 
	 * If result is same class as default value, it's returned, otherwise default value is returned.
	 * If value is null default value is returned. 
	 * @param arguments {@link Arguments} instance for parsing
	 * @param value String expression to parse
	 * @param defaultValue <T> default value
	 * @param clazz {@link Class} type
	 * @return <T> result value or default value if result has different class type
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseElementAttribute(Arguments arguments, String value, T defaultValue, Class<T> clazz) {
		if (value == null) return defaultValue;
		Object result = StandardExpressionProcessor.processExpression(arguments, value.trim());
		T tmpValue = defaultValue;
		if (clazz.isAssignableFrom(result.getClass())) tmpValue = (T) result;
		return tmpValue;
	}
}
