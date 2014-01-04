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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.extension.Extension;

/**
 * <p>
 * Abstract superclass for all configuration processors, table and column.
 * <p>
 * Basically, a processor processes a value then stores the processed value in
 * the passed {@link TableConfiguration} or {@link ColumnConfiguration}
 * instance.
 * <p>
 * Some processors may need to access a wider context, e.g. to register an
 * {@link Extension} depending on the presence of other attributes. That's why
 * some other objects are also passed to the {@code process} method.
 * <p>
 * Finally, some processors accept a special syntax allowing to load one or more
 * Dandelion scopes to the current {@link HttpServletRequest}. This syntax will
 * be processed only if {@code scopeUpdatable}, which is passed in the
 * constructor of the processor, is set to {@code true}. See
 * {@link #processScope()} for more details.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see TableConfig
 * @see ColumnConfig
 */
public abstract class AbstractConfigurationProcessor implements ConfigurationProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(AbstractConfigurationProcessor.class);

	protected HttpServletRequest request;
	protected TableConfiguration tableConfiguration;
	protected ColumnConfiguration columnConfiguration;
	protected Map<ConfigToken<?>, Object> stagingConf;
	protected Map<ConfigToken<?>, Extension> stagingExtensions;
	protected Entry<ConfigToken<?>, Object> configEntry;
	protected String stringifiedValue;
	protected boolean scopeUpdatable;

	public AbstractConfigurationProcessor() {
		this.scopeUpdatable = false;
	}

	public AbstractConfigurationProcessor(boolean scopeUpdatable) {
		this.scopeUpdatable = scopeUpdatable;
	}

	/**
	 * {@inheritDoc}
	 */
	public void process(Entry<ConfigToken<?>, Object> configEntry, TableConfiguration tableConfiguration) {
		this.configEntry = configEntry;
		this.tableConfiguration = tableConfiguration;
		this.request = tableConfiguration.getRequest();
		this.stringifiedValue = String.valueOf(configEntry.getValue()).trim();

		logger.trace("Processing '{}' with the config token {}", configEntry.getValue(), configEntry.getKey());

		// The value may contain a hash, indicating that one or more scope
		// should be loaded in the current request
		if (this.scopeUpdatable) {
			processScope();
		}

		doProcess();
	}

	/**
	 * {@inheritDoc}
	 */
	public void process(Entry<ConfigToken<?>, Object> configEntry, ColumnConfiguration columnConfiguration,
			TableConfiguration tableConfiguration) {

		this.tableConfiguration = tableConfiguration;
		this.request = tableConfiguration.getRequest();
		this.configEntry = configEntry;
		this.columnConfiguration = columnConfiguration;
		this.stagingConf = columnConfiguration.getStagingConfigurations();
		this.stagingExtensions = columnConfiguration.getStagingExtension();
		this.stringifiedValue = String.valueOf(configEntry.getValue()).trim();

		logger.trace("Processing '{}' with the config token {}", configEntry.getValue(), configEntry.getKey());

		// The value may contain a hash, indicating that one or more scope
		// should be loaded in the current request
		if (this.scopeUpdatable) {
			processScope();
		}

		doProcess();
	}

	/**
	 * <p>
	 * Processes the configuration value stored in {@code stringifiedValue}.
	 */
	protected abstract void doProcess();
	
	/**
	 * <p>
	 * Some processors accept a special syntax, allowing to load one or more
	 * Dandelion scopes in the current {@link HttpServletRequest}.
	 * 
	 * <p>
	 * The syntax is as follows:<br />
	 * {@code scopeNameToAdd[,anotherScopeName]#javascriptObject}.
	 * 
	 * @throws ConfigurationProcessingException
	 *             if the passed value contains an incorrect format.
	 */
	private void processScope() {

		// The value may contain a hash, indicating that one or more scope
		// should be loaded in the current request
		if (stringifiedValue.contains("#")) {
			String[] splittedValue = stringifiedValue.split("#");
			if (stringifiedValue.startsWith("#") || splittedValue.length != 2) {
				StringBuilder sb = new StringBuilder();
				sb.append("Wrong format used in the attribute value. ");
				sb.append("The right format is: 'scopeToAdd#javascriptObject'");
				throw new ConfigurationProcessingException(sb.toString());
			} else {
				if (splittedValue[0].contains(",")) {
					String[] splittedScopes = splittedValue[0].trim().split(",");
					for (String scope : splittedScopes) {
						AssetsRequestContext.get(request).addScopes(scope.trim());
					}
				} else {
					AssetsRequestContext.get(request).addScopes(splittedValue[0].trim());
				}

				// Once the request updated with the scope(s), let's clean both
				// the stringifiedValue and the entry
				this.stringifiedValue = this.stringifiedValue.substring(this.stringifiedValue.indexOf("#") + 1);
				updateEntry(this.stringifiedValue.substring(this.stringifiedValue.indexOf("#") + 1));
			}
		}
	}

	/**
	 * <p>
	 * Utility method used to register an extension to the current
	 * {@link TableConfiguration} instance.
	 * 
	 * @param extension
	 *            The extension to register.
	 */
	protected void registerExtension(Extension extension) {
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
	protected void updateEntry(Object value) {
		this.configEntry.setValue(value);
	}

	/**
	 * <p>
	 * Add a new entry to the staging configuration map of the current
	 * {@link TableConfiguration} instance.
	 * 
	 * <p>
	 * The new entry cannot be directly added to the configuration map because
	 * this map is being iterated on (ConcurrentModificationException).
	 * 
	 * <p>
	 * The staging configuration map will be merged in the final map just after
	 * the end of the loop. See the
	 * {@link TableConfig#processConfiguration(com.github.dandelion.datatables.core.html.HtmlTable)}
	 * method.
	 * 
	 * @param configToken
	 *            The new {@link ConfigToken} to add.
	 * @param value
	 *            The value associated with the {@link ConfigToken}.
	 */
	protected void addTableEntry(ConfigToken<?> configToken, Object value) {
		this.tableConfiguration.getStagingConfiguration().put(configToken, value);
	}

	/**
	 * <p>
	 * Test whether the passed {@link ConfigToken} is already present in the
	 * configuration map.
	 * <p>
	 * In most of the cases, this is useful to initialize a entry with a default
	 * value.
	 * 
	 * @param configToken
	 * @return {@code true} if present, otherwise {@code false}.
	 */
	protected boolean isTableEntryPresent(ConfigToken<?> configToken) {
		return tableConfiguration.getConfigurations().containsKey(configToken);
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
	protected void addColumnEntry(ConfigToken<?> configToken, Object value) {
		this.columnConfiguration.getStagingConfigurations().put(configToken, value);
	}

	/**
	 * <p>
	 * Test whether the passed {@link ConfigToken} is already present in the
	 * configuration map.
	 * <p>
	 * In most of the cases, this is useful to initialize a entry with a default
	 * value.
	 * 
	 * @param configToken
	 * @return {@code true} if present, otherwise {@code false}.
	 */
	protected boolean isColumnEntryPresent(ConfigToken<?> configToken) {
		return columnConfiguration.getConfigurations().containsKey(configToken) || stagingConf.containsKey(configToken);
	}
}