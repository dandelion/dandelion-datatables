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
package com.github.dandelion.datatables.core.configuration;

import java.util.Map;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.processor.ConfigurationProcessor;

/**
 * <p>
 * Token used to store a configuration, how to read it from properties and how
 * to process it.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see TableConfig
 * @see ColumnConfig
 * @see ConfigurationProcessor
 */
public class ConfigToken<T> {

	private String propertyName;
	private ConfigurationProcessor processor;

	public ConfigToken(String propertyName, ConfigurationProcessor processor) {
		this.propertyName = propertyName;
		this.processor = processor;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public ConfigurationProcessor getProcessor() {
		return processor;
	}

	@SuppressWarnings("unchecked")
	public T valueFrom(Map<ConfigToken<?>, Object> configurations) {
		return (T) configurations.get(this);
	}

	@SuppressWarnings("unchecked")
	public T valueFrom(TableConfiguration tableConfiguration) {
		if (tableConfiguration.getConfigurations() != null) {
			return (T) tableConfiguration.getConfigurations().get(this);
		}
		else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public T valueFrom(ColumnConfiguration columnConfiguration) {
		return (T) columnConfiguration.getConfigurations().get(this);
	}

	@SuppressWarnings("unchecked")
	public T valueFrom(HtmlTable table) {
		if (table.getTableConfiguration().getConfigurations() != null) {
			return (T) table.getTableConfiguration().getConfigurations().get(this);
		}
		else {
			return null;
		}
	}

	public void setIn(ColumnConfiguration columnConfiguration, T value) {
		columnConfiguration.getConfigurations().put(this, (T) value);
	}

	public void setIn(TableConfiguration tableConfiguration, T value) {
		tableConfiguration.getConfigurations().put(this, (T) value);
	}

	public void appendIn(TableConfiguration tableConfiguration, String value) {

		Object existingValue = tableConfiguration.getConfigurations().get(this);
		if (existingValue != null) {
			((StringBuilder) existingValue).append(value);
		}
		else {
			tableConfiguration.getConfigurations().put(this, new StringBuilder(value));
		}
	}

	public void toto() {

	}

	public void appendIn(TableConfiguration tableConfiguration, char value) {
		tableConfiguration.getConfigurations().put(this,
				((StringBuilder) tableConfiguration.getConfigurations().get(this)).append(value));
	}

	public void appendIn(ColumnConfiguration columnConfiguration, String value) {
		doAppendIn(columnConfiguration, value);
	}

	public void appendIn(ColumnConfiguration columnConfiguration, char value) {
		doAppendIn(columnConfiguration, String.valueOf(value));
	}

	private void doAppendIn(ColumnConfiguration columnConfiguration, String value) {
		Object existingValue = columnConfiguration.getConfigurations().get(this);
		if (StringUtils.isNotBlank(value)) {
			if (existingValue != null) {
				((StringBuilder) existingValue).append(value);
			}
			else {
				columnConfiguration.getConfigurations().put(this, new StringBuilder(value));
			}
		}
	}

	public void setIn(T value, HtmlTable table) {
		table.getTableConfiguration().getConfigurations().put(this, value);
	}

	@Override
	public String toString() {
		return "['" + propertyName + "' / " + processor.getClass().getSimpleName() + "]";
	}

}