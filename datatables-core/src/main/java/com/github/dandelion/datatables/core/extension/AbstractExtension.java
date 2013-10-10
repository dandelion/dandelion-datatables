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
import java.util.LinkedList;
import java.util.List;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.datatables.core.asset.CssResource;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.generator.AbstractConfigurationGenerator;
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
	protected List<JsResource> jsResources;
	protected List<CssResource> cssResources;
	protected List<Parameter> confs;
	protected AbstractConfigurationGenerator configGenerator;
	protected Boolean appendRandomNumber = false;
	protected String function;
	private HtmlTable table;

	public AbstractExtension(){
		this.name = getName();
	}
	
	public void setupWrapper(HtmlTable table) throws ExtensionLoadingException {
		this.table = table;
		setup(table);
	}
	
	public abstract void setup(HtmlTable table) throws ExtensionLoadingException;
	
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

	public List<JsResource> getJsResources() {
		return jsResources;
	}

	public void setJsResources(List<JsResource> jsResources) {
		this.jsResources = jsResources;
	}

	public List<CssResource> getCssResources() {
		return cssResources;
	}

	public void setCssResources(List<CssResource> cssResources) {
		this.cssResources = cssResources;
	}

	public List<Parameter> getConfs() {
		return confs;
	}

	public void setConfs(List<Parameter> confs) {
		this.confs = confs;
	}

	public void addJsResource(JsResource resource) {
		if (this.jsResources == null) {
			this.jsResources = new LinkedList<JsResource>();
		}
		this.jsResources.add(resource);
	}

	public void addCssResource(CssResource resource) {
		if (this.cssResources == null) {
			this.cssResources = new LinkedList<CssResource>();
		}
		this.cssResources.add(resource);
	}

	public void addParameter(Parameter parameter) {
		if (this.confs == null) {
			this.confs = new ArrayList<Parameter>();
		}
		this.confs.add(parameter);
	}

	public void addScope(String scopeName){
		AssetsRequestContext.get(table.getTableConfiguration().getRequest()).addScopes(scopeName);
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
		result = prime * result + ((afterAll == null) ? 0 : afterAll.hashCode());
		result = prime * result + ((afterStartDocumentReady == null) ? 0 : afterStartDocumentReady.hashCode());
		result = prime * result + ((appendRandomNumber == null) ? 0 : appendRandomNumber.hashCode());
		result = prime * result + ((beforeAll == null) ? 0 : beforeAll.hashCode());
		result = prime * result + ((beforeEndDocumentReady == null) ? 0 : beforeEndDocumentReady.hashCode());
		result = prime * result + ((beforeStartDocumentReady == null) ? 0 : beforeStartDocumentReady.hashCode());
		result = prime * result + ((configGenerator == null) ? 0 : configGenerator.hashCode());
		result = prime * result + ((confs == null) ? 0 : confs.hashCode());
		result = prime * result + ((cssResources == null) ? 0 : cssResources.hashCode());
		result = prime * result + ((function == null) ? 0 : function.hashCode());
		result = prime * result + ((jsResources == null) ? 0 : jsResources.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AbstractExtension other = (AbstractExtension) obj;
		if (afterAll == null) {
			if (other.afterAll != null) return false;
		} else if (!afterAll.equals(other.afterAll)) return false;
		if (afterStartDocumentReady == null) {
			if (other.afterStartDocumentReady != null) return false;
		} else if (!afterStartDocumentReady.equals(other.afterStartDocumentReady)) return false;
		if (appendRandomNumber == null) {
			if (other.appendRandomNumber != null) return false;
		} else if (!appendRandomNumber.equals(other.appendRandomNumber)) return false;
		if (beforeAll == null) {
			if (other.beforeAll != null) return false;
		} else if (!beforeAll.equals(other.beforeAll)) return false;
		if (beforeEndDocumentReady == null) {
			if (other.beforeEndDocumentReady != null) return false;
		} else if (!beforeEndDocumentReady.equals(other.beforeEndDocumentReady)) return false;
		if (beforeStartDocumentReady == null) {
			if (other.beforeStartDocumentReady != null) return false;
		} else if (!beforeStartDocumentReady.equals(other.beforeStartDocumentReady)) return false;
		if (configGenerator == null) {
			if (other.configGenerator != null) return false;
		} else if (!configGenerator.equals(other.configGenerator)) return false;
		if (confs == null) {
			if (other.confs != null) return false;
		} else if (!confs.equals(other.confs)) return false;
		if (cssResources == null) {
			if (other.cssResources != null) return false;
		} else if (!cssResources.equals(other.cssResources)) return false;
		if (function == null) {
			if (other.function != null) return false;
		} else if (!function.equals(other.function)) return false;
		if (jsResources == null) {
			if (other.jsResources != null) return false;
		} else if (!jsResources.equals(other.jsResources)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}
	
	
}