package com.github.dandelion.datatables.thymeleaf.processor.el;

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
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.aggregator.ResourceAggregator;
import com.github.dandelion.datatables.core.asset.CssResource;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.WebResources;
import com.github.dandelion.datatables.core.cache.AssetCache;
import com.github.dandelion.datatables.core.compressor.ResourceCompressor;
import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.exception.CompressionException;
import com.github.dandelion.datatables.core.exception.ConfigurationLoadingException;
import com.github.dandelion.datatables.core.exception.DataNotFoundException;
import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.export.ExportDelegate;
import com.github.dandelion.datatables.core.export.ExportProperties;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.generator.WebResourceGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.DandelionUtils;
import com.github.dandelion.datatables.core.util.RequestHelper;
import com.github.dandelion.datatables.core.util.StringUtils;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractDatatablesElProcessor;
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
			Map<Configuration, Object> localConf = (Map<Configuration, Object>) request.getAttribute(DataTablesDialect.INTERNAL_TABLE_LOCAL_CONF);
			
			try {
				Configuration.applyConfiguration(htmlTable.getTableConfiguration(), localConf);
			} catch (ConfigurationLoadingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			applyCssConfiguration(arguments);
			
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

	private void applyCssConfiguration(Arguments arguments){
		
		// CSS class
		if(htmlTable.getTableConfiguration().getCssClass() != null){
			Node tableNode = (Node) ((IWebContext) arguments.getContext()).getHttpServletRequest().getAttribute(
					DataTablesDialect.INTERNAL_TABLE_NODE);
			
			String cssClass = ((Element) tableNode).getAttributeValue("class");
			if(StringUtils.isNotBlank(cssClass)){
				cssClass += " " + htmlTable.getTableConfiguration().getCssClass();
				((Element) tableNode).setAttribute("class", cssClass);
			}
			else{
				((Element) tableNode).setAttribute("class", htmlTable.getTableConfiguration().getCssClass().toString());
			}
		}
		
		// CSS style
		if(htmlTable.getTableConfiguration().getCssStyle() != null){
			Node tableNode = (Node) ((IWebContext) arguments.getContext()).getHttpServletRequest().getAttribute(
					DataTablesDialect.INTERNAL_TABLE_NODE);
			
			String cssStyle = ((Element) tableNode).getAttributeValue("style");
			if(StringUtils.isNotBlank(cssStyle)){
				cssStyle += ";" + htmlTable.getTableConfiguration().getCssStyle();
				((Element) tableNode).setAttribute("style", cssStyle);
			}
			else{
				((Element) tableNode).setAttribute("style", htmlTable.getTableConfiguration().getCssStyle().toString());
			}
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
		exportProperties.setExportConf(this.htmlTable.getTableConfiguration().getExportConf(currentExportType));
		exportProperties.setFileName(this.htmlTable.getTableConfiguration().getExportConf(currentExportType)
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
	private void setupHtmlGeneration(Arguments arguments, Element element, HttpServletRequest request) {
		WebResources webResources = null;
		
		this.htmlTable.getTableConfiguration().setExporting(false);

		try {

			// First we check if the DataTables configuration already exist in the cache
			String keyToTest = RequestHelper.getCurrentURIWithParameters(request) + "|" + htmlTable.getId();

			if(DandelionUtils.isDevModeEnabled() || !AssetCache.cache.containsKey(keyToTest)){
				logger.debug("No asset for the key {}. Generating...", keyToTest);
				
				// Init the web resources generator
				WebResourceGenerator contentGenerator = new WebResourceGenerator(htmlTable);
	
				// Generate the web resources (JS, CSS) and wrap them into a
				// WebResources POJO
				webResources = contentGenerator.generateWebResources();
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

			Element rootElement = arguments.getDocument().getFirstElementChild();
			Element head = DomUtils.findElement(rootElement, "head");
			Element body = DomUtils.findElement(rootElement, "body");
			
			// <link> HTML tag generation
			if (htmlTable.getTableConfiguration().getMainCdn() != null && htmlTable.getTableConfiguration().getMainCdn()) {
				DomUtils.insertLinkTag(htmlTable.getTableConfiguration().getMainCdnCss(), head);
			}
			for (Entry<String, CssResource> entry : webResources.getStylesheets().entrySet()) {
				String src = RequestHelper.getAssetSource(entry.getKey(), htmlTable, request, false);
				DomUtils.insertLinkTag(src, head);
			}

			// <script> HTML tag generation
			if (htmlTable.getTableConfiguration().getMainCdn() != null && htmlTable.getTableConfiguration().getMainCdn()) {
				DomUtils.insertScriptTag(htmlTable.getTableConfiguration().getMainCdnJs(), body);
			}
			for (Entry<String, JsResource> entry : webResources.getJavascripts().entrySet()) {
				String src = RequestHelper.getAssetSource(entry.getKey(), htmlTable, request, false);
				DomUtils.insertScriptTag(src, body);
			}
			String src = RequestHelper.getAssetSource(webResources.getMainJsFile().getName(), htmlTable, request, true);
			DomUtils.insertScriptTag(src, body);

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
		} catch (ExtensionLoadingException e) {
			logger.error("Something went wront with the extension loading.");
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
}