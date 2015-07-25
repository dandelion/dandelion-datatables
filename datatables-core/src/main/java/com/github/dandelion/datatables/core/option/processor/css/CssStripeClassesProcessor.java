package com.github.dandelion.datatables.core.option.processor.css;

import java.util.Arrays;
import java.util.Iterator;

import com.github.dandelion.core.option.AbstractOptionProcessor;
import com.github.dandelion.core.option.OptionProcessingContext;
import com.github.dandelion.core.util.StringUtils;

public class CssStripeClassesProcessor extends AbstractOptionProcessor {

   @Override
   protected Object getProcessedValue(OptionProcessingContext context) {

      String retval = null;
      String valueAsString = context.getValueAsString();

      if (StringUtils.isNotBlank(valueAsString)) {

         StringBuilder stripeTmp = new StringBuilder("[");
         if (valueAsString.contains(",")) {
            String[] tmp = valueAsString.trim().split(",");
            Iterator<String> iterator = Arrays.asList(tmp).iterator();
            stripeTmp.append("'").append(iterator.next().trim()).append("'");
            while (iterator.hasNext()) {
               stripeTmp.append(",'").append(iterator.next().trim()).append("'");
            }
            retval = stripeTmp.toString();
         }
         else {
            stripeTmp.append("'").append(valueAsString).append("'");
         }
         stripeTmp.append("]");
         retval = stripeTmp.toString();
      }

      return retval;
   }
}