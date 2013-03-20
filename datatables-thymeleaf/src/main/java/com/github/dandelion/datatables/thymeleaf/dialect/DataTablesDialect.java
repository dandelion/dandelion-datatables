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
package com.github.dandelion.datatables.thymeleaf.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.AttributeNameProcessorMatcher;
import org.thymeleaf.processor.IProcessor;

import com.github.dandelion.datatables.thymeleaf.matcher.ElementNameWithoutPrefixProcessorMatcher;
import com.github.dandelion.datatables.thymeleaf.processor.ColumnInitializerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.TableFinalizerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.TableInitializerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.TbodyElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.TdElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.TrElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.ajax.TablePipeSizeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.ajax.TablePipeliningAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.ajax.TableServerSideAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.ajax.TableUrlAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableAppearAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableAutoWidthAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableCdnAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableExportAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableExportLinksAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableFilterAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableInfoAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableLabelsAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TablePaginateAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TablePaginationTypeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableSortAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.ThFilterTypeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.ThFilterableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.ThSearchableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.ThSortableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyCallbackCookieProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyCallbackCreatedRowProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyCallbackDrawProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyCallbackFooterProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyCallbackFormatNumberProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyCallbackHeaderProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyCallbackInfoProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyCallbackInitProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyCallbackPreDrawProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyCallbackRowProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyExportAutoSizeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyExportFilenameAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyExportHeaderAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyExportLinkClassAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyExportLinkLabelAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyExportLinkStyleAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.ThExportFilenameAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.plugin.TheadColReorderAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.plugin.TheadFixedHeaderAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.plugin.TheadScrollerAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.theme.TableThemeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.theme.TableThemeOptionAttrProcessor;

/**
 * The Dandelion-datatables dialect.
 * 
 * @author Thibault Duchateau
 */
public class DataTablesDialect extends AbstractDialect {

	public static final String DIALECT_PREFIX = "dt";
	public static final String LAYOUT_NAMESPACE = "http://www.thymeleaf.org/dandelion/datatables";
	public static final int DT_HIGHEST_PRECEDENCE = 3500;
	
	public String getPrefix() {
		return DIALECT_PREFIX;
	}

	public boolean isLenient() {
		return false;
	}

	/*
	 * The processors.
	 */
	@Override
	public Set<IProcessor> getProcessors() {
		final Set<IProcessor> processors = new HashSet<IProcessor>();
		
		// Element processors
        for(DataTablesElProcessor processor:DataTablesElProcessor.values()) {
            processors.add(processor.getProcessor());
        }

        // Attribute processors
        for(DataTablesAttrProcessor processor:DataTablesAttrProcessor.values()) {
            processors.add(processor.getProcessor());
        }
        return processors;
	}
}
