/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2015 Dandelion
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
package com.github.dandelion.datatables.core.option.processor.export;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.dandelion.core.option.AbstractOptionProcessor;
import com.github.dandelion.core.option.OptionProcessingContext;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.option.TableConfiguration;

/**
 * <p>
 * Abstract option processor for all export-related options.
 * </p>
 * <p>
 * Mainly contains utilities when processing export options.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 1.1.0
 */
public abstract class AbstractExportOptionProcessor extends AbstractOptionProcessor {

   public static final String REGEX_EXPORT_FORMAT = "export\\.(.*?)\\.";

   protected static final Pattern PATTERN_EXPORT_FORMAT = Pattern.compile(REGEX_EXPORT_FORMAT,
         Pattern.CASE_INSENSITIVE);

   /**
    * <p>
    * Extracts the export format from the option name.
    * </p>
    * 
    * @param context
    *           The context to be used during the option processing.
    * @return the current export format.
    */
   protected String getExportFormat(OptionProcessingContext context) {

      String exportFormat = null;
      Matcher m = PATTERN_EXPORT_FORMAT.matcher(context.getOptionEntry().getKey().getName());
      if (m.find()) {
         exportFormat = m.group(1);
      }

      return exportFormat;
   }

   /**
    * <p>
    * Returns an instance of {@link ExportConf} to be configured by the export
    * option.
    * </p>
    * <p>
    * The instance is retrieved either from the corresponding
    * {@link TableConfiguration} if it exists or is created and then stored in
    * {@link TableConfiguration}.
    * </p>
    * 
    * @param exportFormat
    *           The current export format.
    * @param context
    *           The context to be used during the option processing.
    * @return
    */
   protected ExportConf getExportConf(String exportFormat, OptionProcessingContext context) {

      TableConfiguration tc = (TableConfiguration) context.getRequest()
            .getAttribute(TableConfiguration.class.getCanonicalName());

      ExportConf exportConf = null;
      // The export configuration already exists for this export format
      if (tc.getExportConfigurations().containsKey(exportFormat)) {

         exportConf = tc.getExportConfigurations().get(exportFormat);
      }
      // The export configuration must be initialized
      else {
         exportConf = new ExportConf(exportFormat);
         tc.getExportConfigurations().put(exportFormat, exportConf);
      }

      return exportConf;
   }
}
