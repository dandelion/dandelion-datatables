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
package com.github.dandelion.datatables.core.extension.feature;

import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.ResourceType;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.generator.ColumnFilteringGenerator;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.StringUtils;

/**
 * Java implementation of the DataTables Column Filter Add-on written by Jovan Popovic.
 *
 * @see http://code.google.com/p/jquery-datatables-column-filter/
 * @author Thibault Duchateau
 * @since 0.7.1
 */
public class FilteringFeature extends AbstractFeature {

	@Override
	public String getName() {
		return "Filtering";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public void setup(HtmlTable table) throws BadConfigurationException {

		if(StringUtils.isNotBlank(table.getTableConfiguration().getFeatureFilterPlaceholder())){
			if("head:before".equalsIgnoreCase(table.getTableConfiguration().getFeatureFilterPlaceholder())){
				updateHeader(table);
			}
			else if("head:after".equalsIgnoreCase(table.getTableConfiguration().getFeatureFilterPlaceholder())){
				updateHeader(table);
			}
			else if("foot".equalsIgnoreCase(table.getTableConfiguration().getFeatureFilterPlaceholder())){
				updateFooter(table);
			}
		}
		else{
			updateFooter(table);
		}
		
		setFunction("columnFilter");
		setConfigGenerator(new ColumnFilteringGenerator());
		addJsResource(new JsResource(ResourceType.FEATURE, "FilteringAddOn", "datatables/features/filtering/filteringaddon.js"));
	}
	
	private void updateHeader(HtmlTable table){
		table.addHeaderRow();
		for (HtmlColumn column : table.getFirstHeaderRow().getColumns()) {
			table.getLastHeaderRow().addColumn(column);
		}
	}
	
	private void updateFooter(HtmlTable table){
		table.addFooterRow();
		for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {
			table.getLastFooterRow().addColumn(column);
		}
	}
}