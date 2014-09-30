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

import com.github.dandelion.datatables.core.config.DatatableBundles;
import com.github.dandelion.datatables.core.config.DatatableOptions;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.generator.ColumnFilteringGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Java implementation of the DataTables Column Filter Add-on written by Jovan
 * Popovic.
 * </p>
 * 
 * <p>
 * The add-on now lives in its own repository <a
 * href="https://github.com/tduchateau/jquery-datatables-column-filter"
 * >here</a>.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.7.1
 * @see ColumnConfig#FILTERABLE
 * @see TableConfig#FEATURE_FILTER_PLACEHOLDER
 */
public abstract class AbstractFilteringFeature extends AbstractExtension {

	public static final String FILTERING_FEATURE_NAME = "filtering";

	@Override
	public String getExtensionName() {
		return FILTERING_FEATURE_NAME;
	}

	@Override
	public void setup(HtmlTable table) {

		addBundle(DatatableBundles.DDL_DT_FILTERING);

		FilterPlaceholder filterPlaceHolder = DatatableOptions.FEATURE_FILTER_PLACEHOLDER.valueFrom(table.getTableConfiguration());
		if (filterPlaceHolder != null) {
			switch (filterPlaceHolder) {
			case FOOT:
				adaptFooter(table);
				break;
			case HEAD_AFTER:
				adaptHeader(table);
				break;
			case HEAD_BEFORE:
				adaptHeader(table);
				break;
			case NONE:
				break;
			}
		}
		// Default: footer
		else {
			adaptFooter(table);
		}

		setFunction("columnFilter");
		setConfigGenerator(new ColumnFilteringGenerator());
	}

	protected abstract void adaptHeader(HtmlTable table);

	protected abstract void adaptFooter(HtmlTable table);
}