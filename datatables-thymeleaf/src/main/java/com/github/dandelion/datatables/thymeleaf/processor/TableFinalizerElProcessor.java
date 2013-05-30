package com.github.dandelion.datatables.thymeleaf.processor;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.aggregator.ResourceAggregator;
import com.github.dandelion.datatables.core.asset.CssResource;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.WebResources;
import com.github.dandelion.datatables.core.cache.AssetCache;
import com.github.dandelion.datatables.core.compressor.ResourceCompressor;
import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.constants.CdnConstants;
import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.exception.CompressionException;
import com.github.dandelion.datatables.core.exception.DataNotFoundException;
import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.export.ExportDelegate;
import com.github.dandelion.datatables.core.export.ExportProperties;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.feature.FilteringFeature;
import com.github.dandelion.datatables.core.generator.WebResourceGenerator;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.DandelionUtils;
import com.github.dandelion.datatables.core.util.RequestHelper;
import com.github.dandelion.datatables.thymeleaf.dialect.AbstractDatatablesElProcessor;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.util.DomUtils;

/**
 * <p>
 * Element processor applied to the internal HTML <code>div</code> tag.
 * <p>
 * The <code>div</code> is added by the TableInitializerProcessor after the
 * <code>table</code> in order to be processed after all the "table" processors.
 * 
 * @author Thibault Duchateau
 */
public class TableFinalizerElProcessor extends AbstractDatatablesElProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TableFinalizerElProcessor.class);

	private HtmlTable htmlTable;

	public TableFinalizerElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 50000;
	}

	@Override
	protected ProcessorResult doProcessElement(Arguments arguments, Element element, HtmlTable table) {

		// Get the HTTP request
		HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();

        this.htmlTable = table;

		if (this.htmlTable != null) {

			@SuppressWarnings("unchecked")
			Map<Configuration, Object> localConf = (Map<Configuration, Object>) request.getAttribute(DataTablesDialect.INTERNAL_LOCAL_CONF);
			Configuration.applyConfiguration(htmlTable.getTableConfiguration(), localConf);
			
			// The table is being exported
			if (RequestHelper.isTableBeingExported(request, this.htmlTable)) {
				setupExport(arguments);
			}
			// The table must be generated and displayed
			else {
				setupHtmlGeneration(arguments, element, request);
			}
		}

		// The "finalizing div" can now be removed
		element.getParent().removeChild(element);

		return ProcessorResult.OK;
	}

	private void registerFeatures(Element element, Arguments arguments, HtmlTable htmlTable) {
		
		if (htmlTable.hasOneFilterableColumn()) {
			logger.info("Feature detected : select with filter");

			// Duplicate header row in the footer
			generateFooter(element, arguments);

			htmlTable.getTableConfiguration().registerFeature(new FilteringFeature());
		}
	}

	/**
	 * Set up the export properties, before the filter intercepts the response.
	 */
	private void setupExport(Arguments arguments) {
		logger.debug("Setting export up ...");

		HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();
		HttpServletResponse response = ((IWebContext) arguments.getContext())
				.getHttpServletResponse();

		// Init the export properties
		ExportProperties exportProperties = new ExportProperties();

		ExportType currentExportType = getCurrentExportType(request);

		exportProperties.setCurrentExportType(currentExportType);
		exportProperties.setExportConf(this.htmlTable.getTableConfiguration().getExportConfMap().get(currentExportType));
		exportProperties.setFileName(this.htmlTable.getTableConfiguration().getExportConfMap().get(currentExportType)
				.getFileName());

		this.htmlTable.getTableConfiguration().setExportProperties(exportProperties);
		this.htmlTable.getTableConfiguration().setExporting(true);

		try {
			// Call the export delegate
			ExportDelegate exportDelegate = new ExportDelegate(this.htmlTable, exportProperties,
					request);
			exportDelegate.launchExport();

		} catch (ExportException e) {
			logger.error("Something went wront with the Dandelion-datatables export configuration.");
			e.printStackTrace();
		} catch (BadConfigurationException e) {
			logger.error("Something went wront with the Dandelion-datatables configuration.");
			e.printStackTrace();
		}

		response.reset();
	}

	/**
	 * Set up the HTML table generation.
	 */
	private void setupHtmlGeneration(Arguments arguments, Element element,
			HttpServletRequest request) {
		WebResources webResources = null;
		
		this.htmlTable.getTableConfiguration().setExporting(false);

		// Plugins and themes are activated in their respective attribute
		// processor

		// Register all activated features
		registerFeatures(element, arguments, this.htmlTable);

		try {

			// First we check if the DataTables configuration already exist in the cache
			String keyToTest = RequestHelper.getCurrentURIWithParameters(request) + "|" + htmlTable.getId();

			if(DandelionUtils.isDevModeEnabled() || !AssetCache.cache.containsKey(keyToTest)){
				logger.debug("No asset for the key {}. Generating...", keyToTest);
				
				// Init the web resources generator
				WebResourceGenerator contentGenerator = new WebResourceGenerator();
	
				// Generate the web resources (JS, CSS) and wrap them into a
				// WebResources POJO
				webResources = contentGenerator.generateWebResources(htmlTable);
				logger.debug("Web content generated successfully");
				
				AssetCache.cache.put(keyToTest, webResources);
				logger.debug("Cache updated with new web resources");
			}
			else{
				logger.debug("Asset(s) already exist, retrieving content from cache...");

				webResources = (WebResources) AssetCache.cache.get(keyToTest);
			}
						
			// Aggregation
			if (htmlTable.getTableConfiguration().getMainAggregatorEnable()) {
				logger.debug("Aggregation enabled");
				ResourceAggregator.processAggregation(webResources, htmlTable);
			}

			// Compression
			if (htmlTable.getTableConfiguration().getMainCompressorEnable()) {
				logger.debug("Compression enabled");
				ResourceCompressor.processCompression(webResources, htmlTable);
			}

			// <link> HTML tag generation
			if (htmlTable.getTableConfiguration().getExtraCdn() != null && htmlTable.getTableConfiguration().getExtraCdn()) {
				DomUtils.addLinkTag(DomUtils.getParentAsElement(element), request,
						CdnConstants.CDN_DATATABLES_CSS);
			}
			for (Entry<String, CssResource> entry : webResources.getStylesheets().entrySet()) {
				String src = RequestHelper.getAssetSource(entry.getKey(), htmlTable, request, false);
				DomUtils.addLinkTag(element, request, src);
			}

			// <script> HTML tag generation
			if (htmlTable.getTableConfiguration().getExtraCdn() != null && htmlTable.getTableConfiguration().getExtraCdn()) {
				DomUtils.addScriptTag(DomUtils.getParentAsElement(element), request,
						CdnConstants.CDN_DATATABLES_JS_MIN);
			}
			for (Entry<String, JsResource> entry : webResources.getJavascripts().entrySet()) {
				String src = RequestHelper.getAssetSource(entry.getKey(), htmlTable, request, false);
				DomUtils.addScriptTag(DomUtils.getParentAsElement(element), request, src);
			}
			String src = RequestHelper.getAssetSource(webResources.getMainJsFile().getName(), htmlTable, request, true);
			DomUtils.addScriptTag(DomUtils.getParentAsElement(element), request, src);

			logger.debug("Web content generated successfully");
		} catch (CompressionException e) {
			logger.error("Something went wront with the compressor.");
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (BadConfigurationException e) {
			logger.error("Something went wront with the Dandelion-datatables configuration.");
			throw new RuntimeException(e);
		} catch (DataNotFoundException e) {
			logger.error("Something went wront with the data provider.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Return the current export type asked by the user on export link click.
	 * 
	 * @return An enum corresponding to the type of export.
	 */
	private ExportType getCurrentExportType(HttpServletRequest request) {

		// Get the URL parameter used to identify the export type
		String exportTypeString = request.getParameter(
                ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE);

		// Convert it to the corresponding enum

        return ExportType.findByUrlParameter(Integer.parseInt(exportTypeString));
	}

    /**
     * TODO finalize
     * @param element .
     * @param arguments .
     */
	private void generateFooter(Element element, Arguments arguments) {
		Element tfoot = new Element("tfoot");

		Node tableNode = (Node)((IWebContext) arguments.getContext()).getHttpServletRequest().getAttribute("tableNode");
		
		for(HtmlColumn column : htmlTable.getLastHeaderRow().getColumns()){
			Element th = new Element("th");
			th.addChild(new Text(column.getContent().toString()));
			tfoot.addChild(th);
		}

		((Element) tableNode).addChild(tfoot);
	}
}