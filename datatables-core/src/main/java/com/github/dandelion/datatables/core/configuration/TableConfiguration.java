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
import org.apache.commons.lang.UnhandledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.aggregator.AggregatorMode;
import com.github.dandelion.datatables.core.asset.ExtraConf;
import com.github.dandelion.datatables.core.asset.ExtraFile;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.compressor.CompressorMode;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.export.ExportProperties;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.feature.AbstractFeature;
import com.github.dandelion.datatables.core.feature.PaginationType;
import com.github.dandelion.datatables.core.plugin.AbstractPlugin;
import com.github.dandelion.datatables.core.theme.AbstractTheme;
import com.github.dandelion.datatables.core.theme.ThemeOption;

/**
 * POJO that contains all the table-specific properties.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class TableConfiguration implements Cloneable {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TableConfiguration.class);

	public static final String DEFAULT_GROUP_NAME = "global";
	public static final String KEY_NAME_SEP = ".";

	/**
	 * Unique instance of TableProperties that will be cloned for each table.
	 */
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
	private String cssStyle;
	private String cssClass;
	private String cssStripeClasses;
	private String labels;

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
	private List<String> extraCustomFeatures;
	private List<String> extraCustomPlugins;
	private List<ExtraFile> extraFiles;
	private List<ExtraConf> extraConfs;
	private List<Callback> extraCallbacks;
	private Boolean extraCdn;
	private String extraAppear;
	private String extraAppearDuration;

	// Export parameters
	private ExportProperties exportProperties;
	private Boolean exporting;
	private Map<ExportType, ExportConf> exportConfMap = new HashMap<ExportType, ExportConf>();
	private List<ExportLinkPosition> exportLinkPositions;
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
		
		return (TableConfiguration) tableConfiguration.clone();
	}
	
	/**
	 * Private constructor used by the getInstance() methods when the instance
	 * does not exist in the cache.
	 * 
	 * @param request
	 * 
	 * @param groupName
	 *            Name of the configuration group to load.
	 */
	private TableConfiguration(HttpServletRequest request, String groupName) {
		this.request = request;
		
		logger.debug("Getting the ConfigurationLoader...");
		AbstractConfigurationLoader confLoader = DatatablesConfigurator.getInstance().getConfLoader();
		logger.debug("The configurationLoader '{}' will be used.", confLoader.getClass().getSimpleName());
		
		try {
			// Loading default configuration (from properties file)
			confLoader.loadDefaultConfiguration();
			
			// Loading specific configuration (using the specificConfLoader)
			confLoader.loadSpecificConfiguration(groupName);

			Configuration.applyConfiguration(this, confLoader.getStagingConfiguration());
			
		} catch (BadConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Clone the {@link TableConfiguration} instance to use it in a table.
	 */
	public Object clone() {
		TableConfiguration copy;
		try {
			copy = (TableConfiguration) super.clone();
		} catch (CloneNotSupportedException e) {
			// should never happen
			throw new UnhandledException(e);
		}
		return copy;
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

	public String getLabels() {
		return labels;
	}

	public TableConfiguration setLabels(String labels) {
		this.labels = labels;
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

	public String getFeatureAppear() {
		return extraAppear;
	}

	public TableConfiguration setExtraAppear(String appear) {
		this.extraAppear = appear;
		return this;
	}

	public String getFeatureAppearDuration() {
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

	public List<String> getExtraCustomFeatures() {
		return extraCustomFeatures;
	}

	public TableConfiguration setExtraCustomFeatures(List<String> customFeatures) {
		this.extraCustomFeatures = customFeatures;
		return this;
	}

	public List<String> getExtraCustomPlugins() {
		return extraCustomPlugins;
	}

	public TableConfiguration setExtraCustomPlugins(List<String> customPlugins) {
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

	public Map<ExportType, ExportConf> getExportConfMap() {
		return exportConfMap;
	}

	public void configureExport(ExportType exportType, ExportConf exportConf) {
		this.exportConfMap.put(exportType, exportConf);
	}

	public void setExportConfMap(Map<ExportType, ExportConf> exportConfs) {
		this.exportConfMap = exportConfs;
	}

	public Boolean isExportable() {
		return isExportable;
	}

	public void setIsExportable(Boolean isExportable) {
		this.isExportable = isExportable;
	}

	public List<ExportLinkPosition> getExportLinkPositions() {
		return exportLinkPositions;
	}

	public TableConfiguration setExportLinkPositions(List<ExportLinkPosition> exportLinkPositions) {
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

	public String getCssStyle() {
		return cssStyle;
	}

	public TableConfiguration setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
		return this;
	}

	public String getCssClass() {
		return cssClass;
	}

	public TableConfiguration setCssClass(String cssClass) {
		this.cssClass = cssClass;
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
	
	public Map<String, TableConfiguration> getConfigurations(){
		return configurations;
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
				+ cssClass + ", cssStripeClasses=" + cssStripeClasses + ", labels=" + labels + ", ajaxProcessing="
				+ ajaxProcessing + ", ajaxDeferRender=" + ajaxDeferRender + ", ajaxServerSide=" + ajaxServerSide
				+ ", ajaxSource=" + ajaxSource + ", ajaxPipelining=" + ajaxPipelining + ", ajaxPipeSize="
				+ ajaxPipeSize + ", ajaxServerData=" + ajaxServerData + ", ajaxServerParam=" + ajaxServerParam
				+ ", ajaxServerMethod=" + ajaxServerMethod + ", pluginFixedPosition=" + pluginFixedPosition
				+ ", pluginFixedOffsetTop=" + pluginFixedOffsetTop + ", pluginFixedHeader=" + pluginFixedHeader
				+ ", pluginScroller=" + pluginScroller + ", pluginColReorder=" + pluginColReorder + ", extraTheme="
				+ extraTheme + ", extraThemeOption=" + extraThemeOption + ", extraCustomFeatures="
				+ extraCustomFeatures + ", extraCustomPlugins=" + extraCustomPlugins + ", extraFiles=" + extraFiles
				+ ", extraConfs=" + extraConfs + ", extraCallbacks=" + extraCallbacks + ", extraCdn=" + extraCdn
				+ ", extraAppear=" + extraAppear + ", extraAppearDuration=" + extraAppearDuration
				+ ", exportProperties=" + exportProperties + ", exporting=" + exporting + ", exportConfMap="
				+ exportConfMap + ", exportLinkPositions=" + exportLinkPositions + ", isExportable=" + isExportable
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