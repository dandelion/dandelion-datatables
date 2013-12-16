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

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeBootstrapFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeBootstrapFourButtonFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeBootstrapFullNumbersFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeFourButtonFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeInputFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeListboxFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeScrollingFeature;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class FeaturePaginationTypeProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess() {
		PaginationType type = null;
		if (StringUtils.isNotBlank(stringifiedValue)) {
			try {
				type = PaginationType.valueOf(stringifiedValue.toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new ConfigurationProcessingException(
						stringifiedValue + " is not a valid value among " + PaginationType.values(), e);
			}

			switch (type) {
				case INPUT:
					tableConfiguration.registerExtension(new PaginationTypeInputFeature());
					break;
				case LISTBOX:
					tableConfiguration.registerExtension(new PaginationTypeListboxFeature());
					break;
				case SCROLLING:
					tableConfiguration.registerExtension(new PaginationTypeScrollingFeature());
					break;
				case FOUR_BUTTON:
					tableConfiguration.registerExtension(new PaginationTypeFourButtonFeature());
					break;
				
				// --- Bootstrap 2 styles ---
				case BOOTSTRAP:
					tableConfiguration.registerExtension(new PaginationTypeBootstrapFeature());
					break;
				case BOOTSTRAP_FOUR_BUTTON:
					tableConfiguration.registerExtension(new PaginationTypeBootstrapFourButtonFeature());
					break;
				case BOOTSTRAP_FULL_NUMBERS:
					tableConfiguration.registerExtension(new PaginationTypeBootstrapFullNumbersFeature());
					break;
				
				default:
					break;
			}
		}
		
		updateEntry(type);
	}
}