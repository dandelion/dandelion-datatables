package com.github.dandelion.datatables.core.option.processor.feature;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.extension.feature.MultiFilterFeature;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.processor.AbstractOptionProcessor;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;

/**
 * As soon as the {@code filterSelector} / {@code dt:filterSelector}
 * (JSP/Thymeleaf) table attribute is used, we assume the end-user wants to use
 * the {@link MultiFilterFeature}.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public class FeatureFilterSelectorProcessor extends AbstractOptionProcessor {

	@Override
	protected Object getProcessedValue(OptionProcessingContext context) {

		String valueAsString = context.getValueAsString();
		
		if (StringUtils.isNotBlank(valueAsString)) {
			context.registerExtension(MultiFilterFeature.MULTI_FILTER_FEATURE_NAME);
			context.addTableEntry(DatatableOptions.FEATURE_FILTER_TRIGGER, "click");
		}
		
		return valueAsString;
	}
}