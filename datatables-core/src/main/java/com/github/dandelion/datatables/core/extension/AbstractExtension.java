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
package com.github.dandelion.datatables.core.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.core.asset.web.AssetRequestContext;
import com.github.dandelion.datatables.core.asset.JavascriptFunction;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.asset.Parameter.Mode;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.configuration.Scope;
import com.github.dandelion.datatables.core.generator.configuration.AbstractConfigurationGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Abstract superclass for all extensions.
 * <p>
 * Lots of utilities are available in this class, allowing you to:
 * <ul>
 * <li>bufferize Javascript code before flushing it in the final
 * {@link JsResource}</li>
 * <li>add some {@link Parameter} to the generated DataTables configuration</li>
 * <li>add some {@link Scope} to the current request</li>
 * <li>add some {@link Callback} to the generated DataTables configuration</li>
 * </ul>
 * 
 * @author Thibault Duchateau
 */
public abstract class AbstractExtension implements Extension {

	private String name;
	private StringBuilder beforeAll;
	private StringBuilder beforeStartDocumentReady;
	private StringBuilder afterStartDocumentReady;
	private StringBuilder beforeEndDocumentReady;
	private StringBuilder afterAll;
	private List<Parameter> confs;
	private AbstractConfigurationGenerator configGenerator;
	private String function;
	private HtmlTable table;

	public AbstractExtension() {
		this.name = getName();
	}

	public void setupWrapper(HtmlTable table) {
		this.table = table;
		setup(table);
	}

	public abstract void setup(HtmlTable table);

	public StringBuilder getBeforeAll() {
		return beforeAll;
	}

	public StringBuilder getAfterAll() {
		return afterAll;
	}

	public StringBuilder getBeforeStartDocumentReady() {
		return beforeStartDocumentReady;
	}

	public StringBuilder getAfterStartDocumentReady() {
		return afterStartDocumentReady;
	}

	public StringBuilder getBeforeEndDocumentReady() {
		return beforeEndDocumentReady;
	}

	public List<Parameter> getParameters() {
		return confs;
	}

	public void setConfs(List<Parameter> confs) {
		this.confs = confs;
	}

	/**
	 * <p>
	 * Adds the passed {@link Parameter} to a temporary list.
	 * 
	 * <p>
	 * Later this list is processed and all {@link Parameter}s are added to the
	 * DataTables generated configuration.
	 * 
	 * @param parameter
	 *            The param to add.
	 */
	public void addParameter(Parameter parameter) {
		if (this.confs == null) {
			this.confs = new ArrayList<Parameter>();
		}
		this.confs.add(parameter);
	}

	/**
	 * <p>
	 * Adds a new DataTables parameter to the generated configuration.
	 * <p>
	 * If the parameter already exists, it will be overriden.
	 * 
	 * @param parameterName
	 *            Name of the parameter.
	 * @param parameterValue
	 *            Value of the parameter.
	 */
	public void addParameter(String parameterName, Object parameterValue) {
		addParameter(new Parameter(parameterName, parameterValue));
	}

	/**
	 * <p>
	 * Adds a new DataTables parameter to the generated configuration, using a
	 * custom method of updating.
	 * 
	 * @param parameterName
	 *            Name of the parameter.
	 * @param parameterValue
	 *            Value of the parameter.
	 * @param mode
	 *            Method of updating used for this parameter.
	 */
	public void addParameter(String parameterName, Object parameterValue, Parameter.Mode mode) {
		addParameter(new Parameter(parameterName, parameterValue, mode));
	}

	/**
	 * Updates the current {@link HttpServletRequest} with the passed
	 * {@link Scope}.
	 * 
	 * @param scope
	 *            The {@link Scope} to add.
	 */
	public void addScope(Scope scope) {
		AssetRequestContext.get(table.getTableConfiguration().getRequest()).addScopes(scope.getScopeName());
	}

	/**
	 * Updates the current {@link HttpServletRequest} with the passed
	 * {@link Scope}.
	 * 
	 * @param scopeName
	 *            The name of the {@link Scope} to add.
	 */
	public void addScope(String scopeName) {
		AssetRequestContext.get(table.getTableConfiguration().getRequest()).addScopes(scopeName);
	}

	public void addScopeParameter(String assetName, String paramName, Object paramValue) {
		AssetRequestContext.get(table.getTableConfiguration().getRequest()).addParameter(assetName, paramName,
				paramValue);
	}

	/**
	 * <p>
	 * Adds a {@link Callback} to the DataTables generated configuration.
	 * 
	 * <p>
	 * By default, if the {@link CallbackType} already exists in the
	 * configuration, the code will be appended to the existing code.
	 * 
	 * @param callbackType
	 *            The type of the callback.
	 * @param javascript
	 *            The Javascript code to execute in the callback.
	 */
	public void addCallback(CallbackType callbackType, String javascript) {
		addParameter(new Parameter(callbackType.getName(), new JavascriptFunction(javascript, callbackType.getArgs()),
				Mode.APPEND));
	}

	/**
	 * <p>
	 * Adds a {@link Callback} to the DataTables generated configuration,
	 * specifying a method of updating if the {@link CallbackType} already
	 * exists in the configuration.
	 * 
	 * @param callbackType
	 *            The type of the callback.
	 * @param javascript
	 *            The Javascript code to execute in the callback.
	 * @param mode
	 *            Method of updating used for this parameter.
	 */
	public void addCallback(CallbackType callbackType, String javascript, Mode mode) {
		addParameter(new Parameter(callbackType.getName(), new JavascriptFunction(javascript, callbackType.getArgs()),
				mode));
	}

	public AbstractConfigurationGenerator getConfigGenerator() {
		return configGenerator;
	}

	public void setConfigGenerator(AbstractConfigurationGenerator configGenerator) {
		this.configGenerator = configGenerator;
	}

	public void appendToBeforeAll(String beforeAll) {
		if (this.beforeAll == null) {
			this.beforeAll = new StringBuilder();
		}
		this.beforeAll.append(beforeAll);
	}

	public void appendToBeforeStartDocumentReady(String beforeStartDocumentReady) {
		if (this.beforeStartDocumentReady == null) {
			this.beforeStartDocumentReady = new StringBuilder();
		}
		this.beforeStartDocumentReady.append(beforeStartDocumentReady);
	}

	public void appendToAfterStartDocumentReady(String afterStartDocumentReady) {
		if (this.afterStartDocumentReady == null) {
			this.afterStartDocumentReady = new StringBuilder();
		}
		this.afterStartDocumentReady.append(afterStartDocumentReady);
	}

	public void appendToBeforeEndDocumentReady(String beforeEndDocumentReady) {
		if (this.beforeEndDocumentReady == null) {
			this.beforeEndDocumentReady = new StringBuilder();
		}
		this.beforeEndDocumentReady.append(beforeEndDocumentReady);
	}

	public void appendToAfterAll(String afterAll) {
		if (this.afterAll == null) {
			this.afterAll = new StringBuilder();
		}
		this.afterAll.append(afterAll);
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public Map<String, String> getDynamicAttributes() {
		return this.table.getDynamicAttributes();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractExtension other = (AbstractExtension) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}
}