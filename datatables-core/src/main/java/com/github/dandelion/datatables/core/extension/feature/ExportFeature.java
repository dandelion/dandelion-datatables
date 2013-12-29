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
package com.github.dandelion.datatables.core.extension.feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.asset.Parameter.Mode;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.configuration.Scope;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.constants.HttpMethod;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlDiv;
import com.github.dandelion.datatables.core.html.HtmlHyperlink;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Extension used to generate export links, depending on the export
 * configurations.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see TableConfig#EXPORT_ENABLED_FORMATS
 * @see TableConfig#EXPORT_LINK_POSITIONS
 */
public class ExportFeature extends AbstractExtension {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ExportFeature.class);
		
	@Override
	public String getName() {
		return "export";
	}

	@Override
	public void setup(HtmlTable table) {

		for(ExportConf exportConf : table.getTableConfiguration().getExportConfiguration().values()){
			if(exportConf.getMethod().equals(HttpMethod.POST)){
				addScope(Scope.DDL_DT_EXPORT);
			}
		}
		
		StringBuilder links = new StringBuilder();
		for (ExportLinkPosition position : TableConfig.EXPORT_LINK_POSITIONS.valueFrom(table.getTableConfiguration())) {

			// Init the wrapping HTML div
			HtmlDiv divExport = initExportDiv(table);
			
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
				if(StringUtils.isNotBlank(TableConfig.FEATURE_SCROLLY.valueFrom(table))){
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

		addCallback(CallbackType.INIT, links.toString(), Mode.APPEND);
	}

	private HtmlDiv initExportDiv(HtmlTable table){
		
		// Init the wrapping HTML div
		HtmlDiv divExport = new HtmlDiv();
		divExport.addCssClass("dandelion_dataTables_export");
		
		// Export is enabled in the table
		if (!table.getTableConfiguration().getExportConfiguration().isEmpty()) {
			logger.debug("Generating export links");

			HtmlHyperlink link = null;

			// A HTML link is generated for each ExportConf bean
			for (ExportConf conf : table.getTableConfiguration().getExportConfiguration().values()) {

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

				if(conf.hasCustomUrl()){
					String tableId = "oTable_" + table.getId();
					
					StringBuilder exportFuncName = new StringBuilder("ddl_dt_launch_export_");
					exportFuncName.append(table.getId());
					exportFuncName.append("_");
					exportFuncName.append(conf.getFormat());
					
					StringBuilder exportFunc = new StringBuilder("function ");
					exportFunc.append(exportFuncName.toString());
					exportFunc.append("(){");
                                        
					// HTTP GET
					if(conf.getMethod().equals(HttpMethod.GET)){
						StringBuilder params = new StringBuilder();
						if(StringUtils.isNotBlank(TableConfig.AJAX_SERVERPARAM.valueFrom(table))){
							exportFunc.append("var aoData = ");
							exportFunc.append(tableId);
							exportFunc.append(".oApi._fnAjaxParameters(");
							exportFunc.append(tableId);
							exportFunc.append(".fnSettings());");
							exportFunc.append(TableConfig.AJAX_SERVERPARAM.valueFrom(table));
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
						exportFunc.append("\n");
						
						appendToBeforeAll(exportFunc.toString());
					}
					// HTTP POST/PUT/DELETE
					else{
						StringBuilder params = new StringBuilder();
						if(StringUtils.isNotBlank(TableConfig.AJAX_SERVERPARAM.valueFrom(table))){
							exportFunc.append("var aoData = ");
							exportFunc.append(tableId);
							exportFunc.append(".oApi._fnAjaxParameters(");
							exportFunc.append(tableId);
							exportFunc.append(".fnSettings());");
							exportFunc.append(TableConfig.AJAX_SERVERPARAM.valueFrom(table));
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
						exportFunc.append("\n");
						
						appendToBeforeAll(exportFunc.toString());					
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