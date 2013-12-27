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
package com.github.dandelion.datatables.thymeleaf.processor.el;

import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractElProcessor;
import com.github.dandelion.datatables.thymeleaf.util.AttributeUtils;

/**
 * TODO
 * 
 * @author Thibault Duchateau
 */
public class ColumnInitializerElProcessor extends AbstractElProcessor {

	public ColumnInitializerElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 8000;
	}

	@Override
	protected ProcessorResult doProcessElement(Arguments arguments, Element element) {

		Map<ConfigToken<?>, Object> stagingConf = new HashMap<ConfigToken<?>, Object>();
		Map<ConfigToken<?>, Extension> stagingExtension = new HashMap<ConfigToken<?>, Extension>();

		// AJAX sources require to set dt:property attribute
		// This attribute is processed here, before being removed
		if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":property")) {
			stagingConf.put(
					ColumnConfig.PROPERTY,
					AttributeUtils.parseStringAttribute(arguments, element, DataTablesDialect.DIALECT_PREFIX
							+ ":property"));
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":property");
		}

		if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":renderFunction")) {
			stagingConf.put(
					ColumnConfig.RENDERFUNCTION,
					AttributeUtils.parseStringAttribute(arguments, element, DataTablesDialect.DIALECT_PREFIX
							+ ":renderFunction"));
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":renderFunction");
		}

		if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":default")) {
			stagingConf.put(
					ColumnConfig.DEFAULTVALUE,
					AttributeUtils.parseStringAttribute(arguments, element, DataTablesDialect.DIALECT_PREFIX
							+ ":default"));
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":default");
		} else {
			stagingConf.put(ColumnConfig.DEFAULTVALUE, "");
		}

		// The staging configuration is stored as a local variable. It must be
		// accessible in all column head processors.
		Map<String, Object> newVariable = new HashMap<String, Object>();
		newVariable.put(DataTablesDialect.INTERNAL_BEAN_COLUMN_LOCAL_CONF, stagingConf);
		newVariable.put(DataTablesDialect.INTERNAL_BEAN_COLUMN_LOCAL_EXT, stagingExtension);
		return ProcessorResult.setLocalVariables(newVariable);
	}
}