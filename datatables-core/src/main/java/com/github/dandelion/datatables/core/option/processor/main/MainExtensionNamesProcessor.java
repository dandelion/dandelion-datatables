package com.github.dandelion.datatables.core.option.processor.main;

import java.util.HashSet;
import java.util.Set;

import com.github.dandelion.core.option.AbstractOptionProcessor;
import com.github.dandelion.core.option.OptionProcessingContext;
import com.github.dandelion.core.util.StringUtils;

public class MainExtensionNamesProcessor extends AbstractOptionProcessor {

   @Override
   protected Object getProcessedValue(OptionProcessingContext context) {

      String valueAsString = context.getValueAsString();
      Set<String> retval = null;
      if (StringUtils.isNotBlank(valueAsString)) {
         retval = new HashSet<String>();

         String[] customFeatures = valueAsString.split(",");

         for (String feature : customFeatures) {
            retval.add(feature.trim().toLowerCase());
         }
      }

      return retval;
   }
}