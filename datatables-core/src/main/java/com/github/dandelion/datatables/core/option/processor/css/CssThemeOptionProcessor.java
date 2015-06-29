package com.github.dandelion.datatables.core.option.processor.css;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.util.EnumUtils;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.extension.theme.ThemeOption;
import com.github.dandelion.datatables.core.option.processor.AbstractOptionProcessor;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;

public class CssThemeOptionProcessor extends AbstractOptionProcessor {

   @Override
   protected Object getProcessedValue(OptionProcessingContext context) {

      ThemeOption retval = null;
      String valueAsString = context.getValueAsString();
      if (StringUtils.isNotBlank(valueAsString)) {

         try {
            retval = ThemeOption.valueOf(valueAsString.toUpperCase());
         }
         catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("'");
            sb.append(valueAsString);
            sb.append("' is not a valid theme option. Possible values are: ");
            sb.append(EnumUtils.printPossibleValuesOf(ThemeOption.class));
            throw new DandelionException(sb.toString(), e);
         }

      }

      return retval;
   }
}