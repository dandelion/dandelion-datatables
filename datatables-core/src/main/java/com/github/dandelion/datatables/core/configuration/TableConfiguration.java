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
package com.github.dandelion.datatables.core.configuration;

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

import com.github.dandelion.datatables.core.asset.ExtraConf;
import com.github.dandelion.datatables.core.asset.ExtraFile;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.html.ExtraHtml;
import com.github.dandelion.datatables.core.html.HtmlTag;
import com.github.dandelion.datatables.core.i18n.MessageResolver;

/**
 * Contains all the table configuration.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class TableConfiguration {

	private Map<ConfigToken<?>, Object> configurations;
	private Map<ConfigToken<?>, Object> stagingConfiguration;
	private Map<String, ExportConf> exportConfiguration;

	// Dandelion-Datatables parameters
	private List<ExtraFile> extraFiles;
	private List<ExtraConf> extraConfs;
	private List<Callback> extraCallbacks;

	// Export parameters
	private Boolean exporting;
	private Boolean isExportable = false;
	private String currentExportFormat;

	// I18n
	private Properties messages = new Properties();
	private MessageResolver internalMessageResolver;

	// Class of the iterated objects. Only used in XML export.
	private Set<Extension> internalExtensions;
	private String tableId; // A CONSERVER
	private List<ExtraHtml> extraHtmlSnippets;
	private HttpServletRequest request; // A CONSERVER
	private HttpServletResponse response;// A CONSERVER

	/**
	 * Return an instance of {@link TableConfiguration} for the
	 * <code>DEFAULT_GROUP_NAME</code> (global), i.e. containing all global
	 * configurations.
	 * 
	 * @param request
	 *            The request is not used yet but will to work with Locale.
	 * @return an instance of {@link TableConfiguration} that contains all the
	 *         table configuration.
	 */
	public static TableConfiguration getInstance(String tableId, HttpServletRequest request) {
		return getInstance(tableId, request, ConfigurationLoader.DEFAULT_GROUP_NAME);
	}

	/**
	 * <p>
	 * Return an instance of {@link TableConfiguration} for the given groupName.
	 * The instance is retrieved from the {@link ConfigurationStore}.
	 * <p>
	 * If the passed group name doesn't exist, the DEFAULT_GROUP_NAME (global)
	 * will be used.
	 * 
	 * @param request
	 * 
	 * @param groupName
	 *            Name of the configuration group to load.
	 * @return an instance of {@link TableConfiguration} that contains all the
	 *         table configuration.
	 */
	public static TableConfiguration getInstance(String tableId, HttpServletRequest request, String groupName) {

		// Retrieve the TableConfiguration prototype from the store
		TableConfiguration prototype = ConfigurationStore.getPrototype(request, groupName);

		// Clone the TableConfiguration for the local use
		return new TableConfiguration(tableId, prototype, request);
	}

	/**
	 * 
	 * <b>FOR INTERNAL USE ONLY</b>
	 * 
	 * @param userConf
	 */
	public TableConfiguration(Map<ConfigToken<?>, Object> userConf, HttpServletRequest request) {
		this.request = request;
		this.configurations = userConf;
		this.stagingConfiguration = new HashMap<ConfigToken<?>, Object>();
		this.exportConfiguration = new LinkedHashMap<String, ExportConf>();
	}

	/**
	 * Private constructor used to build clones of TableConfiguration.
	 * 
	 * @param objectToClone
	 *            Source object to clone.
	 * @param request
	 *            The request attached to the {@link TableConfiguration}
	 *            instance.
	 */
	private TableConfiguration(String tableId, TableConfiguration objectToClone, HttpServletRequest request) {
		this.request = request;
		this.tableId = tableId;
		this.configurations = objectToClone.configurations;
		this.stagingConfiguration = objectToClone.stagingConfiguration;
		this.exportConfiguration = objectToClone.exportConfiguration;

		// Dandelion-Datatables parameters
		this.extraFiles = objectToClone.extraFiles;
		this.extraConfs = objectToClone.extraConfs;
		this.extraCallbacks = objectToClone.extraCallbacks;

		// Export parameters
		this.exporting = objectToClone.exporting;
		this.isExportable = objectToClone.isExportable;

		this.internalMessageResolver = objectToClone.internalMessageResolver;
		this.messages = objectToClone.messages;
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

	public void set(ConfigToken<?> configToken, Object object) {
		configurations.put(configToken, object);
	}

	public Map<ConfigToken<?>, Object> getConfigurations() {
		return configurations;
	}

	public void setConfig(Map<ConfigToken<?>, Object> configurations2) {
		this.configurations = configurations2;
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

	public List<ExtraFile> getExtraFiles() {
		return extraFiles;
	}

	public TableConfiguration addExtraFile(ExtraFile extraFile) {
		if (this.extraFiles == null) {
			this.extraFiles = new ArrayList<ExtraFile>();
		}
		this.extraFiles.add(extraFile);
		return this;
	}

	public List<ExtraConf> getExtraConfs() {
		return extraConfs;
	}

	public TableConfiguration addExtraConf(ExtraConf extraConf) {
		if (this.extraConfs == null) {
			this.extraConfs = new ArrayList<ExtraConf>();
		}
		this.extraConfs.add(extraConf);
		return this;
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

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public TableConfiguration addCssStyle(String cssStyle) {
		if (TableConfig.CSS_STYLE.valueFrom(this) == null) {
			TableConfig.CSS_STYLE.setIn(this, new StringBuilder());
		} else {
			TableConfig.CSS_STYLE.appendIn(this, HtmlTag.CSS_SEPARATOR);
		}
		TableConfig.CSS_STYLE.appendIn(this, cssStyle);
		return this;
	}

	public TableConfiguration addCssClass(String cssClass) {
		if (TableConfig.CSS_CLASS.valueFrom(this) == null) {
			TableConfig.CSS_CLASS.setIn(this, new StringBuilder());
		} else {
			TableConfig.CSS_CLASS.appendIn(this, HtmlTag.CLASS_SEPARATOR);
		}
		TableConfig.CSS_CLASS.appendIn(this, cssClass);
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

	public void setMessages(Properties messages) {
		this.messages = messages;
	}

	public void setInternalMessageResolver(MessageResolver internalResourceProvider) {
		this.internalMessageResolver = internalResourceProvider;
	}

	public MessageResolver getInternalMessageResolver() {
		return internalMessageResolver;
	}

	public String getMessage(String key) {
		return this.messages.getProperty(key);
	}

	public List<ExtraHtml> getExtraHtmlSnippets() {
		return extraHtmlSnippets;
	}

	public void setExtraHtmlSnippets(List<ExtraHtml> linkGroups) {
		this.extraHtmlSnippets = linkGroups;
	}

	public void addExtraHtmlSnippet(ExtraHtml extraHtml) {
		if (extraHtmlSnippets == null) {
			extraHtmlSnippets = new ArrayList<ExtraHtml>();
		}
		extraHtmlSnippets.add(extraHtml);
	}

	public String getCurrentExportFormat() {
		return currentExportFormat;
	}

	public void setCurrentExportFormat(String currentExport) {
		this.currentExportFormat = currentExport;
	}

	public Map<ConfigToken<?>, Object> getStagingConfiguration() {
		return stagingConfiguration;
	}

	public void setStagingConfiguration(Map<ConfigToken<?>, Object> stagingConfiguration) {
		this.stagingConfiguration = stagingConfiguration;
	}
	
	public void addStagingConf(ConfigToken<?> configToken, Object value){
		this.stagingConfiguration.put(configToken, value);
	}
}