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

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.core.asset.Asset;
import com.github.dandelion.core.asset.AssetMapper;
import com.github.dandelion.core.asset.AssetType;
import com.github.dandelion.core.asset.locator.spi.AssetLocator;
import com.github.dandelion.core.storage.AssetStorageUnit;
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

	public static final String EXTRA_JS_FEATURE_NAME = "extraJs";

	@Override
	public String getExtensionName() {
		return EXTRA_JS_FEATURE_NAME;
	}

	@Override
	public void setup(HtmlTable table) {

		AssetMapper assetMapper = new AssetMapper(table.getTableConfiguration().getRequest(), getContext());
		
		HttpServletRequest request = table.getTableConfiguration().getRequest();
		Set<AssetStorageUnit> assetsToInject = null;
		
		for (ExtraJs extraJs : table.getTableConfiguration().getExtraJs()) {
			
			assetsToInject = new LinkedHashSet<AssetStorageUnit>();
	
//			AssetQuery aq = new AssetQuery(table.getTableConfiguration().getRequest(), getContext()).;
			for(String bundleName : extraJs.getBundles()){
				assetsToInject.addAll(getContext().getBundleStorage().getBundleDag().getVertex(bundleName).getAssetStorageUnits());
			}

			Set<AssetStorageUnit> filteredAsus = new LinkedHashSet<AssetStorageUnit>();
			for (AssetStorageUnit asu: assetsToInject) {
				if (asu.getType().equals(AssetType.js)) {
					filteredAsus.add(asu);
				}
			}
			
			Set<Asset> processedAssets = assetMapper.mapToAssets(filteredAsus);
	
			Map<String, AssetLocator> locators = getContext().getAssetLocatorsMap();
	
			for (Asset asset : processedAssets) {
				AssetLocator locator = locators.get(asset.getConfigLocationKey());
				String content = locator.getContent(asset, request);
	
				switch (extraJs.getInsert()) {
				case BEFOREALL:
					appendToBeforeAll(content);
					break;

				case AFTERSTARTDOCUMENTREADY:
					appendToAfterStartDocumentReady(content);
					break;

				case BEFOREENDDOCUMENTREADY:
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