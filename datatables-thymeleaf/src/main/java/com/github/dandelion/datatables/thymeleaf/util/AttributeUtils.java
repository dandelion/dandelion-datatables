/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2014 Dandelion
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

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

import com.github.dandelion.datatables.core.ajax.ColumnDef.SortDirection;
import com.github.dandelion.datatables.core.export.HttpMethod;
import com.github.dandelion.datatables.core.extension.feature.FilterPlaceholder;
import com.github.dandelion.datatables.core.extension.feature.PagingType;
import com.github.dandelion.datatables.core.extension.feature.SortType;
import com.github.dandelion.datatables.core.extension.theme.Theme;
import com.github.dandelion.datatables.core.extension.theme.ThemeOption;
import com.github.dandelion.datatables.core.generator.YadcfConfigGenerator.FilterType;

/**
 * <p>
 * Set of utilities used in the Dandelion-Datatables dialect.
 * </p>
 * 
 * @author Thibault Duchateau
 * @author Pavel Janecka
 * @author Gautier Dhordain
 */
public class AttributeUtils {

	private static Pattern booleanRegex = Pattern.compile("^true|false$");
	private static Pattern stringRegex;

	static {
		stringRegex = generateStringRegexFromEnumerations(
				SortDirection.class, 
				Theme.class,
				ThemeOption.class, 
				FilterPlaceholder.class, 
				FilterType.class, 
				PagingType.class, 
				SortType.class,
				HttpMethod.class);
	}

	private static Pattern generateStringRegexFromEnumerations(Class<?>... enumClasses) {
		boolean firstElement = true;
		StringBuilder enumRegexBuilder = new StringBuilder();
		StringBuilder finalRegexBuilder = new StringBuilder();
		for (Class<?> enumClass : enumClasses) {
			for (Object enumCst : enumClass.getEnumConstants()) {
				if (firstElement) {
					firstElement = false;
				} else {
					enumRegexBuilder.append('|');
				}
				String enumName = ((Enum<?>) enumCst).name().toLowerCase();
				enumRegexBuilder.append(enumName);
			}
		}

		finalRegexBuilder
			.append("^(")
			.append(enumRegexBuilder)
			.append(")(,(")
			.append(enumRegexBuilder)
			.append("))*$");

		return Pattern.compile(finalRegexBuilder.toString(), Pattern.CASE_INSENSITIVE);
	}

	/**
	 * <p>
	 * Parses the value of an attribute as a {@link Boolean}.
	 * </p>
	 * 
	 * @param arguments
	 *            Thymeleaf {@link Arguments}
	 * @param element
	 *            The element in which an attribute is to be parsed.
	 * @param attributeName
	 *            The attribute name for which the value is to be parsed.
	 * @return A Boolean which is evaluated :
	 *         <ul>
	 *         <li>either using the "true" or "false" strings</li>
	 *         <li>or using the default value if the expression to parse is null
	 *         </li>
	 *         <li>or using the Standard Thymeleaf Expression in all other cases
	 *         </li>
	 *         </ul>
	 */
	public static Boolean parseBooleanAttribute(Arguments arguments, Element element, String attributeName) {

		String value = element.getAttributeValue(attributeName);

		// Handling null value
		if (value == null) {
			return null;
		}

		if (booleanRegex.matcher(value).find()) {
			return Boolean.parseBoolean(value.trim().toLowerCase());
		}

		// Default behaviour
		return processStandardExpression(arguments, value, Boolean.class);
	}

	/**
	 * <p>
	 * Parses the value of an attribute as a {@link String}.
	 * </p>
	 * 
	 * @param arguments
	 *            Thymeleaf {@link Arguments}
	 * @param element
	 *            The element in which an attribute is to be parsed.
	 * @param attributeName
	 *            The attribute name for which the value is to be parsed.
	 * @return A string which is evaluated :
	 *         <ul>
	 *         <li>either using a specific case (e.g. asc/desc)</li>
	 *         <li>or using the default value if the expression to parse is null
	 *         </li>
	 *         <li>or using the Standard Thymeleaf Expression in all other cases
	 *         </li>
	 *         </ul>
	 */
	public static String parseStringAttribute(Arguments arguments, Element element, String attributeName) {

		String value = element.getAttributeValue(attributeName);

		// Handling null value
		if (value == null) {
			return null;
		}

		if (stringRegex.matcher(value.trim().toLowerCase()).find()) {
			return value.trim().toLowerCase();
		}

		// Default behaviour
		return processStandardExpression(arguments, value, String.class);
	}

	
	/**
	 * <p>
	 * Parses the value of an attribute according to the provided {@code clazz}.
	 * </p>
	 * 
	 * @param arguments
	 *            Thymeleaf {@link Arguments}
	 * @param element
	 *            The element in which an attribute is to be parsed.
	 * @param attributeName
	 *            The attribute name for which the value is to be parsed.
	 * @param clazz
	 *            The {@link Class} in which the value of the given attribute
	 *            name is to be parsed.
	 * @return the default value if the expression to parse is null or the value
	 *         of the Thymeleaf Standard expression processing.
	 */
	public static <T> T parseAttribute(Arguments arguments, Element element, String attributeName, Class<T> clazz) {

		String value = element.getAttributeValue(attributeName);

		// Handling null value
		if (value == null) {
			return null;
		}

		// Default behaviour
		return processStandardExpression(arguments, value, clazz);
	}

	/**
	 * <p>
	 * Processes the provided value using the Thymeleaf Standard Expression
	 * processor.
	 * </p>
	 * 
	 * @param arguments
	 *            Thymeleaf {@link Arguments}
	 * @param value
	 *            String expression to parse
	 * @param clazz
	 *            {@link Class} of the attribute's value
	 * @return an object processed by the Thymeleaf Standard Expression
	 *         processor.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T processStandardExpression(Arguments arguments, String value, Class<T> clazz) {

		// Use the Thymeleaf Standard expression processor on the expression
		Object result = null;

		if (value.contains("${") || value.contains("@{") || value.contains("#{")) {
			Configuration configuration = arguments.getConfiguration();
			IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
			IStandardExpression standardExpression = expressionParser.parseExpression(configuration, arguments, value);
			result = standardExpression.execute(configuration, arguments);
		}
		else {
			result = value;
		}
		
		// Handling null value
		if (result == null) {
			return null;
		}

		T tmpValue = null;

		// Typing the result
		if (clazz.isAssignableFrom(result.getClass())) {
			tmpValue = (T) result;
		}

		// Handling default value
		return tmpValue;
	}
}