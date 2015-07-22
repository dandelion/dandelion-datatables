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

import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.extension.ExtensionLoader;
import com.github.dandelion.datatables.core.option.ColumnConfiguration;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.option.TableConfiguration;
import com.github.dandelion.datatables.core.util.ProcessorUtils;

/**
 * <p>
 * Context in which an {@link Option}/value pair is processed.
 * </p>
 * <p>
 * Also provides utilities to register {@link Extension} in the
 * {@link TableConfiguration} instance.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 1.0.0
 */
public class OptionProcessingContext {

   /**
    * The Option/value pair to be processed.
    */
   private final Entry<Option<?>, Object> optionEntry;

   /**
    * The current TableConfiguration in which the Option/value pair is
    * processed.
    */
   private final TableConfiguration tableConfiguration;

   /**
    * The current ColumnConfiguration in which the Option/value pair is
    * processed.
    */
   private final ColumnConfiguration columnConfiguration;

   /**
    * The current request.
    */
   private final HttpServletRequest request;

   /**
    * The option value as a String.
    */
   private final String valueAsString;

   /**
    * Whether the OptionProcess can update the bundle graph or not.
    */
   private final boolean isBundleGraphUpdatable;

   public OptionProcessingContext(Entry<Option<?>, Object> optionEntry, TableConfiguration tableConfiguration) {
      this(optionEntry, tableConfiguration, null, false);
   }

   public OptionProcessingContext(Entry<Option<?>, Object> optionEntry, TableConfiguration tableConfiguration,
         boolean isBundleGraphUpdatable) {
      this(optionEntry, tableConfiguration, null, isBundleGraphUpdatable);
   }

   public OptionProcessingContext(Entry<Option<?>, Object> optionEntry, TableConfiguration tableConfiguration,
         ColumnConfiguration columnConfiguration) {
      this(optionEntry, tableConfiguration, columnConfiguration, false);
   }

   public OptionProcessingContext(Entry<Option<?>, Object> optionEntry, TableConfiguration tableConfiguration,
         ColumnConfiguration columnConfiguration, boolean isBundleGraphUpdatable) {
      this.optionEntry = optionEntry;
      this.tableConfiguration = tableConfiguration;
      this.columnConfiguration = columnConfiguration;
      this.request = tableConfiguration.getRequest();
      this.isBundleGraphUpdatable = isBundleGraphUpdatable;

      if (!this.isBundleGraphUpdatable) {
         this.valueAsString = optionEntry.getValue() != null ? String.valueOf(optionEntry.getValue()).trim() : null;
      }
      else {
         if (optionEntry.getValue() != null) {
            this.valueAsString = ProcessorUtils
                  .getValueAfterProcessingBundles(String.valueOf(optionEntry.getValue()).trim(), this.request);
         }
         else {
            this.valueAsString = null;
         }
      }
   }

   /**
    * @return the {@link Option}/value pair to be processed.
    */
   public Entry<Option<?>, Object> getOptionEntry() {
      return this.optionEntry;
   }

   /**
    * @return the {@link TableConfiguration} in which the {@link Option}/value
    *         pair is processed.
    */
   public TableConfiguration getTableConfiguration() {
      return this.tableConfiguration;
   }

   /**
    * @return the {@link ColumnConfiguration} in which the {@link Option}/value
    *         pair is processed.
    */
   public ColumnConfiguration getColumnConfiguration() {
      return this.columnConfiguration;
   }

   /**
    * @return the option value as a {@link String} which may have been
    *         pre-processed depending on the {@link #isBundleGraphUpdatable}
    *         value.
    */
   public String getValueAsString() {
      return this.valueAsString;
   }

   /**
    * @return the current request.
    */
   public HttpServletRequest getRequest() {
      return this.request;
   }

   /**
    * <p>
    * Utility method used to register an {@link Extension} in the current
    * {@link TableConfiguration} instance.
    * 
    * @param extension
    *           The {@link Extension} to register.
    */
   public void registerExtension(Extension extension) {
      this.tableConfiguration.registerExtension(extension);
   }

   /**
    * <p>
    * Utility method used to register an {@link Extension} in the current
    * {@link TableConfiguration} instance.
    * 
    * @param extensionName
    *           The name of the {@link Extension} to register.
    */
   public void registerExtension(String extensionName) {
      this.tableConfiguration.registerExtension(ExtensionLoader.get(extensionName));
   }
}
