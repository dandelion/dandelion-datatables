/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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
package com.github.dandelion.datatables.core.processor;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.Scope;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.extension.Extension;

/**
 * <p>
 * Abstract processor for all column processors.
 * <p>
 * Basically, a column processor processes a value only if it is neither null
 * nor blank and then stores the processed value in the passed
 * {@link ColumnConfiguration} instance.
 * <p>
 * Some processors may need to access a wider context, e.g. to register an
 * {@link Extension} depending on the presence of other attributes. That's why
 * some others objects are also passed to the {@code process} method.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see ColumnConfig
 */
public abstract class AbstractColumnProcessor implements ColumnProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(AbstractColumnProcessor.class);

	protected TableConfiguration tableConfiguration;
	protected Entry<ConfigToken<?>, Object> configEntry;
	protected ColumnConfiguration columnConfiguration;
	protected Map<ConfigToken<?>, Object> stagingConf;
	protected Map<ConfigToken<?>, Extension> stagingExtensions;
	protected String stringifiedValue;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(Entry<ConfigToken<?>, Object> configEntry, ColumnConfiguration columnConfiguration,
			TableConfiguration tableConfiguration) {
		
		this.tableConfiguration = tableConfiguration;
		this.configEntry = configEntry;
		this.columnConfiguration = columnConfiguration;
		this.stagingConf = columnConfiguration.getStagingConfigurations();
		this.stagingExtensions = columnConfiguration.getStagingExtension();
		this.stringifiedValue = String.valueOf(configEntry.getValue()).trim();
		
		logger.trace("Processing '{}' with the config token {}", configEntry.getValue(), configEntry.getKey());

		doProcess();
	}

	public abstract void doProcess();
	
	/**
	 * <p>
	 * Utility method used to register an extension to the current
	 * {@link TableConfiguration} instance.
	 * 
	 * @param extension
	 *            The extension to register.
	 */
	protected void registerExtension(Extension extension){
		this.tableConfiguration.registerExtension(extension);
	}
	
	/**
	 * <p>
	 * Update the entry using with the new value.
	 * <p>
	 * Note that {@link Entry#setValue(Object)} must be used because the
	 * configuration map is being iterated on.
	 * 
	 * @param value
	 *            The new typed value to set in the entry.
	 */
	protected void updateEntry(Object value){
		this.configEntry.setValue(value);
	}
	
	/**
	 * <p>
	 * Add a new entry to the staging configuration map of the current
	 * {@link ColumnConfiguration} instance.
	 * 
	 * <p>
	 * The new entry cannot be directly added to the configuration map because
	 * this map is being iterated on (ConcurrentModificationException).
	 * 
	 * <p>
	 * The staging configuration map will be merged in the final map just after
	 * the end of the loop. See the
	 * {@link ColumnConfig#processConfiguration(com.github.dandelion.datatables.core.html.HtmlColumn, com.github.dandelion.datatables.core.html.HtmlTable)}
	 * method.
	 * 
	 * @param configToken
	 *            The new {@link ConfigToken} to add.
	 * @param value
	 *            The value associated with the {@link ConfigToken}.
	 */
	protected void addEntry(ConfigToken<?> configToken, Object value){
		this.columnConfiguration.getStagingConfigurations().put(configToken, value);
	}
	
	/**
	 * <p>
	 * Test whether the passed {@link ConfigToken} is already present in the
	 * configuration map.
	 * <p>
	 * In most of the cases, this is useful to initialize a entry with a
	 * default value.
	 * 
	 * @param configToken
	 * @return {@code true} if present, otherwise {@code false}.
	 */
	protected boolean isPresent(ConfigToken<?> configToken){
		return columnConfiguration.getConfigurations().containsKey(configToken) || stagingConf.containsKey(configToken);
	}
	
	/**
	 * <p>
	 * Test whether the passed {@link ConfigToken} is <b>not</b> already present
	 * in the configuration map.
	 * <p>
	 * In most of the cases, this is useful to initialize a entry with a default
	 * value.
	 * 
	 * @param configToken
	 * @return {@code true} if non present, otherwise {@code false}.
	 */
	protected boolean isNonPresent(ConfigToken<?> configToken){
		return !isPresent(configToken);
	}
	
	protected void addScope(Scope scope) {
		AssetsRequestContext.get(tableConfiguration.getRequest()).addScopes(scope.getScopeName());
	}
	
	protected void addScope(String scopeName) {
		AssetsRequestContext.get(tableConfiguration.getRequest()).addScopes(scopeName);
	}
}