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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.option.OptionProcessingContext;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.export.ExportConf;

/**
 * <p>
 * Processor that configures the file name of within the {@link ExportConf}
 * corresponding to the export format.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 1.1.0
 */
public class ExportFileNameProcessor extends AbstractExportOptionProcessor {

   private static Logger logger = LoggerFactory.getLogger(ExportFileNameProcessor.class);

   @Override
   protected Object getProcessedValue(OptionProcessingContext context) {

      String valueAsString = context.getValueAsString();

      if (StringUtils.isNotBlank(valueAsString)) {

         // Extract the export format
         String exportFormat = getExportFormat(context);

         if (StringUtils.isNotBlank(exportFormat)) {
            logger.debug("Export format found: \"{}\"", exportFormat);
            ExportConf exportConf = getExportConf(exportFormat, context);
            exportConf.setFileName(valueAsString);
         }
         else {
            throw new DandelionException("Format " + exportFormat + " unknown");
         }
      }

      // The value is not used later during the processing but returned anyway
      // mainly for logging purpose
      return valueAsString;
   }
}