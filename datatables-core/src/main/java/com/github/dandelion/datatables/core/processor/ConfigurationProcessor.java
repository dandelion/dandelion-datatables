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
package com.github.dandelion.datatables.core.processor;

import java.util.Map.Entry;

import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;

/**
 * <p>
 * Super interface for all configuration processors.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public interface ConfigurationProcessor {

	/**
	 * <p>
	 * Processes the passed entry which is a {@link ConfigToken} from
	 * {@link ColumnConfig}. The value stored in the entry will be updated
	 * depending on the processor declared in the {@link ConfigToken}.
	 * 
	 * <p>
	 * If new entries are to be added, they will be added in the
	 * {@code stagingConfigurations} field of the passed
	 * {@link ColumnConfiguration} instance.
	 * 
	 * @param configEntry
	 *            The map entry to update.
	 * @param columnConfiguration
	 *            The column configuration to be updated.
	 * @param tableConfiguration
	 *            The table configuration to be updated.
	 */
	public void process(Entry<ConfigToken<?>, Object> configEntry, ColumnConfiguration columnConfiguration,
			TableConfiguration tableConfiguration);

	/**
	 * <p>
	 * Processes the passed entry which is a {@link ConfigToken} from
	 * {@link TableConfig}. The value stored in the entry will be updated
	 * depending on the processor declared in the {@link ConfigToken}.
	 * 
	 * <p>
	 * If new entries are to be added, they will be added in the
	 * {@code stagingConfigurations} field of the passed
	 * {@link TableConfiguration} instance.
	 * 
	 * @param configEntry
	 *            The map entry to update.
	 * @param tableConfiguration
	 *            The table configuration to be updated.
	 */
	public void process(Entry<ConfigToken<?>, Object> configEntry, TableConfiguration tableConfiguration);
}
