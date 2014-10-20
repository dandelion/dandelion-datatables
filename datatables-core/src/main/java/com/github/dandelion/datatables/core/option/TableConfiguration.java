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
package com.github.dandelion.datatables.core.option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.dandelion.core.html.AbstractHtmlTag;
import com.github.dandelion.core.i18n.MessageResolver;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.extension.feature.ExtraHtml;
import com.github.dandelion.datatables.core.extension.feature.ExtraJs;

/**
 * Contains all the table configuration.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class TableConfiguration {

	/**
	 * The table DOM id.
	 */
	private final String tableId;
	private final Map<Option<?>, Object> configurations;
	private final MessageResolver messageResolver;
	private final HttpServletRequest request;

	private Map<Option<?>, Object> stagingConfiguration;

	// Dandelion-Datatables parameters
	private Set<ExtraJs> extraJs;
	private List<Callback> extraCallbacks;
	private List<ExtraHtml> extraHtmls;

	// Export parameters
	private Map<String, ExportConf> exportConfiguration;
	private Boolean exporting;
	private Boolean isExportable = false;
	private String currentExportFormat;

	// I18n
	private Properties messages = new Properties();

	private Set<Extension> internalExtensions;
	private HttpServletResponse response;

	/**
	 * <b>FOR INTERNAL USE ONLY</b>
	 * 
	 * @param userConf
	 */
	TableConfiguration(String tableId, Map<Option<?>, Object> userConf, MessageResolver messageResolver,
			HttpServletRequest request) {
		this.tableId = tableId;
		this.configurations = userConf;
		this.messageResolver = messageResolver;
		this.request = request;
		this.stagingConfiguration = new HashMap<Option<?>, Object>();
		this.exportConfiguration = new LinkedHashMap<String, ExportConf>();
	}

	/**
	 * TODO
	 * 
	 * @param objectToClone
	 *            Source object to clone.
	 * @param request
	 *            The request attached to the {@link TableConfiguration}
	 *            instance.
	 */
	static TableConfiguration clone(TableConfiguration original) {
		TableConfiguration clone = new TableConfiguration(original.getTableId(), original.getConfigurations(),
				original.getMessageResolver(), original.getRequest());
		clone.setExtraJs(original.getExtraJs());
		clone.setCallbacks(original.getCallbacks());
		clone.setExtraHtmlSnippets(original.getExtraHtmlSnippets());
		clone.setExporting(original.getExporting());
		clone.setMessages(original.getMessages());
		return clone;
	}

	public void set(String exportFormat, ExportConf exportConf) {
		this.exportConfiguration.put(exportFormat, exportConf);
	}

	public Map<String, ExportConf> getExportConfiguration() {
		return exportConfiguration;
	}

	public void setExportConfiguration(Map<String, ExportConf> exports) {
		this.exportConfiguration = exports;
	}

	public Map<Option<?>, Object> getConfigurations() {
		return configurations;
	}

	/**
	 * Register an extension in the TableConfiguration.
	 * 
	 * @param extension
	 *            The extension to register.
	 */
	public TableConfiguration registerExtension(Extension extension) {
		if (this.internalExtensions == null) {
			this.internalExtensions = new HashSet<Extension>();
		}
		this.internalExtensions.add(extension);
		return this;
	}

	public Set<ExtraJs> getExtraJs() {
		return extraJs;
	}

	public TableConfiguration addExtraJs(ExtraJs extraJs) {
		if (this.extraJs == null) {
			this.extraJs = new HashSet<ExtraJs>();
		}
		this.extraJs.add(extraJs);
		return this;
	}

	public void setExtraJs(Set<ExtraJs> extraJs) {
		this.extraJs = extraJs;
	}

	public Set<Extension> getInternalExtensions() {
		return internalExtensions;
	}

	public TableConfiguration setInternalExtensions(Set<Extension> extensions) {
		this.internalExtensions = extensions;
		return this;
	}

	public List<Callback> getCallbacks() {
		return extraCallbacks;
	}

	public void setCallbacks(List<Callback> callbacks) {
		this.extraCallbacks = callbacks;
	}

	public TableConfiguration registerCallback(Callback callback) {
		if (this.extraCallbacks == null) {
			this.extraCallbacks = new ArrayList<Callback>();
		}
		this.extraCallbacks.add(callback);
		return this;
	}

	public Boolean hasCallback(CallbackType callbackType) {
		if (this.extraCallbacks != null) {
			for (Callback callback : this.extraCallbacks) {
				if (callback.getType().equals(callbackType)) {
					return true;
				}
			}
		}
		return false;
	}

	public Callback getCallback(CallbackType callbackType) {
		for (Callback callback : this.extraCallbacks) {
			if (callback.getType().equals(callbackType)) {
				return callback;
			}
		}
		return null;
	}

	public Boolean getExporting() {
		return exporting;
	}

	public void setExporting(Boolean exporting) {
		this.exporting = exporting;
	}

	public Boolean isExportable() {
		return isExportable;
	}

	public void setIsExportable(Boolean isExportable) {
		this.isExportable = isExportable;
	}

	public TableConfiguration setExportTypes(String exportTypes) {
		return this;
	}

	public String getTableId() {
		return tableId;
	}

	public TableConfiguration addCssStyle(String cssStyle) {

		if (DatatableOptions.CSS_STYLE.valueFrom(this) == null) {
			DatatableOptions.CSS_STYLE.setIn(this, new StringBuilder());
		}
		else {
			DatatableOptions.CSS_STYLE.appendIn(this, AbstractHtmlTag.STYLE_SEPARATOR);
		}
		DatatableOptions.CSS_STYLE.appendIn(this, cssStyle);
		return this;
	}

	public TableConfiguration addCssClass(String cssClass) {
		if (DatatableOptions.CSS_CLASS.valueFrom(this) == null) {
			DatatableOptions.CSS_CLASS.setIn(this, new StringBuilder());
		}
		else {
			DatatableOptions.CSS_CLASS.appendIn(this, AbstractHtmlTag.CLASS_SEPARATOR);
		}
		DatatableOptions.CSS_CLASS.appendIn(this, cssClass);
		return this;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public ExportConf getExportConf(String format) {
		return exportConfiguration.get(format);
	}

	public Properties getMessages() {
		return messages;
	}

	public MessageResolver getMessageResolver() {
		return messageResolver;
	}

	public void setMessages(Properties messages) {
		this.messages = messages;
	}

	public String getMessage(String key) {
		return this.messages.getProperty(key);
	}

	public List<ExtraHtml> getExtraHtmlSnippets() {
		return extraHtmls;
	}

	public void setExtraHtmlSnippets(List<ExtraHtml> linkGroups) {
		this.extraHtmls = linkGroups;
	}

	public void addExtraHtmlSnippet(ExtraHtml extraHtml) {
		if (extraHtmls == null) {
			extraHtmls = new ArrayList<ExtraHtml>();
		}
		this.extraHtmls.add(extraHtml);
	}

	public String getCurrentExportFormat() {
		return currentExportFormat;
	}

	public void setCurrentExportFormat(String currentExport) {
		this.currentExportFormat = currentExport;
	}

	public Map<Option<?>, Object> getStagingConfiguration() {
		return stagingConfiguration;
	}

	public void addOption(Option<?> option, Object value) {
		this.configurations.put(option, value);
	}

	public void setStagingConfiguration(Map<Option<?>, Object> stagingConfiguration) {
		this.stagingConfiguration = stagingConfiguration;
	}

	public void addStagingConf(Option<?> configToken, Object value) {
		this.stagingConfiguration.put(configToken, value);
	}
}