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
package com.github.dandelion.datatables.core.option;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.dandelion.core.html.AbstractHtmlTag;
import com.github.dandelion.core.i18n.MessageResolver;
import com.github.dandelion.core.option.Option;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.extension.ExtensionLoader;
import com.github.dandelion.datatables.core.extension.feature.ExtraHtml;
import com.github.dandelion.datatables.core.extension.feature.ExtraJs;

/**
 * <p>
 * Contains all the table configuration.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class TableConfiguration {

   /**
    * The table DOM id.
    */
   private final String tableId;

   /**
    * All {@link Option}/value entries associated with this configuration.
    */
   private final Map<Option<?>, Object> options;

   /**
    * The configured message resolver.
    */
   private final MessageResolver messageResolver;

   /**
    * The current request.
    */
   private final HttpServletRequest request;

   /**
    * The activated option group.
    */
   private final String optionGroupName;

   /**
    * All registered {@link ExtraJs}.
    */
   private Set<ExtraJs> extraJs;

   /**
    * All registered {@link Callback}s.
    */
   private List<Callback> extraCallbacks;

   /**
    * All registered {@link ExtraHtml}.
    */
   private List<ExtraHtml> extraHtmls;

   /**
    * The map of export configuration, one entry per export format.
    */
   private Map<String, ExportConf> exportConfigurations;

   /**
    * Flag that indicates whether the table is being exported.
    */
   private Boolean exporting;

   /**
    * Current export format (if the table is being exported).
    */
   private String currentExportFormat;

   /**
    * All internationalized messages.
    */
   private Properties messages = new Properties();

   /**
    * All registered {@link Extension}s.
    */
   private Set<Extension> internalExtensions;

   /**
    * The current response.
    */
   private HttpServletResponse response;

   /**
    * <b>FOR INTERNAL USE ONLY</b>
    * 
    * @param userConf
    */
   TableConfiguration(String tableId, Map<Option<?>, Object> userConf, MessageResolver messageResolver,
         HttpServletRequest request, String optionGroupName) {
      this.tableId = tableId;
      this.options = userConf;
      this.messageResolver = messageResolver;
      this.request = request;
      this.exportConfigurations = new LinkedHashMap<String, ExportConf>();
      this.optionGroupName = optionGroupName;
   }

   public void set(String exportFormat, ExportConf exportConf) {
      this.exportConfigurations.put(exportFormat, exportConf);
   }

   public Map<String, ExportConf> getExportConfigurations() {
      return exportConfigurations;
   }

   /**
    * @return the current {@link Option}s/values entries associated with this
    *         {@link TableConfiguration} instance.
    */
   public Map<Option<?>, Object> getOptions() {
      return options;
   }

   /**
    * <p>
    * Registers an {@link Extension} which will be processed during the asset
    * generation.
    * </p>
    * 
    * @param extension
    *           The extension to register.
    */
   public TableConfiguration registerExtension(Extension extension) {
      if (this.internalExtensions == null) {
         this.internalExtensions = new HashSet<Extension>();
      }
      this.internalExtensions.add(extension);
      return this;
   }

   /**
    * <p>
    * Registers an {@link Extension} whose name is passed as parameter.
    * </p>
    * 
    * @param extensionName
    *           The name of the {@link Extension} to register.
    */
   public void registerExtension(String extensionName) {
      if (this.internalExtensions == null) {
         this.internalExtensions = new HashSet<Extension>();
      }
      this.internalExtensions.add(ExtensionLoader.get(extensionName));
   }

   public Set<ExtraJs> getExtraJs() {
      return extraJs;
   }

   public TableConfiguration addExtraJs(ExtraJs extraJs) {
      if (this.extraJs == null) {
         this.extraJs = new HashSet<ExtraJs>();
      }
      this.extraJs.add(extraJs);
      return this;
   }

   public void setExtraJs(Set<ExtraJs> extraJs) {
      this.extraJs = extraJs;
   }

   public Set<Extension> getInternalExtensions() {
      return internalExtensions;
   }

   public TableConfiguration setInternalExtensions(Set<Extension> extensions) {
      this.internalExtensions = extensions;
      return this;
   }

   public List<Callback> getCallbacks() {
      return extraCallbacks;
   }

   public void setCallbacks(List<Callback> callbacks) {
      this.extraCallbacks = callbacks;
   }

   public TableConfiguration registerCallback(Callback callback) {
      if (this.extraCallbacks == null) {
         this.extraCallbacks = new ArrayList<Callback>();
      }
      this.extraCallbacks.add(callback);
      return this;
   }

   public Boolean hasCallback(CallbackType callbackType) {
      if (this.extraCallbacks != null) {
         for (Callback callback : this.extraCallbacks) {
            if (callback.getType().equals(callbackType)) {
               return true;
            }
         }
      }
      return false;
   }

   public Callback getCallback(CallbackType callbackType) {
      for (Callback callback : this.extraCallbacks) {
         if (callback.getType().equals(callbackType)) {
            return callback;
         }
      }
      return null;
   }

   public Boolean getExporting() {
      return exporting;
   }

   public void setExporting(Boolean exporting) {
      this.exporting = exporting;
   }

   public TableConfiguration setExportTypes(String exportTypes) {
      return this;
   }

   public String getTableId() {
      return tableId;
   }

   public TableConfiguration addCssStyle(String cssStyle) {

      if (DatatableOptions.CSS_STYLE.valueFrom(this.getOptions()) == null) {
         DatatableOptions.CSS_STYLE.setIn(this.getOptions(), new StringBuilder());
      }
      else {
         DatatableOptions.CSS_STYLE.appendIn(this.getOptions(), AbstractHtmlTag.STYLE_SEPARATOR);
      }
      DatatableOptions.CSS_STYLE.appendIn(this.getOptions(), cssStyle);
      return this;
   }

   public TableConfiguration addCssClass(String cssClass) {
      if (DatatableOptions.CSS_CLASS.valueFrom(this.getOptions()) == null) {
         DatatableOptions.CSS_CLASS.setIn(this.getOptions(), new StringBuilder());
      }
      else {
         DatatableOptions.CSS_CLASS.appendIn(this.getOptions(), AbstractHtmlTag.CLASS_SEPARATOR);
      }
      DatatableOptions.CSS_CLASS.appendIn(this.getOptions(), cssClass);
      return this;
   }

   public HttpServletRequest getRequest() {
      return request;
   }

   public HttpServletResponse getResponse() {
      return response;
   }

   public ExportConf getExportConf(String format) {
      return exportConfigurations.get(format);
   }

   public Properties getMessages() {
      return messages;
   }

   public MessageResolver getMessageResolver() {
      return messageResolver;
   }

   public void setMessages(Properties messages) {
      this.messages = messages;
   }

   public String getMessage(String key) {
      return this.messages.getProperty(key);
   }

   public List<ExtraHtml> getExtraHtmlSnippets() {
      return extraHtmls;
   }

   public void setExtraHtmlSnippets(List<ExtraHtml> linkGroups) {
      this.extraHtmls = linkGroups;
   }

   public void addExtraHtmlSnippet(ExtraHtml extraHtml) {
      if (extraHtmls == null) {
         extraHtmls = new ArrayList<ExtraHtml>();
      }
      this.extraHtmls.add(extraHtml);
   }

   public String getCurrentExportFormat() {
      return currentExportFormat;
   }

   public void setCurrentExportFormat(String currentExport) {
      this.currentExportFormat = currentExport;
   }

   public void addOption(Option<?> option, Object value) {
      this.options.put(option, value);
   }

   public String getOptionGroupName() {
      return optionGroupName;
   }
}