/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2014 Dandelion
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
package com.github.dandelion.datatables.core.util;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.config.ColumnConfiguration;
import com.github.dandelion.datatables.core.config.TableConfiguration;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.option.processor.OptionProcessor;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;

public final class ConfigUtils {

	protected static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

	/**
	 * <p>
	 * Overloads the configurations stored in the {@link TableConfiguration}
	 * instance with the one passed as parameter.
	 * 
	 * @param stagingConf
	 *            The staging configurations filled either with the JSP taglib
	 *            or with the Thymeleaf dialect.
	 * @param table
	 *            The table which holds the {@link TableConfiguration} to
	 *            overload.
	 */
	public static void applyConfiguration(Map<Option<?>, Object> stagingConf, HtmlTable table) {

		for (Entry<Option<?>, Object> stagingEntry : stagingConf.entrySet()) {
			table.getTableConfiguration().getConfigurations().put(stagingEntry.getKey(), stagingEntry.getValue());
		}
	}

	/**
	 * <p>
	 * At this point, the configuration stored inside the
	 * {@link TableConfiguration} contains only Strings. All these strings will
	 * be processed in this method, depending on the {@link ConfigToken} they
	 * are bound to.
	 * </p>
	 * <p>
	 * Once processed, all strings will be replaced by the typed value.
	 * </p>
	 * <p>
	 * Only configuration token with not blank values will be merged into the
	 * {@link TableConfiguration} instance.
	 * </p>
	 * 
	 * @param table
	 *            The table which holds the configuration to process.
	 */
	public static void processConfiguration(HtmlTable table) {

		Map<Option<?>, Object> configurations = table.getTableConfiguration().getConfigurations();

		if (configurations != null) {

			for (Entry<Option<?>, Object> entry : configurations.entrySet()) {
				logger.debug("Processing configuration \"{}\" with the value \"{}\"({})", entry.getKey(),
						entry.getValue(), entry.getValue().getClass().getSimpleName());
				OptionProcessor optionProcessor = entry.getKey().getProcessor();
				OptionProcessingContext pc = new OptionProcessingContext(entry, table.getTableConfiguration(), null, optionProcessor.isBundleGraphUpdatable());
				optionProcessor.process(pc);
			}

			// Merging staging configuration into to the final configuration map
			configurations.putAll(table.getTableConfiguration().getStagingConfiguration());
		}
	}

	/**
	 * <p>
	 * Overloads the configurations stored in the {@link ColumnConfiguration}
	 * instance with the one passed as parameter.
	 * <p>
	 * Only configuration token with not blank values will be merged into the
	 * {@link ColumnConfiguration} instance.
	 * 
	 * @param stagingConf
	 *            The staging configurations filled either with the JSP taglib
	 *            or with the Thymeleaf dialect.
	 * @param stagingExtensions
	 *            The staging extensions filled either with the JSP taglib or
	 *            with the Thymeleaf dialect.
	 * @param column
	 *            The column which holds the {@link ColumnConfiguration} to
	 *            overload.
	 */
	public static void applyConfiguration(Map<Option<?>, Object> stagingConf,
			Map<Option<?>, Extension> stagingExtensions, HtmlColumn column) {

		for (Entry<Option<?>, Object> stagingEntry : stagingConf.entrySet()) {
			column.getColumnConfiguration().getConfigurations().put(stagingEntry.getKey(), stagingEntry.getValue());
		}

		column.getColumnConfiguration().getStagingExtension().putAll(stagingExtensions);
	}

	/**
	 * <p>
	 * At this point, the configuration stored inside the
	 * {@link ColumnConfiguration} contains only Strings. All these strings will
	 * be processed in this method, depending on the {@link ConfigToken} they
	 * are bound to.
	 * 
	 * <p>
	 * Once processed, all strings will be replaced by the typed value.
	 * 
	 * @param column
	 *            The column which contains the configurations to process.
	 * @param table
	 *            The table may be used by processor to register extensions.
	 */
	public static void processConfiguration(HtmlColumn column, HtmlTable table) {

		if (column.getColumnConfiguration().getConfigurations() != null) {
			for (Entry<Option<?>, Object> entry : column.getColumnConfiguration().getConfigurations().entrySet()) {
				
				OptionProcessor optionProcessor = entry.getKey().getProcessor();
				OptionProcessingContext pc = new OptionProcessingContext(entry, table.getTableConfiguration(), column.getColumnConfiguration(), optionProcessor.isBundleGraphUpdatable());
				optionProcessor.process(pc);
				
			}

			// Merging staging configuration into to the final configuration map
			column.getColumnConfiguration().getConfigurations()
					.putAll(column.getColumnConfiguration().getStagingConfigurations());
		}
	}

	/**
	 * <p>
	 * Suppress default constructor for noninstantiability.
	 * </p>
	 */
	private ConfigUtils() {
		throw new AssertionError();
	}
}
