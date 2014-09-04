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
package com.github.dandelion.datatables.core.processor.feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeBootstrapFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeBootstrapFourButtonFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeBootstrapFullNumbersFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeFourButtonFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeInputFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeListboxFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeScrollingFeature;
import com.github.dandelion.datatables.core.processor.AbstractConfigurationProcessor;

public class FeaturePaginationTypeProcessor extends AbstractConfigurationProcessor {

	private static Logger logger = LoggerFactory.getLogger(FeaturePaginationTypeProcessor.class);

	@Override
	public void doProcess() {

		PaginationType type = null;

		if (StringUtils.isNotBlank(stringifiedValue)) {

			// Let first try to match the value against an existing pagination
			// type
			try {
				type = PaginationType.valueOf(stringifiedValue.toUpperCase());
			}
			// If not, we assume that you have previously registered an
			// extension using the Extension mechanism
			catch (IllegalArgumentException e) {
				logger.info(
						"The pagination type '{}' doesn't match any of the predefined pagination types. Make sure an extension is registered for this pagination type.",
						stringifiedValue);
			}

			if (type != null) {

				switch (type) {
				case INPUT:
					registerExtension(PaginationTypeInputFeature.PAGINATION_TYPE_INPUT_FEATURE_NAME);
					break;
				case LISTBOX:
					registerExtension(PaginationTypeListboxFeature.PAGINATION_TYPE_LISTBOX_FEATURE_NAME);
					break;
				case SCROLLING:
					registerExtension(PaginationTypeScrollingFeature.PAGINATION_TYPE_SCROLLING_FEATURE_NAME);
					break;
				case FOUR_BUTTON:
					registerExtension(PaginationTypeFourButtonFeature.PAGINATION_TYPE_FOURBUTTON_FEATURE_NAME);
					break;

				// --- Bootstrap 2 styles ---
				case BOOTSTRAP:
					registerExtension(PaginationTypeBootstrapFeature.PAGINATION_TYPE_BS_FEATURE_NAME);
					break;
				case BOOTSTRAP_FOUR_BUTTON:
					registerExtension(PaginationTypeBootstrapFourButtonFeature.PAGINATION_TYPE_BS_FOURBUTTON_FEATURE_NAME);
					break;
				case BOOTSTRAP_FULL_NUMBERS:
					registerExtension(PaginationTypeBootstrapFullNumbersFeature.PAGINATION_TYPE_BS_FULLNUMBERS_FEATURE_NAME);
					break;

				default:
					break;
				}
			}

		}

		updateEntry(type);
	}
}
