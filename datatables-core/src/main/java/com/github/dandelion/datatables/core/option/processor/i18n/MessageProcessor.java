package com.github.dandelion.datatables.core.option.processor.i18n;

import com.github.dandelion.core.option.AbstractOptionProcessor;
import com.github.dandelion.core.option.OptionProcessingContext;
import com.github.dandelion.datatables.core.option.TableConfiguration;

/**
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class MessageProcessor extends AbstractOptionProcessor {

   @Override
   protected Object getProcessedValue(OptionProcessingContext context) {

      TableConfiguration tc = (TableConfiguration) context.getRequest()
            .getAttribute(TableConfiguration.class.getCanonicalName());
      tc.getMessages().setProperty(context.getOptionEntry().getKey().getName(),
            String.valueOf(context.getOptionEntry().getValue()));

      // TODO bizarre
      return null;
   }
}
