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
package com.github.dandelion.datatables.core.option.processor;

import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.option.ColumnConfiguration;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.option.TableConfiguration;

/**
 * <p>
 * Abstract superclass for all {@link Option} processors.
 * </p>
 * <p>
 * Basically, a processor processes a value, then stores the processed value in
 * the passed {@link TableConfiguration} or {@link ColumnConfiguration}
 * instance.
 * </p>
 * <p>
 * Some processors may need to access a wider context, e.g. to register an
 * {@link Extension} depending on the presence of other attributes. That's why
 * some other objects are also passed to the {@code process} method.
 * </p>
 * <p>
 * Finally, some processors accept a special syntax allowing to load one or more
 * Dandelion bundles to the current {@link HttpServletRequest}. This syntax will
 * be processed only if {@code bundleAware}, which is passed in the constructor
 * of the processor, is set to {@code true}.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see DatatableOptions
 */
public abstract class AbstractOptionProcessor implements OptionProcessor {

   private static Logger logger = LoggerFactory.getLogger(AbstractOptionProcessor.class);

   private final boolean isBundleGraphUpdatable;

   public AbstractOptionProcessor() {
      this.isBundleGraphUpdatable = false;
   }

   public AbstractOptionProcessor(boolean bundleAware) {
      this.isBundleGraphUpdatable = bundleAware;
   }

   public void process(OptionProcessingContext context) {

      Entry<Option<?>, Object> optionEntry = context.getOptionEntry();
      logger.trace("Processing the option '{}' with the value {}", optionEntry.getKey(), optionEntry.getValue());

      // Update the entry with the processed value
      optionEntry.setValue(getProcessedValue(context));
   }

   /**
    * <p>
    * Processes the {@link Option} value from the provided
    * {@link OptionProcessingContext} and returns it.
    * </p>
    * 
    * @param context
    *           The context to be used during the processing.
    * @return the processed and typed value of the option.
    * @throws DandelionException
    *            if something goes wrong during the processing of the option
    *            value.
    */
   protected abstract Object getProcessedValue(OptionProcessingContext context);

   /**
    * @return {@code true} if the option being processed can update the bundle
    *         graph, {@code false} otherwise.
    */
   public boolean isBundleGraphUpdatable() {
      return this.isBundleGraphUpdatable;
   }
}