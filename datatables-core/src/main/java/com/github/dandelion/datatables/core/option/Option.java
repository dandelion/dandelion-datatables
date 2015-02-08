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
package com.github.dandelion.datatables.core.option;

import java.util.Map;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.generator.DatatableConfigGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.processor.OptionProcessor;

/**
 * <p>
 * Representation of a component option, which is used to configure different
 * aspects of a component.
 * </p>
 * <p>
 * Each {@link Option} is composed of:
 * </p>
 * <ul>
 * <li>a uniq {@code name}, which may be used in configuration properties</li>
 * <li>an associated {@link OptionProcessor} which indicates how the value of
 * the option should be processed</li>
 * <li>a {@code precedence} to indicate if the {@link Option} should be
 * processed before another one</li>
 * </ul>
 * <p>
 * Options are registered in multiple ways:
 * </p>
 * <ul>
 * <li>Using one or more properties files</li>
 * <li>Using a JSP taglib</li>
 * <li>Using a Thymeleaf dialect</li>
 * <li>Using the API</li>
 * </ul>
 * <p>
 * Options are usually stored in {@link Map} structures, such as
 * {@code Map<Option<?>, Object>} where {@code Object} is the value of the
 * option.
 * </p>
 * 
 * @param <T>
 *            Type of the option, crucial for the JavaScript generation. See
 *            {@link DatatableConfigGenerator}.
 * @author Thibault Duchateau
 * @since 0.11.0
 * @see OptionProcessor
 */
public class Option<T> implements Comparable<Option<T>> {

	/**
	 * Name of the option.
	 */
	private final String name;

	/**
	 * Associated option processor.
	 */
	private final OptionProcessor processor;

	/**
	 * Precedence of the option.
	 */
	private final int precedence;

	/**
	 * Optional user-defined name of the option.
	 */
	private String userName;

	/**
	 * 
	 * @param name
	 * @param processor
	 * @param precedence
	 */
	public Option(String name, OptionProcessor processor, int precedence) {
		this.name = name;
		this.processor = processor;
		this.precedence = precedence;
	}

	/**
	 * @return the name of the option.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the optional user-defined name of the option.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * TODO
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the {@link OptionProcessor} to be applied on the value associated
	 *         with the option.
	 */
	public OptionProcessor getProcessor() {
		return processor;
	}

	/**
	 * @return the precedence of the option.
	 */
	public int getPrecedence() {
		return this.precedence;
	}

	@SuppressWarnings("unchecked")
	public T valueFrom(Map<Option<?>, Object> configurations) {
		return (T) configurations.get(this);
	}

	@SuppressWarnings("unchecked")
	public T valueFrom(TableConfiguration tableConfiguration) {
		if (tableConfiguration.getConfigurations() != null && tableConfiguration.getConfigurations().containsKey(this)) {
			return (T) tableConfiguration.getConfigurations().get(this);
		}
		else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public T valueFrom(ColumnConfiguration columnConfiguration) {
		if (columnConfiguration.getConfigurations() != null) {
			return (T) columnConfiguration.getConfigurations().get(this);
		}
		else {
			return null;
		}
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
		return "[" + this.name + "|" + this.processor.getClass().getSimpleName() + "|" + this.precedence + "]";
	}

	/**
	 * <p>
	 * Compare options according to their precedence.
	 * </p>
	 * 
	 * <p>
	 * Be careful: This implementation of compareTo breaks
	 * <tt>(o1.compareTo(o2) == 0) == (o1.equals(o2))</tt>, as two different
	 * options can have the same precedence.
	 * </p>
	 * 
	 * @param o
	 *            the object to compare to
	 * @return the comparison result.
	 */
	@Override
	public int compareTo(Option<T> o) {
		if (o == null) {
			return 1;
		}
		if (!(o instanceof Option)) {
			// final int result = o.compareTo(this);
			// return (-1) * result;
			return -1;
		}
		int thisPrecedence = this.getPrecedence();
		int otherPrecedence = o.getPrecedence();
		if (thisPrecedence < otherPrecedence) {
			return -1;
		}
		if (thisPrecedence > otherPrecedence) {
			return 1;
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Option))
			return false;
		Option<?> other = (Option<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}
}