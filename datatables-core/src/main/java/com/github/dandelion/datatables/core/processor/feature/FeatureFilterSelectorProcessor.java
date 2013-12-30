package com.github.dandelion.datatables.core.processor.feature;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.extension.feature.MultiFilterFeature;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

/**
 * As soon as the {@code filterSelector} / {@code dt:filterSelector}
 * (JSP/Thymeleaf) table attribute is used, we assume the end-user wants to use
 * the {@link MultiFilterFeature}.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public class FeatureFilterSelectorProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess() {

		if (StringUtils.isNotBlank(stringifiedValue)) {
			registerExtension(new MultiFilterFeature());
			addEntry(TableConfig.FEATURE_FILTER_TRIGGER, "click");
			updateEntry(stringifiedValue);
		}
	}
}