package com.github.dandelion.datatables.core.option.processor.i18n;

import com.github.dandelion.datatables.core.option.processor.AbstractOptionProcessor;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;

/**
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class MessageProcessor extends AbstractOptionProcessor {

	@Override
	protected Object getProcessedValue(OptionProcessingContext context) {
		
		context.getTableConfiguration().getMessages().setProperty(context.getOptionEntry().getKey().getName(),
				String.valueOf(context.getOptionEntry().getValue()));
		
		// TODO bizarre
		return null;
	}
}
