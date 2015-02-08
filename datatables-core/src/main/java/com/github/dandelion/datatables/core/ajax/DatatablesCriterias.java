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
package com.github.dandelion.datatables.core.ajax;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.core.util.Validate;
import com.github.dandelion.datatables.core.ajax.ColumnDef.SortDirection;
import com.github.dandelion.datatables.core.generator.DTConstants;

/**
 * <p>
 * POJO that wraps all the parameters sent by Datatables to the server when
 * server-side processing is enabled. This bean can then be used to build SQL
 * queries.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.8.2
 */
public class DatatablesCriterias implements Serializable {

	private static final long serialVersionUID = 8661357461501153387L;

	private static final Logger LOG = LoggerFactory.getLogger(DatatablesCriterias.class);

	private static Pattern pattern = Pattern.compile("columns\\[([0-9]*)?\\]");
	private final String search;
	private final Integer start;
	private final Integer length;
	private final List<ColumnDef> columnDefs;
	private final List<ColumnDef> sortingColumnDefs;
	private final Integer draw;

	private DatatablesCriterias(String search, Integer displayStart, Integer displaySize, List<ColumnDef> columnDefs,
			List<ColumnDef> sortingColumnDefs, Integer draw) {
		this.search = search;
		this.start = displayStart;
		this.length = displaySize;
		this.columnDefs = columnDefs;
		this.sortingColumnDefs = sortingColumnDefs;
		this.draw = draw;
	}

	public Integer getStart() {
		return start;
	}

	public Integer getLength() {
		return length;
	}

	public String getSearch() {
		return search;
	}

	public Integer getDraw() {
		return draw;
	}

	public List<ColumnDef> getColumnDefs() {
		return columnDefs;
	}

	public List<ColumnDef> getSortingColumnDefs() {
		return sortingColumnDefs;
	}

	/**
	 * @return true if a column is filterable, false otherwise.
	 */
	public Boolean hasOneFilterableColumn() {
		for (ColumnDef columnDef : this.columnDefs) {
			if (columnDef.isFilterable()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true if a column is being filtered, false otherwise.
	 */
	public Boolean hasOneFilteredColumn() {
		for (ColumnDef columnDef : this.columnDefs) {
			if (StringUtils.isNotBlank(columnDef.getSearch()) || StringUtils.isNotBlank(columnDef.getSearchFrom())
					|| StringUtils.isNotBlank(columnDef.getSearchTo())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true if a column is being sorted, false otherwise.
	 */
	public Boolean hasOneSortedColumn() {
		return !sortingColumnDefs.isEmpty();
	}

	/**
	 * <p>
	 * Map all request parameters into a wrapper POJO that eases SQL querying.
	 * </p>
	 * 
	 * @param request
	 *            The request sent by Datatables containing all parameters.
	 * @return a wrapper POJO.
	 */
	public static DatatablesCriterias getFromRequest(HttpServletRequest request) {

		Validate.notNull(request, "The HTTP request cannot be null");

		int columnNumber = getColumnNumber(request);
		LOG.trace("Number of columns: {}", columnNumber);

		String paramSearch = request.getParameter(DTConstants.DT_S_SEARCH);
		String paramDraw = request.getParameter(DTConstants.DT_I_DRAW);
		String paramStart = request.getParameter(DTConstants.DT_I_START);
		String paramLength = request.getParameter(DTConstants.DT_I_LENGTH);

		Integer draw = StringUtils.isNotBlank(paramDraw) ? Integer.parseInt(paramDraw) : -1;
		Integer start = StringUtils.isNotBlank(paramStart) ? Integer.parseInt(paramStart) : -1;
		Integer length = StringUtils.isNotBlank(paramLength) ? Integer.parseInt(paramLength) : -1;

		// Column definitions
		List<ColumnDef> columnDefs = new ArrayList<ColumnDef>();

		for (int i = 0; i < columnNumber; i++) {

			ColumnDef columnDef = new ColumnDef();

			columnDef.setName(request.getParameter("columns[" + i + "][data]"));
			columnDef.setFilterable(Boolean.parseBoolean(request.getParameter("columns[" + i + "][searchable]")));
			columnDef.setSortable(Boolean.parseBoolean(request.getParameter("columns[" + i + "][orderable]")));
			columnDef.setRegex(request.getParameter("columns[" + i + "][search][regex]"));
			
			String searchTerm = request.getParameter("columns[" + i + "][search][value]");

			if (StringUtils.isNotBlank(searchTerm)) {
				columnDef.setFiltered(true);
				String[] splittedSearch = searchTerm.split("~");
				if ("~".equals(searchTerm)) {
					columnDef.setSearch("");
				}
				else if (searchTerm.startsWith("~")) {
					columnDef.setSearchTo(splittedSearch[1]);
				}
				else if (searchTerm.endsWith("~")) {
					columnDef.setSearchFrom(splittedSearch[0]);
				}
				else if (searchTerm.contains("~")) {
					columnDef.setSearchFrom(splittedSearch[0]);
					columnDef.setSearchTo(splittedSearch[1]);
				}
				else {
					columnDef.setSearch(searchTerm);
				}
			}

			columnDefs.add(columnDef);
		}

		// Sorted column definitions
		List<ColumnDef> sortingColumnDefs = new LinkedList<ColumnDef>();

		for (int i = 0; i < columnNumber; i++) {
			String paramSortedCol = request.getParameter("order[" + i + "][column]");

			// The column is being sorted
			if (StringUtils.isNotBlank(paramSortedCol)) {
				Integer sortedCol = Integer.parseInt(paramSortedCol);
				ColumnDef sortedColumnDef = columnDefs.get(sortedCol);
				String sortedColDirection = request.getParameter("order[" + i + "][dir]");
				if (StringUtils.isNotBlank(sortedColDirection)) {
					sortedColumnDef.setSortDirection(SortDirection.valueOf(sortedColDirection.toUpperCase()));
				}

				sortingColumnDefs.add(sortedColumnDef);
			}
		}

		return new DatatablesCriterias(paramSearch, start, length, columnDefs, sortingColumnDefs, draw);
	}

	private static int getColumnNumber(HttpServletRequest request) {

		int columnNumber = 0;
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String param = e.nextElement();
			Matcher matcher = pattern.matcher(param);
			while (matcher.find()) {
				Integer col = Integer.parseInt(matcher.group(1));
				if (col > columnNumber) {
					columnNumber = col;
				}
			}
		}

		if (columnNumber != 0) {
			columnNumber++;
		}
		return columnNumber;
	}

	@Override
	public String toString() {
		return "DatatablesCriterias [search=" + search + ", start=" + start + ", length=" + length + ", columnDefs="
				+ columnDefs + ", sortingColumnDefs=" + sortingColumnDefs + ", draw=" + draw + "]";
	}
}