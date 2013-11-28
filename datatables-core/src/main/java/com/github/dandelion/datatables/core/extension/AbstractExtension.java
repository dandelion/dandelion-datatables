/*
 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
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

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.datatables.core.asset.JavascriptFunction;
import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.asset.Parameter.Mode;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.configuration.Scope;
import com.github.dandelion.datatables.core.generator.configuration.AbstractConfigurationGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Abstract superclass for all extensions.
 * 
 * @author Thibault Duchateau
 */
public abstract class AbstractExtension implements Extension {

	protected String name;
	protected StringBuilder beforeAll;
	protected StringBuilder afterAll;
	protected StringBuilder beforeStartDocumentReady;
	protected StringBuilder afterStartDocumentReady;
	protected StringBuilder beforeEndDocumentReady;
	protected List<Parameter> confs;
	protected AbstractConfigurationGenerator configGenerator;
	protected Boolean appendRandomNumber = false;
	protected String function;
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

	public StringBuilder getAfterStartDocumentReady() {
		return afterStartDocumentReady;
	}

	public StringBuilder getBeforeEndDocumentReady() {
		return beforeEndDocumentReady;
	}

	public List<Parameter> getConfs() {
		return confs;
	}

	public void setConfs(List<Parameter> confs) {
		this.confs = confs;
	}

	public void addParameter(Parameter parameter) {
		if (this.confs == null) {
			this.confs = new ArrayList<Parameter>();
		}
		this.confs.add(parameter);
	}

	public void addParameter(String parameterName) {
		addParameter(new Parameter(parameterName));
	}

	public void addParameter(String parameterName, Object parameterValue) {
		addParameter(new Parameter(parameterName, parameterValue));
	}

	public void addParameter(String parameterName, Object parameterValue, Parameter.Mode mode) {
		addParameter(new Parameter(parameterName, parameterValue, mode));
	}

	public void addScope(Scope scope) {
		AssetsRequestContext.get(table.getTableConfiguration().getRequest()).addScopes(scope.getScopeName());
	}
	
	public void addScope(String scopeName) {
		AssetsRequestContext.get(table.getTableConfiguration().getRequest()).addScopes(scopeName);
	}

	public void addScopeParameter(String assetName, String paramName, Object paramValue) {
		AssetsRequestContext.get(table.getTableConfiguration().getRequest())
			.addParameter(assetName, paramName, paramValue);
	}
	
	public void addCallback(CallbackType callbackType, String javascript) {
		addParameter(new Parameter(callbackType.getName(), new JavascriptFunction(javascript, callbackType.getArgs()),
				Mode.APPEND));

	}

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

	public Boolean getAppendRandomNumber() {
		return appendRandomNumber;
	}

	public void setAppendRandomNumber(Boolean appendRandomNumber) {
		this.appendRandomNumber = appendRandomNumber;
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