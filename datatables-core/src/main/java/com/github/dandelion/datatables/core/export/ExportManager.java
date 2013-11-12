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
package com.github.dandelion.datatables.core.export;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.asset.JavascriptFunction;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.constants.HttpMethod;
import com.github.dandelion.datatables.core.html.HtmlDiv;
import com.github.dandelion.datatables.core.html.HtmlHyperlink;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.FileUtils;

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
	 * All links are surrounded by a div which is inserted in the DOM using
	 * jQuery. The wrapping div can be inserted at multiple positions, depending
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

		for(ExportConf exportConf : table.getTableConfiguration().getExportConfs()){
			if(exportConf.getMethod().equals(HttpMethod.POST)){
				try {
					mainJsFile.appendToBeforeAll(FileUtils.getFileContentFromClasspath("datatables/export/download.js"));
				} catch (IOException e) {
					logger.warn("Unable to retrieve the content of the download.js file");
				}
				break;
			}
		}
		
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
				if(StringUtils.isNotBlank(table.getTableConfiguration().getFeatureScrollY())){
					links.append("$('div.dataTables_scroll').before('" + divExport.toHtml() + "');");
				}
				else{
					links.append("$('#" + table.getId()	+ "').before('" + divExport.toHtml() + "');");
				}
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
			table.getTableConfiguration().getCallback(CallbackType.INIT).appendCode(links.toString());
		}
		else{
			Callback initCallback = new Callback(CallbackType.INIT, new JavascriptFunction(links.toString(),
					CallbackType.INIT.getArgs()));
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
					
					StringBuilder exportFuncName = new StringBuilder("ddl_dt_launch_export_");
					exportFuncName.append(tableId);
					exportFuncName.append("_");
					exportFuncName.append(conf.getType().name());
					
					StringBuilder exportFunc = new StringBuilder("function ");
					exportFunc.append(exportFuncName.toString());
					exportFunc.append("(){");
                                        
					// HTTP GET
					if(conf.getMethod().equals(HttpMethod.GET)){
						StringBuilder params = new StringBuilder();
						if(StringUtils.isNotBlank(table.getTableConfiguration().getAjaxServerParam())){
							exportFunc.append("var aoData = ");
							exportFunc.append(tableId);
							exportFunc.append(".oApi._fnAjaxParameters(");
							exportFunc.append(tableId);
							exportFunc.append(".fnSettings());");
							exportFunc.append(table.getTableConfiguration().getAjaxServerParam());
							exportFunc.append("(aoData);");
							params.append("aoData");
						}
						else{
							params.append(tableId);
							params.append(".oApi._fnAjaxParameters(");
							params.append(tableId);
							params.append(".fnSettings()");
						}
						
						exportFunc.append("window.location='");
						exportFunc.append(conf.getUrl());
						if(conf.getUrl().contains("?")){
							exportFunc.append("&");
						}
						else{
							exportFunc.append("?");
						}
						exportFunc.append("' + $.param(");
						exportFunc.append(params.toString());
						exportFunc.append("));}");
						
						mainJsfile.appendToBeforeAll(exportFunc.toString());
					}
					// HTTP POST/PUT/DELETE
					else{
						StringBuilder params = new StringBuilder();
						if(StringUtils.isNotBlank(table.getTableConfiguration().getAjaxServerParam())){
							exportFunc.append("var aoData = ");
							exportFunc.append(tableId);
							exportFunc.append(".oApi._fnAjaxParameters(");
							exportFunc.append(tableId);
							exportFunc.append(".fnSettings());");
							exportFunc.append(table.getTableConfiguration().getAjaxServerParam());
							exportFunc.append("(aoData);");
							params.append("aoData");
						}
						else{
							params.append(tableId);
							params.append(".oApi._fnAjaxParameters(");
							params.append(tableId);
							params.append(".fnSettings())");
						}
						
						exportFunc.append("$.download('");
						exportFunc.append(conf.getUrl());
						exportFunc.append("',$.param(");
						exportFunc.append(params.toString());
						exportFunc.append("),'");
						exportFunc.append(conf.getMethod());
						exportFunc.append("');");
						exportFunc.append("}");
						
						mainJsfile.appendToBeforeAll(exportFunc.toString());					
					}
					link.setOnclick(exportFuncName.toString().concat("();"));
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
