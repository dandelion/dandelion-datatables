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

import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.extension.Extension;

/**
 * <p>
 * Common interface for all processors to be applied on column attributes.
 * 
 * @author Thibault Duchateau
 */
public interface ColumnProcessor extends ConfigurationProcessor {

	/**
	 * Processes the passed value and stores the result in the
	 * {@link ColumnConfiguration} instance.
	 * 
	 * @param configToken
	 *            The token used to store the processed value in the
	 *            {@link TableConfiguration}.
	 * @param value
	 *            The configuration to process.
	 * @param columnConfiguration
	 *            The column configuration to be updated.
	 * @param tableConfiguration
	 *            The table configuration to be updated.
	 * @param stagingConf
	 *            The staging configuration to be applied, in case of some
	 *            processor result would depend on the result of other
	 *            configuration.
	 * @param stagingExtension
	 *            The staging extension to be register in the
	 *            {@link TableConfiguration} instance.
	 */
	public void process(ConfigToken<?> configToken, String value, ColumnConfiguration columnConfiguration,
			TableConfiguration tableConfiguration, Map<ConfigToken<?>, Object> stagingConf,
			Map<ConfigToken<?>, Extension> stagingExtension);
}
