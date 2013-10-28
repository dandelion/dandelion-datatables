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
package com.github.dandelion.datatables.core.extension.feature;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.FileUtils;

/**
 * <p>
 * Feature used in accordance with the <code>sortType</code> column attribute to
 * help DataTables to configure the sort on a column.
 * <p>
 * See <a
 * href="http://datatables.net/plug-ins/sorting">http://datatables.net/plugins
 * /sorting</a> for an overview of all available sorting functions.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class SortingFeature extends AbstractExtension {

	@Override
	public String getName() {
		return "SortingFeature";
	}

	@Override
	public void setup(HtmlTable table) throws ExtensionLoadingException {
		Set<SortType> enabledSortTypes = new HashSet<SortType>();

		for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {
			if (column.getColumnConfiguration().getSortType() != null) {
				enabledSortTypes.add(column.getColumnConfiguration().getSortType());
			}
		}

		String content = null;

		try {
			for (SortType sortType : enabledSortTypes) {
				switch (sortType) {
				case DATE:
					content = FileUtils.getFileContentFromClasspath("datatables/features/sorting/date-uk.js");
					break;
				case NATURAL:
					content = FileUtils.getFileContentFromClasspath("datatables/features/sorting/naturalSort.js");
					break;
				case ALT_STRING:
					content = FileUtils.getFileContentFromClasspath("datatables/features/sorting/alt-string.js");
					break;
				case ANTI_THE:
					content = FileUtils.getFileContentFromClasspath("datatables/features/sorting/anti-the.js");
					break;
				case CURRENCY:
					content = FileUtils.getFileContentFromClasspath("datatables/features/sorting/currency.js");
					break;
				case FILESIZE:
					content = FileUtils.getFileContentFromClasspath("datatables/features/sorting/filesize.js");
					break;
				case FORMATTED_NUMBERS:
					content = FileUtils.getFileContentFromClasspath("datatables/features/sorting/formatted-numbers.js");
					break;
				default:
					break;
				}
				appendToBeforeAll(content);
			}
		} catch (IOException e) {
			throw new ExtensionLoadingException("Unable to read the content of the file 'pipelining.js'", e);
		}
	}
}