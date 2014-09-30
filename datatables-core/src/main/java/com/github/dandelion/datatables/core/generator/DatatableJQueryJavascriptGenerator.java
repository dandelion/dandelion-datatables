/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2014 Dandelion
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
package com.github.dandelion.datatables.core.generator;

import com.github.dandelion.core.asset.generator.jquery.AbstractJQueryJavascriptGenerator;
import com.github.dandelion.core.asset.generator.jquery.JQueryAssetBuffer;

/**
 * <p>
 * 
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.11.0
 */
public class DatatableJQueryJavascriptGenerator extends AbstractJQueryJavascriptGenerator {

	/**
	 * {@inheritDoc}
	 */
	public void fillBuffer(JQueryAssetBuffer jab) {

		DatatableAssetBuffer dab = (DatatableAssetBuffer) jab;

		appendToPlaceholder(Placeholder.BEFORE_ALL, dab.getBeforeAll());
		appendToPlaceholder(Placeholder.BEFORE_ALL, getVariables(dab));
		appendToPlaceholder(Placeholder.BEFORE_START_DOCUMENT_READY, dab.getBeforeStartDocumentReady());
		appendToPlaceholder(Placeholder.COMPONENT_CONFIGURATION, getComponentConf(dab));
		appendToPlaceholder(Placeholder.AFTER_START_DOCUMENT_READY, dab.getAfterStartDocumentReady());
		appendToPlaceholder(Placeholder.BEFORE_END_DOCUMENT_READY, dab.getBeforeEndDocumentReady());
		appendToPlaceholder(Placeholder.AFTER_ALL, dab.getAfterAll());
	}

	public StringBuilder getComponentConf(DatatableAssetBuffer jsResource) {
		String tableId = jsResource.getProcessedId();

		StringBuilder datatablesConfiguration = new StringBuilder();

		datatablesConfiguration.append("oTable_")
			.append(tableId)
			.append(".dataTable(oTable_")
			.append(tableId)
			.append("_params)");

		if (jsResource.getDataTablesExtra() != null) {
			datatablesConfiguration.append(".");
			datatablesConfiguration.append(jsResource.getDataTablesExtra());
			datatablesConfiguration.append("(");
			if (jsResource.getDataTablesExtraConf() != null) {
				datatablesConfiguration.append(jsResource.getDataTablesExtraConf());
			}
			datatablesConfiguration.append(")");
		}

		datatablesConfiguration.append(";");

		return datatablesConfiguration;
	}

	private StringBuilder getVariables(DatatableAssetBuffer datatableAssetBuffer) {
		String tableId = datatableAssetBuffer.getProcessedId();
		String originalId = datatableAssetBuffer.getOriginalId();

		StringBuilder variables = new StringBuilder();
		variables.append("var oTable_").append(tableId).append("=$('#").append(originalId).append("');");

		variables.append("var oTable_").append(tableId).append("_params=")
				.append(datatableAssetBuffer.getDataTablesConf()).append(";");

		return variables;
	}
}
