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
package com.github.dandelion.datatables.core.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.asset.ExtraConf;
import com.github.dandelion.datatables.core.asset.ExtraFile;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.exception.ConfigurationLoadingException;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.export.ExportProperties;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.extension.feature.FilterPlaceholder;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.extension.theme.ThemeOption;
import com.github.dandelion.datatables.core.html.HtmlTag;
import com.github.dandelion.datatables.core.i18n.MessageResolver;

/**
 * Contains all the table configuration.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class TableConfiguration {

	// DataTables global parameters
	private Boolean featureInfo;
	private Boolean featureAutoWidth;
	private Boolean featureFilterable;
	private FilterPlaceholder featureFilterPlaceholder;
	private Boolean featurePaginate;
	private PaginationType featurePaginationType;
	private Boolean featureLengthChange;
	private Boolean featureSort;
	private Boolean featureStateSave;
	private Boolean featureJqueryUi;
	private String featureLengthMenu;
	private Integer featureDisplayLength;
	private String featureDom;
	private String featureScrolly;
	private Boolean featureScrollCollapse;
	private String featureScrollx;
	private String featureScrollXInner;
	private String featureAppear;
	private String featureAppearDuration;

	// CSS parameters
	private StringBuilder cssStyle;
	private StringBuilder cssClass;
	private String cssStripeClasses;
	private Extension cssTheme;
	private ThemeOption cssThemeOption;

	// DataTables AJAX parameters
	private Boolean ajaxProcessing;
	private Boolean ajaxDeferRender;
	private Boolean ajaxServerSide;
	private String ajaxSource;
	private Boolean ajaxPipelining;
	private Integer ajaxPipeSize;
	private String ajaxServerData;
	private String ajaxServerParam;
	private String ajaxServerMethod;

	// DataTables plugin parameters
	private String pluginFixedPosition;
	private Integer pluginFixedOffsetTop;
	private Boolean pluginFixedHeader;
	private Boolean pluginScroller;
	private Boolean pluginColReorder;

	// Dandelion-Datatables parameters
	private List<ExtraFile> extraFiles;
	private List<ExtraConf> extraConfs;
	private List<Callback> extraCallbacks;

	// Export parameters
	private ExportProperties exportProperties;
	private Boolean exporting;
	private Set<ExportConf> exportConfs;
	private Set<ExportLinkPosition> exportLinkPositions;
	private Boolean isExportable = false;
	private String exportDefaultXlsClass;
	private String exportDefaultXlsxClass;
	private String exportDefaultPdfClass;
	private String exportDefaultXmlClass;
	private String exportDefaultCsvClass;
	private String exportXlsClass;
	private String exportXlsxClass;
	private String exportPdfClass;
	private String exportXmlClass;
	private String exportCsvClass;
	
	// Configuration
	private String mainExtensionPackage;
	private Set<String> mainExtensionNames;
	private String mainUrlBase;
	private Boolean mainCdn;
	private String mainCdnJs;
	private String mainCdnCss;

	// I18n
	private Properties messages = new Properties();
	private MessageResolver internalMessageResolver;

	// Class of the iterated objects. Only used in XML export.
	private String internalObjectType;
	private Set<Extension> internalExtensions;
	private String tableId;
	private HttpServletRequest request;

	/**
	 * Return an instance of {@link TableConfiguration} for the
	 * DEFAULT_GROUP_NAME (global), i.e. containing all global configurations.
	 * 
	 * @param request
	 *            The request is not used yet but will to work with Locale.
	 * @return an instance of {@link TableConfiguration} that contains all the
	 *         table configuration.
	 */
	public static TableConfiguration getInstance(HttpServletRequest request) {
		return getInstance(request, ConfigurationLoader.DEFAULT_GROUP_NAME);
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
	public static TableConfiguration getInstance(HttpServletRequest request, String groupName) {
		
		// Retrieve the TableConfiguration prototype from the store
		TableConfiguration prototype = ConfigurationStore.getPrototype(request, groupName);
		
		// Clone the TableConfiguration for the local use
		return new TableConfiguration(prototype, request);
	}
	
	/**
	 * 
	 * <b>FOR INTERNAL USE ONLY</b>
	 * @param stagingConf
	 * @throws ConfigurationLoadingException  
	 */
	public TableConfiguration(Map<Configuration, Object> stagingConf, HttpServletRequest request) 
			throws ConfigurationLoadingException {
		this.request = request;
		Configuration.applyConfiguration(this, stagingConf);
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
	private TableConfiguration (TableConfiguration objectToClone, HttpServletRequest request){
		this.request = request;
		
		// DataTables global parameters
		featureInfo = objectToClone.featureInfo;
		featureAutoWidth = objectToClone.featureAutoWidth;
		featureFilterable = objectToClone.featureFilterable;
		featureFilterPlaceholder = objectToClone.featureFilterPlaceholder;
		featurePaginate = objectToClone.featurePaginate;
		featurePaginationType = objectToClone.featurePaginationType;
		featureLengthChange = objectToClone.featureLengthChange;
		featureSort = objectToClone.featureSort;
		featureStateSave = objectToClone.featureStateSave;
		featureJqueryUi = objectToClone.featureJqueryUi;
		featureLengthMenu = objectToClone.featureLengthMenu;
		featureDisplayLength = objectToClone.featureDisplayLength;
		featureDom = objectToClone.featureDom;
		featureScrolly = objectToClone.featureScrolly;
		featureScrollCollapse = objectToClone.featureScrollCollapse;
		featureScrollx = objectToClone.featureScrollx;
		featureScrollXInner = objectToClone.featureScrollXInner;
		featureAppear = objectToClone.featureAppear;
		featureAppearDuration = objectToClone.featureAppearDuration;

		// CSS parameters
		cssStyle = objectToClone.cssStyle;
		cssClass = objectToClone.cssClass;
		cssStripeClasses = objectToClone.cssStripeClasses;
		cssTheme = objectToClone.cssTheme;
		cssThemeOption = objectToClone.cssThemeOption;

		// DataTables AJAX parameters
		ajaxProcessing = objectToClone.ajaxProcessing;
		ajaxDeferRender = objectToClone.ajaxDeferRender;
		ajaxServerSide = objectToClone.ajaxServerSide;
		ajaxSource = objectToClone.ajaxSource;
		ajaxPipelining = objectToClone.ajaxPipelining;
		ajaxPipeSize = objectToClone.ajaxPipeSize;
		ajaxServerData = objectToClone.ajaxServerData;
		ajaxServerParam = objectToClone.ajaxServerParam;
		ajaxServerMethod = objectToClone.ajaxServerMethod;

		// DataTables plugin parameters
		pluginFixedPosition = objectToClone.pluginFixedPosition;
		pluginFixedOffsetTop = objectToClone.pluginFixedOffsetTop;
		pluginFixedHeader = objectToClone.pluginFixedHeader;
		pluginScroller = objectToClone.pluginScroller;
		pluginColReorder = objectToClone.pluginColReorder;

		// Dandelion-Datatables parameters
		extraFiles = objectToClone.extraFiles;
		extraConfs = objectToClone.extraConfs;
		extraCallbacks = objectToClone.extraCallbacks;

		// Export parameters
		exportProperties = objectToClone.exportProperties;
		exporting = objectToClone.exporting;
		exportConfs = objectToClone.getExportConfs() != null ? new HashSet<ExportConf>(objectToClone.exportConfs) : null;
		exportLinkPositions = objectToClone.exportLinkPositions != null ? new HashSet<ExportLinkPosition>(
				objectToClone.exportLinkPositions) : null;
		isExportable = objectToClone.isExportable;
		exportDefaultXlsClass = objectToClone.exportDefaultXlsClass;
		exportDefaultXlsxClass = objectToClone.exportDefaultXlsxClass;
		exportDefaultPdfClass = objectToClone.exportDefaultPdfClass;
		exportDefaultXmlClass = objectToClone.exportDefaultXmlClass;
		exportDefaultCsvClass = objectToClone.exportDefaultCsvClass;
		exportXlsClass = objectToClone.exportXlsClass;
		exportXlsxClass = objectToClone.exportXlsxClass;
		exportPdfClass = objectToClone.exportPdfClass;
		exportXmlClass = objectToClone.exportXmlClass;
		exportCsvClass = objectToClone.exportCsvClass;
		
		// Configuration
		mainExtensionPackage = objectToClone.mainExtensionPackage;
		mainExtensionNames = objectToClone.mainExtensionNames;
		mainUrlBase = objectToClone.mainUrlBase;
		mainCdn = objectToClone.mainCdn;
		mainCdnJs = objectToClone.mainCdnJs;
		mainCdnCss = objectToClone.mainCdnCss;
		
		internalMessageResolver = objectToClone.internalMessageResolver;
		messages = objectToClone.messages;
	}


	/**
	 * TODO
	 * 
	 * @param exportType
	 * @return
	 */
	public String getExportClass(ExportType exportType) {

		String exportClass = null;

		switch (exportType) {
		case CSV:
			exportClass = exportCsvClass;
			if(StringUtils.isBlank(exportClass)){
				exportClass = exportDefaultCsvClass;
			}
			break;
		case PDF:
			exportClass = exportPdfClass;
			if (StringUtils.isBlank(exportClass)) {
				exportClass = exportDefaultPdfClass;
			}
			break;
		case XLS:
			exportClass = exportXlsClass;
			if (StringUtils.isBlank(exportClass)) {
				exportClass = exportDefaultXlsClass;
			}
			break;
		case XLSX:
			exportClass = exportXlsxClass;
			if (StringUtils.isBlank(exportClass)) {
				exportClass = exportDefaultXlsxClass;
			}
			break;
		case XML:
			exportClass = exportXmlClass;
			if (StringUtils.isBlank(exportClass)) {
				exportClass = exportDefaultXmlClass;
			}
			break;
		default:
			break;
		}

		return exportClass;
	}

	public Boolean getFeatureAutoWidth() {
		return featureAutoWidth;
	}

	public TableConfiguration setFeatureAutoWidth(Boolean autoWidth) {
		this.featureAutoWidth = autoWidth;
		return this;
	}

	public Boolean getAjaxDeferRender() {
		return ajaxDeferRender;
	}

	public TableConfiguration setAjaxDeferRender(Boolean deferRender) {
		this.ajaxDeferRender = deferRender;
		return this;
	}

	public Boolean getFeatureInfo() {
		return featureInfo;
	}

	public TableConfiguration setFeatureInfo(Boolean info) {
		this.featureInfo = info;
		return this;
	}

	public Boolean getFeatureFilterable() {
		return featureFilterable;
	}

	public TableConfiguration setFeatureFilterable(Boolean filterable) {
		this.featureFilterable = filterable;
		return this;
	}

	public Boolean getFeaturePaginate() {
		return featurePaginate;
	}

	public TableConfiguration setFeaturePaginate(Boolean paginate) {
		this.featurePaginate = paginate;
		return this;
	}

	public PaginationType getFeaturePaginationType() {
		return featurePaginationType;
	}

	public TableConfiguration setFeaturePaginationType(PaginationType  paginationType) {
		this.featurePaginationType = paginationType;
		return this;
	}

	public Boolean getFeatureLengthChange() {
		return featureLengthChange;
	}

	public TableConfiguration setFeatureLengthChange(Boolean lengthChange) {
		this.featureLengthChange = lengthChange;
		return this;
	}

	public Boolean getFeatureSort() {
		return featureSort;
	}

	public TableConfiguration setFeatureSort(Boolean sort) {
		this.featureSort = sort;
		return this;
	}

	public Boolean getFeatureStateSave() {
		return featureStateSave;
	}

	public TableConfiguration setFeatureStateSave(Boolean stateSave) {
		this.featureStateSave = stateSave;
		return this;
	}

	public Boolean getMainCdn() {
		return mainCdn;
	}

	public TableConfiguration setMainCdn(Boolean cdn) {
		this.mainCdn = cdn;
		return this;
	}

	public Boolean getFeatureJqueryUI() {
		return featureJqueryUi;
	}

	public TableConfiguration setFeatureJqueryUi(Boolean jqueryUI) {
		this.featureJqueryUi = jqueryUI;
		return this;
	}

	public String getFeatureAppear() {
		return featureAppear;
	}

	public TableConfiguration setFeatureAppear(String appear) {
		this.featureAppear = appear;
		return this;
	}
	
	public String getFeatureAppearDuration() {
		return featureAppearDuration;
	}

	public TableConfiguration setFeatureAppearDuration(String featureAppearDuration) {
		this.featureAppearDuration = featureAppearDuration;
		return this;
	}


	public String getFeatureLengthMenu() {
		return featureLengthMenu;
	}

	public TableConfiguration setFeatureLengthMenu(String lengthMenu) {
		this.featureLengthMenu = lengthMenu;
		return this;
	}

	public String getCssStripeClasses() {
		return cssStripeClasses;
	}

	public TableConfiguration setCssStripeClasses(String cssStripeClasses) {
		this.cssStripeClasses = cssStripeClasses;
		return this;
	}

	public Integer getFeatureDisplayLength() {
		return featureDisplayLength;
	}

	public TableConfiguration setFeatureDisplayLength(Integer displayLength) {
		this.featureDisplayLength = displayLength;
		return this;
	}

	public String getFeatureDom() {
		return featureDom;
	}

	public TableConfiguration setFeatureDom(String dom) {
		this.featureDom = dom;
		return this;
	}

	public Boolean getAjaxProcessing() {
		return ajaxProcessing;
	}

	public TableConfiguration setAjaxProcessing(Boolean processing) {
		this.ajaxProcessing = processing;
		return this;
	}

	public Boolean getAjaxServerSide() {
		return ajaxServerSide;
	}

	public TableConfiguration setAjaxServerSide(Boolean  serverSide) {
		this.ajaxServerSide = serverSide;
		return this;
	}

	public String getAjaxSource() {
		return ajaxSource;
	}

	public TableConfiguration setAjaxSource(String ajaxSource) {
		this.ajaxSource = ajaxSource;
		return this;
	}

	public Boolean getAjaxPipelining() {
		return ajaxPipelining;
	}

	public TableConfiguration setAjaxPipelining(Boolean pipelining) {
		this.ajaxPipelining = pipelining;
		return this;
	}

	public Integer getAjaxPipeSize() {
		return ajaxPipeSize;
	}

	public TableConfiguration setAjaxPipeSize(Integer pipeSize) {
		this.ajaxPipeSize = pipeSize;
		return this;
	}

	public String getAjaxServerData() {
		return ajaxServerData;
	}

	public TableConfiguration setAjaxServerData(String serverData) {
		this.ajaxServerData = serverData;
		return this;
	}

	public String getAjaxServerParam() {
		return ajaxServerParam;
	}

	public TableConfiguration setAjaxServerParam(String serverParam) {
		this.ajaxServerParam = serverParam;
		return this;
	}

	public String getAjaxServerMethod() {
		return ajaxServerMethod;
	}

	public TableConfiguration setAjaxServerMethod(String serverMethod) {
		this.ajaxServerMethod = serverMethod;
		return this;
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

	public String getFeatureScrollY() {
		return featureScrolly;
	}

	public TableConfiguration setFeatureScrolly(String scrollY) {
		this.featureScrolly = scrollY;
		return this;
	}

	public Boolean getFeatureScrollCollapse() {
		return featureScrollCollapse;
	}

	public TableConfiguration setFeatureScrollCollapse(Boolean scrollCollapse) {
		this.featureScrollCollapse = scrollCollapse;
		return this;
	}

	public String getFeatureScrollX() {
		return featureScrollx;
	}

	public TableConfiguration setFeatureScrollx(String scrollX) {
		this.featureScrollx = scrollX;
		return this;
	}

	public String getFeatureScrollXInner() {
		return featureScrollXInner;
	}

	public TableConfiguration setFeatureScrollXInner(String scrollInner) {
		this.featureScrollXInner = scrollInner;
		return this;
	}

	public String getPluginFixedPosition() {
		return pluginFixedPosition;
	}

	public TableConfiguration setPluginFixedPosition(String fixedPosition) {
		this.pluginFixedPosition = fixedPosition;
		return this;
	}

	public Integer getPluginFixedOffsetTop() {
		return pluginFixedOffsetTop;
	}

	public TableConfiguration setPluginFixedOffsetTop(Integer fixedOffsetTop) {
		this.pluginFixedOffsetTop = fixedOffsetTop;
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

	public Extension getCssTheme() {
		return cssTheme;
	}

	public TableConfiguration setCssTheme(Extension theme) {
		this.cssTheme = theme;
		return this;
	}

	public ThemeOption getCssThemeOption() {
		return cssThemeOption;
	}

	public TableConfiguration setCssThemeOption(ThemeOption themeOption) {
		this.cssThemeOption = themeOption;
		return this;
	}

	public Set<String> getMainExtensionNames() {
		return mainExtensionNames;
	}

	public TableConfiguration setMainExtensionNames(Set<String> extraCustomExtensions) {
		this.mainExtensionNames = extraCustomExtensions;
		return this;
	}

	public Boolean getExporting() {
		return exporting;
	}

	public void setExporting(Boolean exporting) {
		this.exporting = exporting;
	}

	public ExportProperties getExportProperties() {
		return exportProperties;
	}

	public void setExportProperties(ExportProperties exportProperties) {
		this.exportProperties = exportProperties;
	}

	public Boolean isExportable() {
		return isExportable;
	}

	public void setIsExportable(Boolean isExportable) {
		this.isExportable = isExportable;
	}

	public Set<ExportLinkPosition> getExportLinkPositions() {
		return exportLinkPositions;
	}

	public TableConfiguration setExportLinkPositions(Set<ExportLinkPosition> exportLinkPositions) {
		this.exportLinkPositions = exportLinkPositions;
		return this;
	}

	public String getInternalObjectType() {
		return internalObjectType;
	}

	public void setInternalObjectType(String objectType) {
		this.internalObjectType = objectType;
	}

	public Boolean getPluginFixedHeader() {
		return pluginFixedHeader;
	}

	public TableConfiguration setPluginFixedHeader(Boolean fixedHeader) {
		this.pluginFixedHeader = fixedHeader;
		return this;
	}

	public Boolean getPluginScroller() {
		return pluginScroller;
	}

	public TableConfiguration setPluginScroller(Boolean scroller) {
		this.pluginScroller = scroller;
		return this;
	}

	public Boolean getPluginColReorder() {
		return pluginColReorder;
	}

	public TableConfiguration setPluginColReorder(Boolean colReorder) {
		this.pluginColReorder = colReorder;
		return this;
	}
	
	public String getMainExtensionPackage() {
		return mainExtensionPackage;
	}

	public TableConfiguration setMainExtensionPackage(String mainBasePackage) {
		this.mainExtensionPackage = mainBasePackage;
		return this;
	}

	public String getMainUrlBase() {
		return mainUrlBase;
	}

	public TableConfiguration setMainUrlBase(String mainUrlBase) {
		this.mainUrlBase = mainUrlBase;
		return this;
	}

	public TableConfiguration setExportTypes(String exportTypes){
		return this;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public StringBuilder getCssStyle() {
		return cssStyle;
	}

	public TableConfiguration setCssStyle(StringBuilder cssStyle) {
		this.cssStyle = cssStyle;
		return this;
	}

	public TableConfiguration addCssStyle(String cssStyle) {
		if(this.cssStyle == null){
			this.cssStyle = new StringBuilder();
		}
		else{
			this.cssStyle.append(HtmlTag.CSS_SEPARATOR);
		}
		this.cssStyle.append(cssStyle);
		return this;
	}
	
	public StringBuilder getCssClass() {
		return cssClass;
	}

	public TableConfiguration setCssClass(StringBuilder cssClass) {
		this.cssClass = cssClass;
		return this;
	}

	public TableConfiguration addCssClass(String cssClass) {
		if(this.cssClass == null){
			this.cssClass = new StringBuilder();
		}
		else{
			this.cssClass.append(HtmlTag.CLASS_SEPARATOR);
		}
		this.cssClass.append(cssClass);
		return this;
	}
	
	public void setExportDefaultXlsClass(String exportDefaultXlsClass) {
		this.exportDefaultXlsClass = exportDefaultXlsClass;
	}

	public void setExportDefaultXlsxClass(String exportDefaultXlsxClass) {
		this.exportDefaultXlsxClass = exportDefaultXlsxClass;
	}

	public void setExportDefaultPdfClass(String exportDefaultPdfClass) {
		this.exportDefaultPdfClass = exportDefaultPdfClass;
	}

	public void setExportDefaultXmlClass(String exportDefaultXmlClass) {
		this.exportDefaultXmlClass = exportDefaultXmlClass;
	}

	public void setExportDefaultCsvClass(String exportDefaultCsvClass) {
		this.exportDefaultCsvClass = exportDefaultCsvClass;
	}

	public void setExportXlsClass(String exportXlsClass) {
		this.exportXlsClass = exportXlsClass;
	}

	public void setExportXlsxClass(String exportXlsxClass) {
		this.exportXlsxClass = exportXlsxClass;
	}

	public void setExportPdfClass(String exportPdfClass) {
		this.exportPdfClass = exportPdfClass;
	}

	public void setExportXmlClass(String exportXmlClass) {
		this.exportXmlClass = exportXmlClass;
	}

	public void setExportCsvClass(String exportCsvClass) {
		this.exportCsvClass = exportCsvClass;
	}

	public HttpServletRequest getRequest() {
		return request;
	}
	
	public Set<ExportConf> getExportConfs() {
		return exportConfs;
	}

	public void setExportConfs(Set<ExportConf> exportConfs) {
		this.exportConfs = exportConfs;
	}

	public ExportConf getExportConf(ExportType exportType){
		ExportConf retval = null;
		if(this.exportConfs != null){
			for(ExportConf exportConf : this.exportConfs){
				if(exportConf.getType().equals(exportType)){
					retval = exportConf;
					break;
				}
			}
		}
		return retval;
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
	
	public String getMessage(String key){
		return this.messages.getProperty(key);
	}

	public FilterPlaceholder getFeatureFilterPlaceholder() {
		return featureFilterPlaceholder;
	}

	public TableConfiguration setFeatureFilterPlaceholder(FilterPlaceholder featureFilterPlaceholder) {
		this.featureFilterPlaceholder = featureFilterPlaceholder;
		return this;
	}

	public String getMainCdnJs() {
		return mainCdnJs;
	}


	public TableConfiguration setMainCdnJs(String extraCdnJs) {
		this.mainCdnJs = extraCdnJs;
		return this;
	}


	public String getMainCdnCss() {
		return mainCdnCss;
	}


	public TableConfiguration setMainCdnCss(String extraCdnCss) {
		this.mainCdnCss = extraCdnCss;
		return this;
	}


	@Override
	public String toString() {
		return "TableConfiguration [featureInfo=" + featureInfo + ", featureAutoWidth=" + featureAutoWidth
				+ ", featureFilterable=" + featureFilterable + ", featureFilterPlaceholder=" + featureFilterPlaceholder
				+ ", featurePaginate=" + featurePaginate + ", featurePaginationType=" + featurePaginationType
				+ ", featureLengthChange=" + featureLengthChange + ", featureSort=" + featureSort
				+ ", featureStateSave=" + featureStateSave + ", featureJqueryUi=" + featureJqueryUi
				+ ", featureLengthMenu=" + featureLengthMenu + ", featureDisplayLength=" + featureDisplayLength
				+ ", featureDom=" + featureDom + ", featureScrolly=" + featureScrolly + ", featureScrollCollapse="
				+ featureScrollCollapse + ", featureScrollx=" + featureScrollx + ", featureScrollXInner="
				+ featureScrollXInner + ", featureAppear=" + featureAppear + ", featureAppearDuration="
				+ featureAppearDuration + ", cssStyle=" + cssStyle + ", cssClass=" + cssClass + ", cssStripeClasses="
				+ cssStripeClasses + ", cssTheme=" + cssTheme + ", cssThemeOption=" + cssThemeOption
				+ ", ajaxProcessing=" + ajaxProcessing + ", ajaxDeferRender=" + ajaxDeferRender + ", ajaxServerSide="
				+ ajaxServerSide + ", ajaxSource=" + ajaxSource + ", ajaxPipelining=" + ajaxPipelining
				+ ", ajaxPipeSize=" + ajaxPipeSize + ", ajaxServerData=" + ajaxServerData + ", ajaxServerParam="
				+ ajaxServerParam + ", ajaxServerMethod=" + ajaxServerMethod + ", pluginFixedPosition="
				+ pluginFixedPosition + ", pluginFixedOffsetTop=" + pluginFixedOffsetTop + ", pluginFixedHeader="
				+ pluginFixedHeader + ", pluginScroller=" + pluginScroller + ", pluginColReorder=" + pluginColReorder
				+ ", extraFiles=" + extraFiles + ", extraConfs=" + extraConfs + ", extraCallbacks=" + extraCallbacks
				+ ", exportProperties=" + exportProperties + ", exporting=" + exporting + ", exportConfs="
				+ exportConfs + ", exportLinkPositions=" + exportLinkPositions + ", isExportable=" + isExportable
				+ ", exportDefaultXlsClass=" + exportDefaultXlsClass + ", exportDefaultXlsxClass="
				+ exportDefaultXlsxClass + ", exportDefaultPdfClass=" + exportDefaultPdfClass
				+ ", exportDefaultXmlClass=" + exportDefaultXmlClass + ", exportDefaultCsvClass="
				+ exportDefaultCsvClass + ", exportXlsClass=" + exportXlsClass + ", exportXlsxClass=" + exportXlsxClass
				+ ", exportPdfClass=" + exportPdfClass + ", exportXmlClass=" + exportXmlClass + ", exportCsvClass="
				+ exportCsvClass + ", mainExtensionPackage=" + mainExtensionPackage + ", mainExtensionNames="
				+ mainExtensionNames + ", mainUrlBase=" + mainUrlBase + ", mainCdn=" + mainCdn + ", mainCdnJs="
				+ mainCdnJs + ", mainCdnCss=" + mainCdnCss + ", messages=" + messages + ", internalMessageResolver="
				+ internalMessageResolver + ", internalObjectType=" + internalObjectType + ", internalExtensions="
				+ internalExtensions + ", tableId=" + tableId + ", request=" + request + "]";
	}
}