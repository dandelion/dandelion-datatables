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
package com.github.dandelion.datatables.core.processor;

import java.util.Map;

import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;

/**
 * <p>
 * Common abstract superclass for all processors.
 * <p>
 * All processors contain the actual processing applied on each Datatables
 * configuration.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public abstract class AbstractProcessor {

	/**
	 * Processes the passed param and returns the right type needed by the
	 * {@link TableConfiguration} to initialize the configuration.
	 * 
	 * @param param
	 *            The parameter to process. This is always a String because it
	 *            can come from properties file, JSP tag attributes or Thymeleaf
	 *            attributes.
	 * @param tableConfiguration
	 *            The {@link TableConfiguration} object may be used to
	 *            initialize other configurations (e.g. register a new
	 *            AbstractFeature).
	 * @param confToBeApplied
	 *            The global configuration to be applied on the
	 *            {@link TableConfiguration} may be useful to initialize linked
	 *            configurations. For example, the
	 *            {@link com.github.dandelion.datatables.core.feature.AjaxFeature}
	 *            must be registered in the TableConfiguration only if
	 *            server-side processing in not enabled.
	 * @return the object needed by one of the setter of the
	 *         {@link TableConfiguration} object.
	 */
	public abstract Object process(String param, TableConfiguration tableConfiguration, Map<Configuration, Object> confToBeApplied);
}