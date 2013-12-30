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
package com.github.dandelion.datatables.thymeleaf.processor.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.util.DOMUtils;

import com.github.dandelion.core.utils.EnumUtils;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.asset.ExtraFile;
import com.github.dandelion.datatables.core.asset.InsertMode;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.constants.HttpMethod;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.exception.DandelionDatatablesException;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportConf.Orientation;
import com.github.dandelion.datatables.core.html.ExtraHtml;
import com.github.dandelion.datatables.core.util.UrlUtils;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractConfigAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.util.AttributeUtils;

/**
 * <p>
 * Attribute processor applied to the HTML {@code div} tag with the
 * {@code dt:confType} attribute.
 * 
 * <p>
 * The {@code div} targeted by this processor fill the configuration map
 * initialized in the {@link DivConfAttrProcessor} with one (or more) of the
 * following configurations:
 * <ul>
 * <li>A {@link Callback}, using {@code dt:confType="callback"}</li>
 * <li>An export configuration ({@link ExportConf}), using
 * {@code dt:confType="export"}</li>
 * <li>An {@link ExtraFile}, using {@code dt:confType="extrafile"}</li>
 * <li>A configuration property, using {@code dt:confType="property"}</li>
 * <li>An extra HTML snippet, using {@code dt:confType="extrahtml"}</li>
 * </ul>
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public class DivConfTypeAttrProcessor extends AbstractConfigAttrProcessor {

	private Arguments arguments;

	public DivConfTypeAttrProcessor(IAttributeNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return DataTablesDialect.DT_HIGHEST_PRECEDENCE;
	}

	@Override
	protected void doProcessAttribute(Arguments arguments, Element element, String attributeName) {

		checkMarkupUsage(element);

		this.arguments = arguments;
		currentTableId = getTableId(element);

		String confTypeStr = AttributeUtils.parseStringAttribute(arguments, element, attributeName);
		ConfType confType = null;
		try {
			confType = ConfType.valueOf(confTypeStr.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			StringBuilder sb = new StringBuilder();
			sb.append("'");
			sb.append(confTypeStr.trim());
			sb.append("' is not a valid configuration type. Possible values are: ");
			sb.append(EnumUtils.printPossibleValuesOf(ConfType.class));
			throw new ConfigurationProcessingException(sb.toString());
		}

		switch (confType) {
		case CALLBACK:
			processCallbackAttributes(element);
			break;
		case EXPORT:
			processExportAttributes(element);
			break;
		case EXTRAFILE:
			processExtrafileAttributes(element);
			break;
		case PROPERTY:
			processPropertyAttributes(element);
			break;
		case EXTRAHTML:
			processExtrahtmlAttributes(element);
			break;
		}
	}

	private void checkMarkupUsage(Element element) {
		Element parent = (Element) element.getParent();

		if (parent == null || !"div".equals(parent.getNormalizedName())
				|| !parent.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":conf")) {
			throw new DandelionDatatablesException(
					"The element 'div dt:confType=\"...\"' must be inside an element 'div dt:conf=\"tableId\"'.");
		}
	}

	/**
	 * Processes ExtraHtml attributes in order to build an instance of
	 * {@link ExtraHtml}.
	 * 
	 * @param element
	 *            The {@code div} element which holds the attribute.
	 */
	@SuppressWarnings("unchecked")
	private void processExtrahtmlAttributes(Element element) {

		if (hasAttribute(element, "uid")) {

			ExtraHtml extraHtml = new ExtraHtml();
			extraHtml.setUid(getStringValue(element, "uid"));

			if (hasAttribute(element, "container")) {
				extraHtml.setContainer(getStringValue(element, "container"));
			} else {
				extraHtml.setContainer("div");
			}

			if (hasAttribute(element, "cssStyle")) {
				extraHtml.setCssStyle(getStringValue(element, "cssStyle"));
			}
			if (hasAttribute(element, "cssClass")) {
				extraHtml.setCssClass(getStringValue(element, "cssClass"));
			}

			if (!element.getChildren().isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (Node child : element.getChildren()) {
					sb.append(DOMUtils.getXmlFor(child).replaceAll("[\n\r]", "").trim());
				}
				extraHtml.setContent(sb.toString());
			}

			if (configs.get(currentTableId).containsKey(ConfType.EXTRAHTML)) {
				List<ExtraHtml> extraHtmls = (List<ExtraHtml>) configs.get(currentTableId).get(ConfType.EXTRAHTML);

				extraHtmls.add(extraHtml);
			} else {
				List<ExtraHtml> extraHtmls = new ArrayList<ExtraHtml>();
				extraHtmls.add(extraHtml);

				configs.get(currentTableId).put(ConfType.EXTRAHTML, extraHtmls);
			}

		} else {
			throw new ConfigurationProcessingException(
					"The attribute 'dt:uid' is required when defining an extra HTML snippet.");
		}
	}

	/**
	 * Processes export attributes in order to build an instance of
	 * {@link ExportConf}.
	 * 
	 * @param element
	 *            The {@code div} element which holds the attribute.
	 */
	@SuppressWarnings("unchecked")
	private void processExportAttributes(Element element) {

		ExportConf conf = null;
		String exportFormat = null;

		if (hasAttribute(element, "type")) {
			exportFormat = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":type");
			conf = new ExportConf(exportFormat);
		} else {
			throw new ConfigurationProcessingException(
					"The attribute 'dt:type' is required when defining an export configuration.");
		}

		if (hasAttribute(element, "fileName")) {
			conf.setFileName(getStringValue(element, "fileName"));
		}

		if (hasAttribute(element, "mimeType")) {
			conf.setMimeType(getStringValue(element, "mimeType"));
		}

		if (hasAttribute(element, "label")) {
			conf.setLabel(getStringValue(element, "label"));
		}

		if (hasAttribute(element, "cssStyle")) {
			conf.setCssStyle(new StringBuilder(getStringValue(element, "cssStyle")));
		}

		if (hasAttribute(element, "cssClass")) {
			conf.setCssStyle(new StringBuilder(getStringValue(element, "cssClass")));
		}

		if (hasAttribute(element, "includeHeader")) {
			conf.setIncludeHeader(Boolean.parseBoolean(getStringValue(element, "includeHeader")));
		}

		String exportUrl = null;
		if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":url")) {
			// Custom mode (export using controller)
			String url = AttributeUtils.parseStringAttribute(arguments, element,
					DataTablesDialect.DIALECT_PREFIX + ":url").trim();
			exportUrl = UrlUtils.getCustomExportUrl(request, response, url);
			conf.setHasCustomUrl(true);
		} else {
			// Default mode (export using filter)
			exportUrl = UrlUtils.getExportUrl(request, response, exportFormat, currentTableId);
			conf.setHasCustomUrl(false);
		}
		conf.setUrl(exportUrl);

		if (hasAttribute(element, "method")) {
			String methodStr = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":method");

			HttpMethod methodEnum = null;
			try {
				methodEnum = HttpMethod.valueOf(methodStr.toUpperCase().trim());
			} catch (IllegalArgumentException e) {
				StringBuilder sb = new StringBuilder();
				sb.append("'");
				sb.append(methodStr);
				sb.append("' is not a valid HTTP method. Possible values are: ");
				sb.append(EnumUtils.printPossibleValuesOf(HttpMethod.class));
				throw new ConfigurationProcessingException(sb.toString());
			}

			conf.setMethod(methodEnum);
		}

		if (hasAttribute(element, "autoSize")) {
			conf.setAutoSize(Boolean.parseBoolean(getStringValue(element, "autoSize")));
		}

		if (hasAttribute(element, "exportClass")) {
			conf.setExportClass(getStringValue(element, "exportClass"));
		}

		if (hasAttribute(element, "extension")) {
			conf.setExtension(getStringValue(element, "extension"));
		}

		if (hasAttribute(element, "orientation")) {
			String orientationStr = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":orientation");

			Orientation orientationEnum = null;
			try {
				orientationEnum = Orientation.valueOf(orientationStr.toUpperCase().trim());
			} catch (IllegalArgumentException e) {
				StringBuilder sb = new StringBuilder();
				sb.append("'");
				sb.append(orientationStr);
				sb.append("' is not a valid orientation. Possible values are: ");
				sb.append(EnumUtils.printPossibleValuesOf(Orientation.class));
				throw new ConfigurationProcessingException(sb.toString());
			}

			conf.setOrientation(orientationEnum);
		}

		if (conf != null) {

			if (configs.get(currentTableId).containsKey(ConfType.EXPORT)) {
				((Map<String, ExportConf>) configs.get(currentTableId).get(ConfType.EXPORT)).put(exportFormat, conf);
			} else {
				Map<String, ExportConf> exportConfiguration = new HashMap<String, ExportConf>();
				exportConfiguration.put(exportFormat, conf);
				configs.get(currentTableId).put(ConfType.EXPORT, exportConfiguration);
			}
		}
	}

	/**
	 * Processes attributes in order to build an instance of {@link Callback}.
	 * 
	 * @param element
	 *            The {@code div} element which holds the attribute.
	 */
	@SuppressWarnings("unchecked")
	private void processCallbackAttributes(Element element) {

		if (hasAttribute(element, "type")) {

			String typeStr = getStringValue(element, "type");

			if (hasAttribute(element, "function")) {

				String functionStr = getStringValue(element, "function");
				CallbackType callbackType = null;
				try {
					callbackType = CallbackType.valueOf(typeStr.toUpperCase().trim());
				} catch (IllegalArgumentException e) {
					StringBuilder sb = new StringBuilder();
					sb.append("'");
					sb.append(typeStr);
					sb.append("' is not a valid callback type. Possible values are: ");
					sb.append(EnumUtils.printPossibleValuesOf(CallbackType.class));
					throw new ConfigurationProcessingException(sb.toString());
				}

				if (configs.get(currentTableId).containsKey(ConfType.CALLBACK)) {
					List<Callback> callbacks = (List<Callback>) configs.get(currentTableId).get(ConfType.CALLBACK);

					if (Callback.hasCallback(callbackType, callbacks)) {
						Callback.findByType(callbackType, callbacks).appendCode(
								(callbackType.hasReturn() ? "return " : "") + functionStr + "("
										+ StringUtils.join(callbackType.getArgs(), ",") + ");");
					} else {
						callbacks.add(new Callback(callbackType, (callbackType.hasReturn() ? "return " : "")
								+ functionStr + "(" + StringUtils.join(callbackType.getArgs(), ",") + ");"));
					}
				} else {
					List<Callback> callbacks = new ArrayList<Callback>();
					callbacks.add(new Callback(callbackType, (callbackType.hasReturn() ? "return " : "") + functionStr
							+ "(" + StringUtils.join(callbackType.getArgs(), ",") + ");"));

					configs.get(currentTableId).put(ConfType.CALLBACK, callbacks);
				}
			} else {
				throw new ConfigurationProcessingException("The attribute '" + DataTablesDialect.DIALECT_PREFIX
						+ ":function' is required when defining a callback.");
			}
		} else {
			throw new ConfigurationProcessingException("The attribute '" + DataTablesDialect.DIALECT_PREFIX
					+ ":type' is required when defining a callback.");
		}
	}

	/**
	 * Processes attributes in order to build an instance of {@link ExtraFile}.
	 * 
	 * @param element
	 *            The {@code div} element which holds the attribute.
	 */
	@SuppressWarnings("unchecked")
	private void processExtrafileAttributes(Element element) {

		if (hasAttribute(element, "src")) {

			String src = AttributeUtils.parseStringAttribute(arguments, element, DataTablesDialect.DIALECT_PREFIX
					+ ":src");
			InsertMode insertMode = null;

			if (hasAttribute(element, "insert")) {
				String insert = getStringValue(element, "insert");
				try {
					insertMode = InsertMode.valueOf(insert.toUpperCase().trim());
				} catch (IllegalArgumentException e) {
					StringBuilder sb = new StringBuilder();
					sb.append("'");
					sb.append(insert);
					sb.append("' is not a valid insert mode. Possible values are: ");
					sb.append(EnumUtils.printPossibleValuesOf(InsertMode.class));
					throw new ConfigurationProcessingException(sb.toString());
				}
			} else {
				insertMode = InsertMode.BEFOREALL;
			}

			if (configs.get(currentTableId).containsKey(ConfType.EXTRAFILE)) {
				((List<ExtraFile>) configs.get(currentTableId).get(ConfType.EXTRAFILE)).add(new ExtraFile(src,
						insertMode));
			} else {
				List<ExtraFile> extraFiles = new ArrayList<ExtraFile>();
				extraFiles.add(new ExtraFile(src, insertMode));
				configs.get(currentTableId).put(ConfType.EXTRAFILE, extraFiles);

			}
		} else {
			throw new ConfigurationProcessingException(
					"The attribute 'dt:src' is required when defining an extra file.");
		}
	}

	/**
	 * Processes attributes in order to overload locally the properties
	 * configured globally.build an instance of {@link Callback}.
	 * 
	 * @param element
	 *            The {@code div} element which holds the attribute.
	 */
	@SuppressWarnings("unchecked")
	private void processPropertyAttributes(Element element) {

		if (hasAttribute(element, "name")) {

			String name = getStringValue(element, "name");
			ConfigToken<?> configToken = TableConfig.findByPropertyName(name);
			String value = getStringValue(element, "value");

			if (configToken == null) {
				throw new ConfigurationProcessingException("'" + name
						+ "' is not a valid property. Please read the documentation.");
			} else {

				if (configs.get(currentTableId).containsKey(ConfType.PROPERTY)) {
					((Map<ConfigToken<?>, Object>) configs.get(currentTableId).get(ConfType.PROPERTY)).put(configToken,
							value);
				} else {
					Map<ConfigToken<?>, Object> stagingConf = new HashMap<ConfigToken<?>, Object>();
					stagingConf.put(configToken, value);
					configs.get(currentTableId).put(ConfType.PROPERTY, stagingConf);
				}
			}
		} else {
			throw new ConfigurationProcessingException(
					"The attribute 'dt:name' is required when overloading a configuration property.");
		}
	}

	public boolean hasAttribute(Element element, String attribute) {
		return element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":" + attribute)
				&& StringUtils
						.isNotBlank(element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":" + attribute));
	}

	public String getStringValue(Element element, String attribute) {
		return String.valueOf(element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":" + attribute));
	}
}
