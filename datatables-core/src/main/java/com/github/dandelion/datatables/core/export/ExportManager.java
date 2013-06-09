package com.github.dandelion.datatables.core.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.html.HtmlDiv;
import com.github.dandelion.datatables.core.html.HtmlHyperlink;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * 
 *
 * @author Thibault Duchateau
 */
public class ExportManager {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ExportManager.class);
		
	/**
	 * <p>
	 * If the export attribute of the table tag has been set to true, this
	 * method will convert every ExportConf bean to a HTML link, corresponding
	 * to each activated export type.
	 * 
	 * <p>
	 * All the link are wrapped into a div which is inserted in the DOM using
	 * jQuery. The wrapping div can be inserted at multiple position, depending
	 * on the tag configuration.
	 * 
	 * <p>
	 * If the user didn't add any ExportTag, Dandelion-datatables will use the default
	 * configuration.
	 * 
	 * @param table
	 *            The HTML table where to get ExportConf beans.
	 * @param mainJsFile
	 *            The web resource to update
	 */
	public void exportManagement(HtmlTable table, JsResource mainJsFile) {

		StringBuilder links = new StringBuilder();
		for (ExportLinkPosition position : table.getTableConfiguration().getExportLinkPositions()) {

			// Init the wrapping HTML div
			HtmlDiv divExport = initExportDiv(table, mainJsFile);
			
			switch (position) {
			case BOTTOM_LEFT:
				divExport.addCssStyle("float:left;margin-right:10px;");
				links.append("$('#" + table.getId() + "').after('" + divExport.toHtml() + "');");
				links.append("$('#" + table.getId() + "_info').css('clear', 'none');");
				break;

			case BOTTOM_MIDDLE:
				divExport.addCssStyle("float:left;margin-left:10px;");
				links.append("$('#" + table.getId() + "_wrapper').append('" + divExport.toHtml() + "');");
				break;

			case BOTTOM_RIGHT:
				divExport.addCssStyle("float:right;");
				links.append("$('#" + table.getId()	+ "').after('" + divExport.toHtml() + "');");
				links.append("$('#" + table.getId() + "_info').css('clear', 'none');");
				break;

			case TOP_LEFT:
				divExport.addCssStyle("float:left;margin-right:10px;");
				links.append("$('#" + table.getId() + "_wrapper').prepend('" + divExport.toHtml() + "');");
				break;

			case TOP_MIDDLE:
				divExport.addCssStyle("float:left;margin-left:10px;");
				links.append("$('#" + table.getId()	+ "').before('" + divExport.toHtml() + "');");
				break;

			case TOP_RIGHT:
				divExport.addCssStyle("float:right;");
				links.append("$('#" + table.getId()	+ "_wrapper').prepend('" + divExport.toHtml() + "');");
				break;

			default:
				break;
			}
		}

		if(table.getTableConfiguration().hasCallback(CallbackType.INIT)){
			table.getTableConfiguration().getCallback(CallbackType.INIT).addContent(links.toString());
		}
		else{
			Callback initCallback = new Callback(CallbackType.INIT, links.toString());
			table.getTableConfiguration().registerCallback(initCallback);
		}
	}
	
	private HtmlDiv initExportDiv(HtmlTable table, JsResource mainJsfile){
		
		// Init the wrapping HTML div
		HtmlDiv divExport = new HtmlDiv();
		divExport.addCssClass("dandelion_dataTables_export");
		
		// ExportTag have been added to the TableTag
		if (table.getTableConfiguration().getExportConfs() != null && table.getTableConfiguration().getExportConfs().size() > 0) {
			logger.debug("Generating export links");

			HtmlHyperlink link = null;

			// A HTML link is generated for each ExportConf bean
			for (ExportConf conf : table.getTableConfiguration().getExportConfs()) {

				link = new HtmlHyperlink();

				if (conf.getCssClass() != null) {
					link.setCssClass(conf.getCssClass());
				}

				if (conf.getCssStyle() != null) {
					link.setCssStyle(conf.getCssStyle());
					link.addCssStyle(";margin-left:2px;");
				}
				else{
					link.addCssStyle("margin-left:2px;");
				}

				if(conf.isCustom()){
					String tableId = "oTable_" + table.getId();
					
					StringBuilder exportFunc = new StringBuilder("function dandelion_export_");
					exportFunc.append(conf.getType().name());
					exportFunc.append("(){window.location='");
					exportFunc.append(conf.getUrl());
					if(conf.getUrl().contains("?")){
						exportFunc.append("&");
					}
					else{
						exportFunc.append("?");
					}
					exportFunc.append("' + $.param(");
					exportFunc.append(tableId).append(".oApi._fnAjaxParameters(").append(tableId).append(".fnSettings()");
					exportFunc.append("));}");
					
					mainJsfile.appendToBeforeAll(exportFunc.toString());
					
					link.setOnclick("dandelion_export_" + conf.getType().name() + "()");
				}
				else{
					link.setHref(conf.getUrl());
				}
				link.addContent(conf.getLabel());

				divExport.addContent(link.toHtml());
			}
		}
				
		return divExport;
	}
}
