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
package com.github.dandelion.datatables.thymeleaf.util;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;
import com.github.dandelion.datatables.core.ajax.ColumnDef.SortDirection;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.extension.theme.Theme;
import com.github.dandelion.datatables.core.extension.theme.ThemeOption;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * General utility methods used in the Dandelion-Datatables dialect.
 *
 * @author Thibault Duchateau
 * @author Pavel Janecka
 * @author Gautier Dhordain
 */
public class Utils {

	private static Pattern booleanRegex = Pattern.compile("^true|false$");
	private static Pattern stringRegex;
	
	static {
		stringRegex = generateStringRegexFromEnumerations(//
				ExportLinkPosition.class, //
				ExportType.class, //
				SortDirection.class, //
				Theme.class, //
				ThemeOption.class);
	}

	private static Pattern generateStringRegexFromEnumerations(Class<?> ... enumClasses) {
		boolean firstElement = true;
		StringBuilder stringRegexBuilder = new StringBuilder();
		stringRegexBuilder.append("^");
		for(Class<?> enumClass: enumClasses) {
			for(Object exportLinkPosition: enumClass.getEnumConstants()) {
				if(firstElement) {
					firstElement = false;
				} else {
					stringRegexBuilder.append('|');
				}
				String enumName = ((Enum<?>)exportLinkPosition).name().toLowerCase();
				stringRegexBuilder.append(enumName);
			}
		}
		stringRegexBuilder.append("$");
		return Pattern.compile(stringRegexBuilder.toString());
	}

	/**
	 * Return the HtmlTable bean that is stored in the REQUEST scope under the
	 * name "htmlTable".
	 * 
	 * @param Thymeleaf
	 *            {@link Arguments}
	 * @return the HtmlTable bean.
	 */
	public static HtmlTable getTable(Arguments arguments) {
		return (HtmlTable) ((IWebContext) arguments.getContext()).getHttpServletRequest().getAttribute("htmlTable");
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
		return request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
	}

	/**
	 * Parse an attribute's value of Boolean class.
	 * 
	 * @param arguments
	 *            Thymeleaf {@link Arguments}
	 * @param value
	 *            The expression to parse
	 * @param defaultValue
	 *            The default value
	 * @param clazz
	 *            {@link Class} of the attribute's value
	 * @return A Boolean which is evaluated :
	 *         <ul>
	 *         <li>either using the "true" or "false" strings</li>
	 *         <li>or using the default value if the expression to parse is null
	 *         </li>
	 *         <li>or using the Standard Thymeleaf Expression in all other cases
	 *         </li>
	 *         </ul>
	 */
	public static Boolean parseElementAttribute(Arguments arguments, String value, Boolean defaultValue,
			Class<Boolean> clazz) {

		// Handling null value
		if (value == null) return defaultValue;

		if (booleanRegex.matcher(value).find()) return Boolean.parseBoolean(value.trim().toLowerCase());

		// Default behaviour
		return processStandardExpression(arguments, value, defaultValue, clazz);
	}

	/**
	 * Parse an attribute's value of String class.
	 * 
	 * @param arguments
	 *            Thymeleaf {@link Arguments}
	 * @param value
	 *            The expression to parse
	 * @param defaultValue
	 *            The default value
	 * @param clazz
	 *            {@link Class} of the attribute's value
	 * @return A string which is evaluated :
	 *         <ul>
	 *         <li>either using a specific case (e.g. asc/desc)</li>
	 *         <li>or using the default value if the expression to parse is null
	 *         </li>
	 *         <li>or using the Standard Thymeleaf Expression in all other cases
	 *         </li>
	 *         </ul>
	 */
	public static String parseElementAttribute(Arguments arguments, String value, String defaultValue,
			Class<String> clazz) {

		// Handling null value
		if (value == null) return defaultValue;

		if (stringRegex.matcher(value.trim().toLowerCase()).find()) return value.trim().toLowerCase();

		// Default behaviour
		return processStandardExpression(arguments, value, defaultValue, clazz);
	}

	/**
	 * Parse an attribute's value of any class.
	 * 
	 * @param arguments
	 *            Thymeleaf {@link Arguments}
	 * @param value
	 *            The expression to parse
	 * @param defaultValue
	 *            The default value
	 * @param clazz
	 *            {@link Class} of the attribute's value
	 * @return the default value if the expression to parse is null or the value
	 *         of the Thymeleaf Standard expression processing.
	 */
	public static <T> T parseElementAttribute(Arguments arguments, String value, T defaultValue, Class<T> clazz) {
		
		// Handling null value
		if (value == null) return defaultValue;

		// Default behaviour
		return processStandardExpression(arguments, value, defaultValue, clazz);
	}

	/**
	 * Process the Thymelead Standard Expression processor on an expression.
	 * 
	 * @param arguments
	 *            Thymeleaf {@link Arguments}
	 * @param value
	 *            String expression to parse
	 * @param defaultValue
	 *            <T> default value
	 * @param clazz
	 *            {@link Class} of the attribute's value
	 * @return an object processed by the Thymeleaf Standard Expression processor.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T processStandardExpression(Arguments arguments, String value, T defaultValue, Class<T> clazz) {
		
		// Use the Thymeleaf Standard expression processor on the expression
		Object result = StandardExpressionProcessor.processExpression(arguments, value.trim());

		// Handling null value
		if (result == null) return defaultValue;
		
		T tmpValue = defaultValue;

		// Typing the result
		if (clazz.isAssignableFrom(result.getClass())) tmpValue = (T) result;
		
		// Handling default value
		return tmpValue;
	}
}