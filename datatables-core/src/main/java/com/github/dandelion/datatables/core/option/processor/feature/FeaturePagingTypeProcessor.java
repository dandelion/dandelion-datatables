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
package com.github.dandelion.datatables.core.option.processor.feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.extension.feature.PagingType;
import com.github.dandelion.datatables.core.extension.feature.PagingTypeBootstrapFullFeature;
import com.github.dandelion.datatables.core.extension.feature.PagingTypeBootstrapFullNumbersFeature;
import com.github.dandelion.datatables.core.extension.feature.PagingTypeBootstrapSimpleFeature;
import com.github.dandelion.datatables.core.extension.feature.PagingTypeExtJsFeature;
import com.github.dandelion.datatables.core.extension.feature.PagingTypeInputFeature;
import com.github.dandelion.datatables.core.extension.feature.PagingTypeListboxFeature;
import com.github.dandelion.datatables.core.extension.feature.PagingTypeScrollingFeature;
import com.github.dandelion.datatables.core.option.processor.AbstractOptionProcessor;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;

public class FeaturePagingTypeProcessor extends AbstractOptionProcessor {

	private static Logger logger = LoggerFactory.getLogger(FeaturePagingTypeProcessor.class);

	@Override
	protected Object getProcessedValue(OptionProcessingContext context) {

		String valueAsString = context.getValueAsString();
		PagingType retval = null;

		if (StringUtils.isNotBlank(valueAsString)) {

			// Let first try to match the value against an existing pagination
			// type
			try {
				retval = PagingType.valueOf(valueAsString.toUpperCase());
			}
			// If not, we assume that you have previously registered an
			// extension using the Extension mechanism
			catch (IllegalArgumentException e) {
				logger.info(
						"The pagination type '{}' doesn't match any of the predefined pagination types. Make sure an extension is registered for this pagination type.",
						valueAsString);
			}

			if (retval != null) {

				switch (retval) {
				case INPUT:
					context.registerExtension(PagingTypeInputFeature.FEATURE_NAME);
					break;
				case LISTBOX:
					context.registerExtension(PagingTypeListboxFeature.FEATURE_NAME);
					break;
				case SCROLLING:
					context.registerExtension(PagingTypeScrollingFeature.FEATURE_NAME);
					break;
				case EXTSTYLE:
               context.registerExtension(PagingTypeExtJsFeature.FEATURE_NAME);
               break;
               
				// --- Bootstrap 2 styles ---
				case BOOTSTRAP_SIMPLE:
					context.registerExtension(PagingTypeBootstrapSimpleFeature.FEATURE_NAME);
					break;
				case BOOTSTRAP_FULL:
					context.registerExtension(PagingTypeBootstrapFullFeature.FEATURE_NAME);
					break;
				case BOOTSTRAP_FULL_NUMBERS:
					context.registerExtension(PagingTypeBootstrapFullNumbersFeature.FEATURE_NAME);
					break;

				default:
					break;
				}
			}
		}

		return retval;
	}
}
