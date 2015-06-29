package com.github.dandelion.datatables.core.option.processor.feature;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.extension.feature.FilterPlaceholder;
import com.github.dandelion.datatables.core.option.processor.AbstractOptionProcessor;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;

public class FeatureFilterPlaceholderProcessor extends AbstractOptionProcessor {

   @Override
   protected Object getProcessedValue(OptionProcessingContext context) {

      String valueAsString = context.getValueAsString();
      FilterPlaceholder retval = null;
      if (StringUtils.isNotBlank(valueAsString)) {
         try {
            retval = FilterPlaceholder.valueOf(valueAsString.toUpperCase());
         }
         catch (IllegalArgumentException e) {
            throw new DandelionException(valueAsString + " is not a valid value among " + FilterPlaceholder.values(), e);
         }
      }

      return retval;
   }
}