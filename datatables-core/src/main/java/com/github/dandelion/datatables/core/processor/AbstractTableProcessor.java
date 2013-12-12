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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.extension.Extension;

/**
 * <p>
 * Abstract processor for all table processors.
 * <p>
 * Basically, a table processor processes a value only if it is neither null nor
 * blank and then stores the processed value in the passed
 * {@link TableConfiguration} instance.
 * <p>
 * Some processors may need to access a wider context, e.g. to register an
 * {@link Extension} depending on the presence of other attributes. That's why
 * some other objects are also passed to the {@code process} method.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see ColumnConfig
 */
public abstract class AbstractTableProcessor implements TableProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(AbstractTableProcessor.class);

	protected TableConfiguration tableConfiguration;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(ConfigToken<?> configToken, String value, TableConfiguration tableConfiguration) {
		this.tableConfiguration = tableConfiguration;

		logger.trace("Processing '{}' with the config token {}", value, configToken);
		
		doProcess(configToken, value);
	}

	public abstract void doProcess(ConfigToken<?> configToken, String value);
}