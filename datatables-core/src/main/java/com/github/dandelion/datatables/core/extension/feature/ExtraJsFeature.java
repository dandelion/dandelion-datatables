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
package com.github.dandelion.datatables.core.extension.feature;

import static com.github.dandelion.datatables.core.util.JavascriptUtils.INDENT;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.core.asset.Asset;
import com.github.dandelion.core.asset.AssetStack;
import com.github.dandelion.core.asset.AssetType;
import com.github.dandelion.core.asset.processor.impl.AssetLocationProcessorEntry;
import com.github.dandelion.core.asset.processor.spi.AssetProcessorEntry;
import com.github.dandelion.core.asset.wrapper.spi.AssetLocationWrapper;
import com.github.dandelion.core.utils.ResourceUtils;
import com.github.dandelion.datatables.core.asset.ExtraJs;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Feature used to insert end-user Javascript files in the generated DataTable
 * confguration.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public class ExtraJsFeature extends AbstractExtension {

	AssetProcessorEntry assetLocationProcessorEntry = new AssetLocationProcessorEntry();

	@Override
	public String getName() {
		return "extraJs";
	}

	@Override
	public void setup(HtmlTable table) {

		HttpServletRequest request = table.getTableConfiguration().getRequest();

		for (ExtraJs extraJs : table.getTableConfiguration().getExtraJs()) {

			List<Asset> assets = AssetStack.assetsFor(extraJs.getScopes());
			List<Asset> jsAssets = AssetStack.filterByType(assets, AssetType.js);
			List<Asset> processedAssets = assetLocationProcessorEntry.process(jsAssets, request);

			Map<String, AssetLocationWrapper> wrappers = AssetStack.getAssetsLocationWrappers();

			for (Asset asset : processedAssets) {
				for (Map.Entry<String, String> location : asset.getLocations().entrySet()) {
					AssetLocationWrapper wrapper = wrappers.get(location.getKey());
					String content;
					if (wrapper != null) {
						content = wrapper.getWrappedContent(asset, request);
					}
					else {
						content = ResourceUtils.getContentFromUrl(request, location.getValue(), true);
					}

					if (!content.endsWith("\n")) {
						content += "\n";
					}

					switch (extraJs.getInsert()) {
					case BEFOREALL:
						appendToBeforeAll(content);
						break;

					case AFTERSTARTDOCUMENTREADY:
						appendToAfterStartDocumentReady(INDENT);
						appendToAfterStartDocumentReady(content);
						break;

					case BEFOREENDDOCUMENTREADY:
						appendToAfterStartDocumentReady(INDENT);
						appendToBeforeEndDocumentReady(content);
						break;

					case AFTERALL:
						appendToAfterAll(content);
						break;

					case BEFORESTARTDOCUMENTREADY:
						appendToBeforeStartDocumentReady(content);
						break;
					}
				}
			}
		}
	}
}