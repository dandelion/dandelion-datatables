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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.aggregator.AggregatorMode;
import com.github.dandelion.datatables.core.asset.ExtraConf;
import com.github.dandelion.datatables.core.asset.ExtraFile;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.compressor.CompressorMode;
import com.github.dandelion.datatables.core.exception.AttributeProcessingException;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.export.ExportProperties;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.feature.AbstractFeature;
import com.github.dandelion.datatables.core.feature.PaginationType;
import com.github.dandelion.datatables.core.html.HtmlTag;
import com.github.dandelion.datatables.core.plugin.AbstractPlugin;
import com.github.dandelion.datatables.core.theme.AbstractTheme;
import com.github.dandelion.datatables.core.theme.ThemeOption;

/**
 * POJO that contains all the table-specific properties.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class TableConfiguration {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TableConfiguration.class);

	public static final String DEFAULT_GROUP_NAME = "global";
	public static final String KEY_NAME_SEP = ".";

	public static TableConfiguration instance;

	private static Map<String, TableConfiguration> configurations = new HashMap<String, TableConfiguration>();

	// DataTables global parameters
	private Boolean featureInfo;
	private Boolean featureAutoWidth;
	private Boolean featureFilterable;
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

	// CSS parameters
	private StringBuilder cssStyle;
	private StringBuilder cssClass;
	private String cssStripeClasses;

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
	private AbstractTheme extraTheme;
	private ThemeOption extraThemeOption;
	private Set<String> extraCustomFeatures;
	private Set<String> extraCustomPlugins;
	private List<ExtraFile> extraFiles;
	private List<ExtraConf> extraConfs;
	private List<Callback> extraCallbacks;
	private Boolean extraCdn;
	private String extraAppear;
	private String extraAppearDuration;
	private String extraLabels;

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
	private String mainBasePackage;
	private Boolean mainCompressorEnable;
	private CompressorMode mainCompressorMode;
	private String mainCompressorClass;
	private Boolean mainCompressorMunge;
	private Boolean mainCompressorPreserveSemiColons;
	private Boolean mainCompressorDisableOpti;
	private Boolean mainAggregatorEnable;
	private AggregatorMode mainAggregatorMode;
	private String mainUrlBase;
			
	// Class of the iterated objects. Only used in XML export.
	private String internalObjectType;
	private Set<AbstractPlugin> internalPlugins;
	private Set<AbstractFeature> internalFeatures;
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
		return getInstance(request, DEFAULT_GROUP_NAME);
	}

	/**
	 * <p>
	 * Return an instance of {@link TableConfiguration} for the given groupName.
	 * The instance is retrieved from the cache (configurations) if it exists or
	 * computed and then stored in it.
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

		String group = groupName;

		if (groupName == null || groupName.length() == 0) {
			group = DEFAULT_GROUP_NAME;
		}

		logger.debug("Trying to retrieve the TableConfiguration for the group {}", group);
		
		TableConfiguration tableConfiguration = configurations.get(group);

		if (tableConfiguration == null) {
			logger.debug("No TableConfiguration was found. Creating one...");
			TableConfiguration tableConf = new TableConfiguration(request, group);
			configurations.put(group, tableConf);
			tableConfiguration = tableConf;
		}

		return new TableConfiguration(tableConfiguration, request);
	}
	
	/**
	 * Private constructor used by the getInstance() methods when the instance
	 * does not exist in the cache.
	 * 
	 * @param request
	 * 
	 * @param groupName
	 *            Name of the configuration group to load.
	 * @throws BadConfigurationException 
	 */
	private TableConfiguration(HttpServletRequest request, String groupName) {
		this.request = request;
		
		logger.debug("Getting the ConfigurationLoader...");
		AbstractConfigurationLoader confLoader = new DatatablesConfigurator().getConfLoader();
		logger.debug("The configurationLoader '{}' will be used.", confLoader.getClass().getSimpleName());
		
		try {
			// Loading default configuration (from properties file)
			confLoader.loadDefaultConfiguration();
			
			// Loading specific configuration (using the specificConfLoader)
			confLoader.loadSpecificConfiguration(groupName);

			Configuration.applyConfiguration(this, confLoader.getStagingConfiguration());
			
		} catch (BadConfigurationException e) {
			logger.warn("Configuration could not be loaded.", e);
		} catch (AttributeProcessingException e) {
			logger.error("Something went wrong during the configuration processing", e);
		}
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

		// CSS parameters
		cssStyle = objectToClone.cssStyle;
		cssClass = objectToClone.cssClass;
		cssStripeClasses = objectToClone.cssStripeClasses;
		extraLabels = objectToClone.extraLabels;

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
		extraTheme = objectToClone.extraTheme;
		extraThemeOption = objectToClone.extraThemeOption;
		extraCustomFeatures = objectToClone.extraCustomFeatures;
		extraCustomPlugins = objectToClone.extraCustomPlugins;
		extraFiles = objectToClone.extraFiles;
		extraConfs = objectToClone.extraConfs;
		extraCallbacks = objectToClone.extraCallbacks;
		extraCdn = objectToClone.extraCdn;
		extraAppear = objectToClone.extraAppear;
		extraAppearDuration = objectToClone.extraAppearDuration;

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
		mainBasePackage = objectToClone.mainBasePackage;
		mainCompressorEnable = objectToClone.mainCompressorEnable;
		mainCompressorMode = objectToClone.mainCompressorMode;
		mainCompressorClass = objectToClone.mainCompressorClass;
		mainCompressorMunge = objectToClone.mainCompressorMunge;
		mainCompressorPreserveSemiColons = objectToClone.mainCompressorPreserveSemiColons;
		mainCompressorDisableOpti = objectToClone.mainCompressorDisableOpti;
		mainAggregatorEnable = objectToClone.mainAggregatorEnable;
		mainAggregatorMode = objectToClone.mainAggregatorMode;
		mainUrlBase = objectToClone.mainUrlBase;
	}
	
	/**
	 * Check if the parameter exists as a property being part of the
	 * configuration properties file.
	 * 
	 * @param property
	 *            The property to check.
	 * @return true if it exists, false otherwise.
	 * @throws BadConfigurationException
	 */
	public Boolean isValidProperty(String property) throws BadConfigurationException {

		// TODO Refactorer en utilisant en scrutant la classe Conf.class
//		Field[] confConstants = ConfConstants.class.getDeclaredFields();
//
//		try {
//			for (Field constant : confConstants) {
//				if (property.equals(constant.get(ConfConstants.class))) {
//					return true;
//				}
//			}
//		} catch (IllegalArgumentException e) {
//			throw new BadConfigurationException("Unable to access the ConfConstants class", e);
//		} catch (IllegalAccessException e) {
//			throw new BadConfigurationException("Unable to access the ConfConstants class", e);
//		}

		return false;
	}


	/**
	 * <p>
	 * According to the YUI JavaScriptCompressor class, micro optimizations
	 * concern :
	 * 
	 * <ul>
	 * <li>object member access : Transforms obj["foo"] into obj.foo whenever
	 * possible, saving 3 bytes</li>
	 * <li>object litteral member declaration : Transforms 'foo': ... into foo:
	 * ... whenever possible, saving 2 bytes</li>
	 * </ul>
	 * 
	 * @return true to disable micro optimizations, false otherwise.
	 */
	public Boolean getCompressorDisableOpti() {
		return mainCompressorDisableOpti;
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

	/**
	 * @return the default base package where to scan for custom extension.
	 */
	public String getBasePackage() {
		return mainBasePackage;
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

	public String getExtraLabels() {
		return extraLabels;
	}

	public TableConfiguration setExtraLabels(String labels) {
		this.extraLabels = labels;
		return this;
	}

	public Boolean getExtraCdn() {
		return extraCdn;
	}

	public TableConfiguration setExtraCdn(Boolean cdn) {
		this.extraCdn = cdn;
		return this;
	}

	public Boolean getFeatureJqueryUI() {
		return featureJqueryUi;
	}

	public TableConfiguration setFeatureJqueryUi(Boolean jqueryUI) {
		this.featureJqueryUi = jqueryUI;
		return this;
	}

	public String getExtraAppear() {
		return extraAppear;
	}

	public TableConfiguration setExtraAppear(String appear) {
		this.extraAppear = appear;
		return this;
	}

	public String getExtraAppearDuration() {
		return extraAppearDuration;
	}

	public TableConfiguration setExtraAppearDuration(String appearDuration) {
		this.extraAppearDuration = appearDuration;
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
	 * Register a plugin to the table.
	 * 
	 * @param plugin
	 *            The plugin to activate.
	 */
	public TableConfiguration registerPlugin(AbstractPlugin plugin) {
		if (this.internalPlugins == null) {
			this.internalPlugins = new HashSet<AbstractPlugin>();
		}
		this.internalPlugins.add(plugin);
		return this;
	}

	/**
	 * Register a feature to the table.
	 * 
	 * @param feature
	 *            The feature to activate.
	 */
	public TableConfiguration registerFeature(AbstractFeature feature) {
		if (this.internalFeatures == null) {
			this.internalFeatures = new HashSet<AbstractFeature>();
		}
		this.internalFeatures.add(feature);
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

	public Set<AbstractPlugin> getExtraPlugins() {
		return internalPlugins;
	}

	public TableConfiguration setExtraPlugins(Set<AbstractPlugin> plugins) {
		this.internalPlugins = plugins;
		return this;
	}

	public Set<AbstractFeature> getExtraFeatures() {
		return internalFeatures;
	}

	public TableConfiguration setExtraFeatures(Set<AbstractFeature> features) {
		this.internalFeatures = features;
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

	public AbstractTheme getExtraTheme() {
		return extraTheme;
	}

	public TableConfiguration setExtraTheme(AbstractTheme theme) {
		this.extraTheme = theme;
		return this;
	}

	public ThemeOption getExtraThemeOption() {
		return extraThemeOption;
	}

	public TableConfiguration setExtraThemeOption(ThemeOption themeOption) {
		this.extraThemeOption = themeOption;
		return this;
	}

	public Set<String> getExtraCustomFeatures() {
		return extraCustomFeatures;
	}

	public TableConfiguration setExtraCustomFeatures(Set<String> customFeatures) {
		this.extraCustomFeatures = customFeatures;
		return this;
	}

	public Set<String> getExtraCustomPlugins() {
		return extraCustomPlugins;
	}

	public TableConfiguration setExtraCustomPlugins(Set<String> customPlugins) {
		this.extraCustomPlugins = customPlugins;
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
	
	public String getMainBasePackage() {
		return mainBasePackage;
	}

	public TableConfiguration setMainBasePackage(String mainBasePackage) {
		this.mainBasePackage = mainBasePackage;
		return this;
	}

	public Boolean getMainCompressorEnable() {
		return mainCompressorEnable;
	}

	public TableConfiguration setMainCompressorEnable(Boolean mainCompressorEnable) {
		this.mainCompressorEnable = mainCompressorEnable;
		return this;
	}

	public CompressorMode getMainCompressorMode() {
		return mainCompressorMode;
	}

	public TableConfiguration setMainCompressorMode(CompressorMode mainCompressorMode) {
		this.mainCompressorMode = mainCompressorMode;
		return this;
	}

	public String getMainCompressorClass() {
		return mainCompressorClass;
	}

	public TableConfiguration setMainCompressorClass(String mainCompressorClass) {
		this.mainCompressorClass = mainCompressorClass;
		return this;
	}

	public Boolean getMainCompressorMunge() {
		return mainCompressorMunge;
	}

	public TableConfiguration setMainCompressorMunge(Boolean mainCompressorMunge) {
		this.mainCompressorMunge = mainCompressorMunge;
		return this;
	}

	public Boolean getMainCompressorPreserveSemiColons() {
		return mainCompressorPreserveSemiColons;
	}

	public TableConfiguration setMainCompressorPreserveSemiColons(Boolean mainCompressorPreserveSemiColons) {
		this.mainCompressorPreserveSemiColons = mainCompressorPreserveSemiColons;
		return this;
	}

	public Boolean getMainCompressorDisableOpti() {
		return mainCompressorDisableOpti;
	}

	public TableConfiguration setMainCompressorDisableOpti(Boolean mainCompressorDisableOpti) {
		this.mainCompressorDisableOpti = mainCompressorDisableOpti;
		return this;
	}

	public Boolean getMainAggregatorEnable() {
		return mainAggregatorEnable;
	}

	public TableConfiguration setMainAggregatorEnable(Boolean mainAggregatorEnable) {
		this.mainAggregatorEnable = mainAggregatorEnable;
		return this;
	}

	public AggregatorMode getMainAggregatorMode() {
		return mainAggregatorMode;
	}

	public TableConfiguration setMainAggregatorMode(AggregatorMode mainAggregatorMode) {
		this.mainAggregatorMode = mainAggregatorMode;
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
	
	public static Map<String, TableConfiguration> getConfigurations(){
		return configurations;
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

	@Override
	public String toString() {
		return "TableConfiguration [featureInfo=" + featureInfo + ", featureAutoWidth=" + featureAutoWidth
				+ ", featureFilterable=" + featureFilterable + ", featurePaginate=" + featurePaginate
				+ ", featurePaginationType=" + featurePaginationType + ", featureLengthChange=" + featureLengthChange
				+ ", featureSort=" + featureSort + ", featureStateSave=" + featureStateSave + ", featureJqueryUi="
				+ featureJqueryUi + ", featureLengthMenu=" + featureLengthMenu + ", featureDisplayLength="
				+ featureDisplayLength + ", featureDom=" + featureDom + ", featureScrolly=" + featureScrolly
				+ ", featureScrollCollapse=" + featureScrollCollapse + ", cssStyle=" + cssStyle + ", cssClass="
				+ cssClass + ", cssStripeClasses=" + cssStripeClasses + ", ajaxProcessing=" + ajaxProcessing
				+ ", ajaxDeferRender=" + ajaxDeferRender + ", ajaxServerSide=" + ajaxServerSide + ", ajaxSource="
				+ ajaxSource + ", ajaxPipelining=" + ajaxPipelining + ", ajaxPipeSize=" + ajaxPipeSize
				+ ", ajaxServerData=" + ajaxServerData + ", ajaxServerParam=" + ajaxServerParam + ", ajaxServerMethod="
				+ ajaxServerMethod + ", pluginFixedPosition=" + pluginFixedPosition + ", pluginFixedOffsetTop="
				+ pluginFixedOffsetTop + ", pluginFixedHeader=" + pluginFixedHeader + ", pluginScroller="
				+ pluginScroller + ", pluginColReorder=" + pluginColReorder + ", extraTheme=" + extraTheme
				+ ", extraThemeOption=" + extraThemeOption + ", extraCustomFeatures=" + extraCustomFeatures
				+ ", extraCustomPlugins=" + extraCustomPlugins + ", extraFiles=" + extraFiles + ", extraConfs="
				+ extraConfs + ", extraCallbacks=" + extraCallbacks + ", extraCdn=" + extraCdn + ", extraAppear="
				+ extraAppear + ", extraAppearDuration=" + extraAppearDuration + ", extraLabels=" + extraLabels
				+ ", exportProperties=" + exportProperties + ", exporting=" + exporting + ", exportConfs="
				+ exportConfs + ", exportLinkPositions=" + exportLinkPositions + ", isExportable=" + isExportable
				+ ", exportDefaultXlsClass=" + exportDefaultXlsClass + ", exportDefaultXlsxClass="
				+ exportDefaultXlsxClass + ", exportDefaultPdfClass=" + exportDefaultPdfClass
				+ ", exportDefaultXmlClass=" + exportDefaultXmlClass + ", exportDefaultCsvClass="
				+ exportDefaultCsvClass + ", exportXlsClass=" + exportXlsClass + ", exportXlsxClass=" + exportXlsxClass
				+ ", exportPdfClass=" + exportPdfClass + ", exportXmlClass=" + exportXmlClass + ", exportCsvClass="
				+ exportCsvClass + ", mainBasePackage=" + mainBasePackage + ", mainCompressorEnable="
				+ mainCompressorEnable + ", mainCompressorMode=" + mainCompressorMode + ", mainCompressorClass="
				+ mainCompressorClass + ", mainCompressorMunge=" + mainCompressorMunge
				+ ", mainCompressorPreserveSemiColons=" + mainCompressorPreserveSemiColons
				+ ", mainCompressorDisableOpti=" + mainCompressorDisableOpti + ", mainAggregatorEnable="
				+ mainAggregatorEnable + ", mainAggregatorMode=" + mainAggregatorMode + ", mainUrlBase=" + mainUrlBase
				+ ", internalObjectType=" + internalObjectType + ", internalPlugins=" + internalPlugins
				+ ", internalFeatures=" + internalFeatures + ", tableId=" + tableId + ", request=" + request + "]";
	}
}