/*
 * [The "BSD licence"]
 * Copyright (c) 2012 DataTables4j
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
 * 3. Neither the name of DataTables4j nor the names of its contributors 
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
package com.github.dandelion.datatables.core.model;

import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.generator.AbstractConfigurationGenerator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * {@inheritDoc}
 */
abstract class AbstractExtension implements Extension {

	protected StringBuffer beforeAll;
	protected StringBuffer afterAll;
	protected StringBuffer beforeStartDocumentReady;
	protected StringBuffer afterStartDocumentReady;
	protected StringBuffer beforeEndDocumentReady;
	protected List<JsResource> jsResources;
	protected List<CssResource> cssResources;
	protected List<Configuration> confs;
	protected AbstractConfigurationGenerator configGenerator;
	protected Boolean appendRandomNumber = false;
	protected String function;

	/**
	 * {@inheritDoc}
	 */
	public abstract String getName();

	/**
     * {@inheritDoc}
	 */
	public abstract String getVersion();

	/**
     * {@inheritDoc}
	 */
	public abstract void setup(HtmlTable table) throws BadConfigurationException;

	public StringBuffer getBeforeAll() {
		return beforeAll;
	}

	public StringBuffer getAfterAll() {
		return afterAll;
	}

	public StringBuffer getAfterStartDocumentReady() {
		return afterStartDocumentReady;
	}

	public StringBuffer getBeforeEndDocumentReady() {
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

	public List<Configuration> getConfs() {
		return confs;
	}

	public void setConfs(List<Configuration> confs) {
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

	public void addConfiguration(Configuration conf) {
		if (this.confs == null) {
			this.confs = new ArrayList<Configuration>();
		}
		this.confs.add(conf);
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
			this.beforeAll = new StringBuffer();
		}
		this.beforeAll.append(beforeAll);
	}

	public void appendToBeforeStartDocumentReady(String beforeStartDocumentReady) {
		if (this.beforeStartDocumentReady == null) {
			this.beforeStartDocumentReady = new StringBuffer();
		}
		this.beforeStartDocumentReady.append(beforeStartDocumentReady);
	}

	public void appendToAfterStartDocumentReady(String afterStartDocumentReady) {
		if (this.afterStartDocumentReady == null) {
			this.afterStartDocumentReady = new StringBuffer();
		}
		this.afterStartDocumentReady.append(afterStartDocumentReady);
	}

	public void appendToBeforeEndDocumentReady(String beforeEndDocumentReady) {
		if (this.beforeEndDocumentReady == null) {
			this.beforeEndDocumentReady = new StringBuffer();
		}
		this.beforeEndDocumentReady.append(beforeEndDocumentReady);
	}

	public void appendToAfterAll(String afterAll) {
		if (this.afterAll == null) {
			this.afterAll = new StringBuffer();
		}
		this.afterAll.append(afterAll);
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}
}