package com.github.dandelion.datatables.thymeleaf.processor.el;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.RequestHelper;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractDatatablesElProcessor;
import com.github.dandelion.datatables.thymeleaf.util.Utils;

/**
 * <p>
 * Element processor applied to the HTML <tt>table</tt> tag.
 * 
 * @author Thibault Duchateau
 */
public class TableInitializerElProcessor extends AbstractDatatablesElProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TableInitializerElProcessor.class);

	public TableInitializerElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return DataTablesDialect.DT_HIGHEST_PRECEDENCE;
	}

	@Override
	protected ProcessorResult doProcessElement(Arguments arguments, Element element, HtmlTable table) {
		String tableId = element.getAttributeValue("id");
		logger.debug("{} element found with id {}", element.getNormalizedName(), tableId);

		HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();

		if (tableId == null) {
			logger.error("The 'id' attribute is required.");
			throw new IllegalArgumentException();
		} else {
			
			String confGroup = (String) request.getAttribute(DataTablesDialect.INTERNAL_CONF_GROUP);
			
			HtmlTable htmlTable = new HtmlTable(tableId, request, confGroup);

			// Add default footer and header row
			htmlTable.addHeaderRow();

			// Add a "finalizing div" after the HTML table tag in order to
			// finalize the Dandelion-datatables configuration generation
			// The div will be removed in its corresponding processor
			Element div = new Element("div");
			div.setAttribute(DataTablesDialect.DIALECT_PREFIX + ":tmp", "internalUse");
			div.setRecomputeProcessorsImmediately(true);
			element.getParent().insertAfter(element, div);

			// Store the htmlTable POJO as a request attribute, so that all the
			// others following HTML tags can access it and particularly the
			// "finalizing div"
			request.setAttribute(DataTablesDialect.INTERNAL_TABLE_BEAN, htmlTable);

			// The table node is also saved in the request, to be easily accessed later
			request.setAttribute(DataTablesDialect.INTERNAL_TABLE_NODE, element);
			
			// Map used to store the table local configuration
			request.setAttribute(DataTablesDialect.INTERNAL_TABLE_LOCAL_CONF, new HashMap<Configuration, Object>());
			
			// TODO this has to be moved
			// Export has been enabled
			if(element.hasAttribute("dt:export")){

				Map<ExportType, ExportConf> exportConfMap = new HashMap<ExportType, ExportConf>();
				ExportType type = null;
				String val = Utils.parseElementAttribute(arguments, element.getAttributeValue("dt:export"), null, String.class);
				
				String[] types = val.trim().toUpperCase().split(",");
				for (String exportTypeString : types) {

					try {
						type = ExportType.valueOf(exportTypeString);
					} catch (IllegalArgumentException e) {
						throw new ConfigurationProcessingException("Invalid value", e);
					}

					ExportConf exportConf = new ExportConf(type);
					String exportUrl = RequestHelper.getCurrentURIWithParameters(request);
					if(exportUrl.contains("?")){
						exportUrl += "&";
					}
					else{
						exportUrl += "?";
					}
					exportUrl += ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE + "="
						+ type.getUrlParameter() + "&"
						+ ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID + "="
						+ tableId;
						
					exportConf.setUrl(exportUrl);
					exportConf.setCustom(false);
					exportConfMap.put(type, exportConf);
				}
				
				request.setAttribute(DataTablesDialect.INTERNAL_EXPORT_CONF_MAP, exportConfMap);
			}

			// Cleaning attribute
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":table");
						
			return ProcessorResult.OK;
		}
	}
}