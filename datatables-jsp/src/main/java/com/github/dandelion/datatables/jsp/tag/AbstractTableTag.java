/*
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
package com.github.dandelion.datatables.jsp.tag;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.feature.AjaxFeature;
import com.github.dandelion.datatables.core.feature.FilteringFeature;
import com.github.dandelion.datatables.core.feature.JsonpFeature;
import com.github.dandelion.datatables.core.feature.PaginationType;
import com.github.dandelion.datatables.core.feature.PaginationTypeBootstrapFeature;
import com.github.dandelion.datatables.core.feature.PaginationTypeFourButtonFeature;
import com.github.dandelion.datatables.core.feature.PaginationTypeInputFeature;
import com.github.dandelion.datatables.core.feature.PaginationTypeListboxFeature;
import com.github.dandelion.datatables.core.feature.PaginationTypeScrollingFeature;
import com.github.dandelion.datatables.core.feature.PipeliningFeature;
import com.github.dandelion.datatables.core.feature.ServerSideFeature;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlLink;
import com.github.dandelion.datatables.core.html.HtmlScript;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.plugin.ColReorderPlugin;
import com.github.dandelion.datatables.core.plugin.FixedHeaderPlugin;
import com.github.dandelion.datatables.core.plugin.ScrollerPlugin;
import com.github.dandelion.datatables.core.theme.Bootstrap2Theme;
import com.github.dandelion.datatables.core.theme.JQueryUITheme;
import com.github.dandelion.datatables.core.theme.ThemeOption;
import com.github.dandelion.datatables.core.util.RequestHelper;

/**
 * <p>
 * Abstract class which contains :
 * <ul>
 * <li>all the boring technical stuff needed by Java tags (getters and setters
 * for all Table tag attributes)</li>
 * <li>helper methods used to init the table</li>
 * </ul>
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public abstract class AbstractTableTag extends BodyTagSupport {

	private static final long serialVersionUID = 4788079931487986884L;

	// Logger
	private static Logger logger = LoggerFactory.getLogger(AbstractTableTag.class);

	protected Object data;
	protected String url;
	protected String row;

	// Tag attributes
	protected String id;
	protected String cssStyle;
	protected String cssClass;
	protected String rowIdBase;
	protected String rowIdPrefix;
	protected String rowIdSufix;

	// Basic features
	protected Boolean autoWidth;
	protected Boolean filter;
	protected Boolean info;
	protected Boolean paginate;
	protected Boolean lengthChange;
	protected String paginationType;
	protected Boolean sort;
	protected Boolean cdn;
	protected String appear;
	protected String footer;

	// Advanced features
	protected Boolean deferRender;
	protected Boolean stateSave;
	protected String labels;
	protected Boolean jqueryUI;

	// Ajax
	protected Boolean processing;
	protected Boolean serverSide;
	protected Boolean pipelining;
	protected Integer pipeSize;
	protected Boolean jsonp;
	
	// Extra features
	protected Boolean fixedHeader = false;
	protected String fixedPosition;
	protected Integer fixedOffsetTop;
	protected Boolean scroller = false;
	protected String scrollY = "300px";
	protected Boolean colReorder = false;

	// Export
	protected String export;
	protected String exportLinks;

	// Theme
	protected String theme;
	protected String themeOption;

	// Internal common attributes
	protected int iterationNumber;
	protected HtmlTable table;
	protected Iterator<Object> iterator;
	protected Object currentObject;
	protected String loadingType;

	/**
	 * Register all common configuration with the table.
	 */
	protected void registerBasicConfiguration() throws BadConfigurationException {

		if (StringUtils.isNotBlank(this.cssClass)) {
			this.table.setCssClass(new StringBuffer(this.cssClass));
		}
		if (StringUtils.isNotBlank(this.cssStyle)) {
			this.table.setCssStyle(new StringBuffer(this.cssStyle));
		}
		if (this.autoWidth != null) {
			this.table.setAutoWidth(this.autoWidth);
		}
		if (this.deferRender != null) {
			this.table.setDeferRender(this.deferRender);
		}
		if (this.filter != null) {
			this.table.setFilterable(this.filter);
		}
		if (this.info != null) {
			this.table.setInfo(this.info);
		}
		if (this.paginate != null) {
			this.table.setPaginate(this.paginate);
		}
		if (this.lengthChange != null) {
			this.table.setLengthChange(this.lengthChange);
		}
		if (StringUtils.isNotBlank(this.paginationType)) {
			PaginationType paginationType = null;
			try {
				paginationType = PaginationType.valueOf(this.paginationType);
			} catch (IllegalArgumentException e) {
				logger.error("{} is not a valid value among {}", this.paginationType,
						PaginationType.values());
				throw new BadConfigurationException(e);
			}

			this.table.setPaginationType(paginationType);
		}
		if (this.processing != null) {
			this.table.setProcessing(this.processing);
		}
		if (this.serverSide != null) {
			this.table.setServerSide(this.serverSide);
		}
		if (this.sort != null) {
			this.table.setSort(this.sort);
		}
		if (this.stateSave != null) {
			this.table.setStateSave(this.stateSave);
		}
		if (this.cdn != null) {
			this.table.setCdn(this.cdn);
		}
		if (StringUtils.isNotBlank(this.labels)) {
			this.table.setLabels(RequestHelper.getBaseUrl(pageContext.getRequest()) + this.labels);
		}
		if (this.jqueryUI != null) {
			this.table.setJqueryUI(this.jqueryUI);
		}
		if(StringUtils.isNotBlank(this.appear)){
			if(this.appear.contains(",") || "fadein".equals(this.appear.toLowerCase().trim())){
				String[] tmp = this.appear.toLowerCase().trim().split(",");
				this.table.setAppear("fadein");
				if(tmp.length > 1){
					this.table.setAppearDuration(tmp[1]);
				}
			}
			else{
				this.table.setAppear("block");
			}
		}
		
		// TODO tester si la valeur vaut "header"
		if (StringUtils.isNotBlank(this.footer)) {

			for (HtmlColumn footerColumn : table.getLastHeaderRow().getColumns()) {
				table.getLastFooterRow().addColumn(footerColumn);
			}
		}
	}

	/**
	 * Register the theme if activated in the table tag.
	 */
	protected void registerTheme() throws JspException {

		if (StringUtils.isNotBlank(this.theme)) {
			if (this.theme.trim().toLowerCase().equals("bootstrap2")) {
				this.table.setTheme(new Bootstrap2Theme());
			} else if (this.theme.trim().toLowerCase().equals("jqueryui")) {
				this.table.setTheme(new JQueryUITheme());
			} else {
				logger.warn(
						"Theme {} is not recognized. Only 'bootstrap2 and jQueryUI' exists for now.",
						this.theme);
			}
		}

		if (StringUtils.isNotBlank(this.themeOption)) {
			ThemeOption themeOption = null;
			try {
				themeOption = ThemeOption.valueOf(this.themeOption.trim().toUpperCase());
			} catch (IllegalArgumentException e) {
				logger.error("{} is not a valid value among {}", this.themeOption,
						ThemeOption.values());
				throw new JspException(e);
			}

			this.table.setThemeOption(themeOption);
		}
	}

	/**
	 * Register activated plugins with the table.
	 */
	protected void registerPlugins() {

		// Modules activation
		if (this.fixedHeader) {
			logger.info("Internal module detected : fixedHeader");
			this.table.registerPlugin(new FixedHeaderPlugin());
		}

		if (this.scroller) {
			logger.info("Internal module detected : scroller");
			this.table.registerPlugin(new ScrollerPlugin());
		}

		if (this.colReorder) {
			logger.info("Internal module detected : colReorder");
			this.table.registerPlugin(new ColReorderPlugin());
		}

		// Modules configuration
		if (StringUtils.isNotBlank(this.scrollY)) {
			this.table.setScrollY(this.scrollY);
		}

		if (StringUtils.isNotBlank(this.fixedPosition)) {
			this.table.setFixedPosition(this.fixedPosition);
		}

		if (this.fixedOffsetTop != null) {
			this.table.setFixedOffsetTop(this.fixedOffsetTop);
		}
	}

	/**
	 * Register activated features with the table.
	 */
	protected void registerFeatures() throws JspException {

		// Si AJAX sans server-side
		if(StringUtils.isNotBlank(this.url) && (this.serverSide == null || !this.serverSide)){
			this.table.registerFeature(new AjaxFeature());
		}
		
		if (table.hasOneFilterableColumn()) {
			logger.info("Feature detected : select with filter");

			for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {
				table.getLastFooterRow().addColumn(column);
			}

			this.table.registerFeature(new FilteringFeature());
		}

		// Only register the feature if the paginationType attribute is set
		if (StringUtils.isNotBlank(this.paginationType)) {
			PaginationType paginationType = null;
			try {
				paginationType = PaginationType.valueOf(this.paginationType);
			} catch (IllegalArgumentException e) {
				logger.error("{} is not a valid value among {}", this.paginationType,
						PaginationType.values());
				throw new JspException(e);
			}

			switch (paginationType) {
			case bootstrap:
				this.table.registerFeature(new PaginationTypeBootstrapFeature());
				break;
			case input:
				this.table.registerFeature(new PaginationTypeInputFeature());
				break;
			case listbox:
				this.table.registerFeature(new PaginationTypeListboxFeature());
				break;
			case scrolling:
				this.table.registerFeature(new PaginationTypeScrollingFeature());
				break;
			case four_button:
				this.table.registerFeature(new PaginationTypeFourButtonFeature());
				break;
			default:
				break;

			}
		}
		
		if(this.serverSide != null && this.serverSide){
			this.table.registerFeature(new ServerSideFeature());
		}
		
		if(this.pipelining != null && this.pipelining){
			this.table.setPipelining(pipelining);
			if(pipeSize != null){
				this.table.setPipeSize(pipeSize);
			}
			this.table.registerFeature(new PipeliningFeature());
		}
		
		if(this.jsonp != null){
			this.table.setJsonp(jsonp);
			if(this.jsonp){
				this.table.registerFeature(new JsonpFeature());
			}
		}
	}

	/**
	 * <p>
	 * Register export configuration.
	 * 
	 * <p>
	 * Depending on the export configuration, export links are generated and
	 * customized around the table.
	 * 
	 * @throws BadExportConfigurationException
	 */
	protected void registerExportConfiguration() throws BadConfigurationException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		if (StringUtils.isNotBlank(this.export)) {

			// Init the exportable flag in order to add export links
			table.setIsExportable(true);

			// Allowed export types
			String[] exportTypes = this.export.trim().toUpperCase().split(",");

			for (String exportTypeString : exportTypes) {
				ExportType type = null;

				try {
					type = ExportType.valueOf(exportTypeString);
				} catch (IllegalArgumentException e) {
					logger.error("The export cannot be activated for the table {}. ", table.getId());
					logger.error("{} is not a valid value among {}", exportTypeString,
							ExportType.values());
					throw new BadConfigurationException(e);
				}

				// ExportConf eventuellement deja charges par le tag ExportTag
				// Du coup, on va completer ici avec la liste des autres exports
				// actives par la balise export=""
				if (!table.getExportConfMap().containsKey(type)) {

					String url = RequestHelper.getCurrentUrlWithParameters(request);
					if(url.contains("?")){
						url += "&";
					}
					else{
						url += "?";
					}
					url += ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE + "="
							+ type.getUrlParameter() + "&"
							+ ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID + "="
							+ this.table.getId();

					ExportConf conf = new ExportConf(type, url);
					table.getExportConfMap().put(type, conf);
				}

				// TODO ne pas prendre ne compte le tag ExportTag s'il permet de
				// customizer un export qui n'est pas specifie dans
				// export="XXXX"
			}

			// Export links position
			List<ExportLinkPosition> positionList = new ArrayList<ExportLinkPosition>();
			if (StringUtils.isNotBlank(this.exportLinks)) {
				String[] positions = this.exportLinks.trim().split(",");

				for (String position : positions) {
					try {
						positionList.add(ExportLinkPosition.valueOf(position.toUpperCase().trim()));
					} catch (IllegalArgumentException e) {
						logger.error("The export cannot be activated for the table {}. ",
								table.getId());
						logger.error("{} is not a valid value among {}", position,
								ExportLinkPosition.values());
						throw new BadConfigurationException(e);
					}
				}
			} else {
				positionList.add(ExportLinkPosition.TOP_RIGHT);
			}
			this.table.setExportLinkPositions(positionList);
		}
	}

	/**
	 * Process the iteration over the data (only for DOM source).
	 * 
	 * @return EVAL_BODY_BUFFERED if some data remain in the Java Collection,
	 *         SKIP_BODY otherwise.
	 * @throws JspException
	 *             if something went wrong during the row id generation.
	 */
	protected int processIteration() throws JspException {

		if ("DOM".equals(this.loadingType) && iterator != null && iterator.hasNext()) {

			Object object = iterator.next();

			this.setCurrentObject(object);
			table.setObjectType(object.getClass().getSimpleName());

			if (row != null) {
				pageContext.setAttribute(row, object);
			}

			String rowId = getRowId();
			if (StringUtils.isNotBlank(rowId)) {
				this.table.addRow(rowId);
			} else {
				this.table.addRow();
			}

			return EVAL_BODY_BUFFERED;
		} else {
			return SKIP_BODY;
		}
	}

	/**
	 * Return the current export type asked by the user on export link click.
	 * 
	 * @return An enum corresponding to the type of export.
	 */
	protected ExportType getCurrentExportType() {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		// Get the URL parameter used to identify the export type
		String exportTypeString = request.getParameter(
				ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE).toString();

		// Convert it to the corresponding enum
		ExportType exportType = ExportType.findByUrlParameter(Integer.parseInt(exportTypeString));

		return exportType;
	}

	/**
	 * Test if the user want his table to be exported.
	 * 
	 * @return true if the table can be exported, false otherwise.
	 */
	protected Boolean canBeExported() {
		return StringUtils.isNotBlank(this.export);
	}

	/**
	 * Return the row id using prefix, base and suffix. Prefix and sufix are
	 * just prepended and appended strings. Base is extracted from the current
	 * iterated object.
	 * 
	 * @return return the row id using prefix, base and suffix.
	 * @throws JspException
	 *             is the rowIdBase doesn't have a corresponding property
	 *             accessor method.
	 */
	protected String getRowId() throws JspException {

		StringBuffer rowId = new StringBuffer();

		if (StringUtils.isNotBlank(this.rowIdPrefix)) {
			rowId.append(this.rowIdPrefix);
		}

		if (StringUtils.isNotBlank(this.rowIdBase)) {
			try {
				Object propertyValue = PropertyUtils.getNestedProperty(this.currentObject, this.rowIdBase);
				rowId.append(propertyValue != null ? propertyValue : "");
			} catch (IllegalAccessException e) {
				logger.error("Unable to get the value for the given rowIdBase {}", this.rowIdBase);
				throw new JspException(e);
			} catch (InvocationTargetException e) {
				logger.error("Unable to get the value for the given rowIdBase {}", this.rowIdBase);
				throw new JspException(e);
			} catch (NoSuchMethodException e) {
				logger.error("Unable to get the value for the given rowIdBase {}", this.rowIdBase);
				throw new JspException(e);
			}
		}

		if (StringUtils.isNotBlank(this.rowIdSufix)) {
			rowId.append(this.rowIdSufix);
		}

		return rowId.toString();
	}

	/**
	 * Generate and write a new HTML link tag.
	 * 
	 * @param href
	 * @throws IOException
	 */
	protected void generateLinkTag(String href) throws IOException {
		pageContext.getOut().println(new HtmlLink(href).toHtml().toString());
	}

	/**
	 * Generate and write a new HTML script tag.
	 * 
	 * @param href
	 * @throws IOException
	 */
	protected void generateScriptTag(String src) throws IOException {
		pageContext.getOut().println(new HtmlScript(src).toHtml().toString());
	}

	/** Getters and setters for all attributes */

	public HtmlTable getTable() {
		return this.table;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public Boolean isFirstIteration() {
		return this.iterationNumber == 1;
	}

	public Integer getIterationNumber() {
		return this.iterationNumber;
	}

	public Object getCurrentObject() {
		return this.currentObject;
	}

	public void setCurrentObject(Object currentObject) {
		this.currentObject = currentObject;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCssStyle() {
		return this.cssStyle;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public String getCssClass() {
		return this.cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getRowIdBase() {
		return this.rowIdBase;
	}

	public void setRowIdBase(String rowIdBase) {
		this.rowIdBase = rowIdBase;
	}

	public String getRowIdPrefix() {
		return this.rowIdPrefix;
	}

	public void setRowIdPrefix(String rowIdPrefix) {
		this.rowIdPrefix = rowIdPrefix;
	}

	public String getRowIdSufix() {
		return this.rowIdSufix;
	}

	public void setRowIdSufix(String rowIdSufix) {
		this.rowIdSufix = rowIdSufix;
	}

	public Boolean getAutoWidth() {
		return autoWidth;
	}

	public void setAutoWidth(Boolean autoWidth) {
		this.autoWidth = autoWidth;
	}

	public Boolean getDeferRender() {
		return deferRender;
	}

	public void setDeferRender(Boolean deferRender) {
		this.deferRender = deferRender;
	}

	public Boolean getFilter() {
		return filter;
	}

	public void setFilter(Boolean filterable) {
		this.filter = filterable;
	}

	public Boolean getInfo() {
		return info;
	}

	public void setInfo(Boolean info) {
		this.info = info;
	}

	public Boolean getPaginate() {
		return paginate;
	}

	public void setPaginate(Boolean paginate) {
		this.paginate = paginate;
	}

	public Boolean getLengthChange() {
		return lengthChange;
	}

	public void setLengthChange(Boolean lengthChange) {
		this.lengthChange = lengthChange;
	}

	public Boolean getProcessing() {
		return processing;
	}

	public void setProcessing(Boolean processing) {
		this.processing = processing;
	}

	public Boolean getServerSide() {
		return serverSide;
	}

	public void setServerSide(Boolean serverSide) {
		this.serverSide = serverSide;
	}
	
	public String getPaginationType() {
		return paginationType;
	}

	public void setPaginationType(String paginationType) {
		this.paginationType = paginationType;
	}

	public Boolean getSort() {
		return sort;
	}

	public void setSort(Boolean sort) {
		this.sort = sort;
	}

	public Boolean getStateSave() {
		return stateSave;
	}

	public void setStateSave(Boolean stateSave) {
		this.stateSave = stateSave;
	}

	public Boolean getFixedHeader() {
		return fixedHeader;
	}

	public void setFixedHeader(Boolean fixedHeader) {
		this.fixedHeader = fixedHeader;
	}

	public Boolean getScroller() {
		return scroller;
	}

	public void setScroller(Boolean scroller) {
		this.scroller = scroller;
	}

	public Boolean getColReorder() {
		return colReorder;
	}

	public void setColReorder(Boolean colReorder) {
		this.colReorder = colReorder;
	}

	public String getScrollY() {
		return scrollY;
	}

	public void setScrollY(String scrollY) {
		this.scrollY = scrollY;
	}

	public String getFixedPosition() {
		return fixedPosition;
	}

	public void setFixedPosition(String fixedPosition) {
		this.fixedPosition = fixedPosition;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public Integer getOffsetTop() {
		return fixedOffsetTop;
	}

	public void setOffsetTop(Integer fixedOffsetTop) {
		this.fixedOffsetTop = fixedOffsetTop;
	}

	public void setCdn(Boolean cdn) {
		this.cdn = cdn;
	}

	public String getExport() {
		return export;
	}

	public void setExport(String export) {
		this.export = export;
	}

	public String getLoadingType() {
		return this.loadingType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.loadingType = "AJAX";
		this.url = url;
	}

	public Boolean getJqueryUI() {
		return jqueryUI;
	}

	public void setJqueryUI(Boolean jqueryUI) {
		this.jqueryUI = jqueryUI;
	}

	public Boolean getPipelining() {
		return pipelining;
	}

	public void setPipelining(Boolean pipelining) {
		this.pipelining = pipelining;
	}
	
	public Integer getPipeSize(){
		return pipeSize;
	}
	
	public void setPipeSize(Integer pipeSize){
		this.pipeSize = pipeSize;
	}
	
	public String getExportLinks() {
		return exportLinks;
	}

	public void setExportLinks(String exportButtons) {
		this.exportLinks = exportButtons;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getThemeOption() {
		return themeOption;
	}

	public void setThemeOption(String themeOption) {
		this.themeOption = themeOption;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getAppear() {
		return appear;
	}

	public void setAppear(String appear) {
		this.appear = appear;
	}
	
	public Boolean getJsonp(){
		return jsonp;
	}
	
	public void setJsonp(Boolean jsonp){
		this.jsonp = jsonp;
	}
	
	public void setData(Collection<Object> data) {
		this.loadingType = "DOM";
		this.data = data;

		Collection<Object> dataTmp = (Collection<Object>) data;
		if (dataTmp != null && dataTmp.size() > 0) {
			iterator = dataTmp.iterator();
		} else {
			// TODO afficher un message d'erreur
			// TODO afficher une alerte javascript
		}
	}
}