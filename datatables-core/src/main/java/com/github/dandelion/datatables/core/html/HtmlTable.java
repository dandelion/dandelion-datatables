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
package com.github.dandelion.datatables.core.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.dandelion.datatables.core.asset.ExtraConf;
import com.github.dandelion.datatables.core.asset.ExtraFile;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.export.ExportProperties;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.feature.AbstractFeature;
import com.github.dandelion.datatables.core.feature.PaginationType;
import com.github.dandelion.datatables.core.plugin.AbstractPlugin;
import com.github.dandelion.datatables.core.properties.TableProperties;
import com.github.dandelion.datatables.core.theme.AbstractTheme;
import com.github.dandelion.datatables.core.theme.ThemeOption;

/**
 * Plain old HTML <code>table</code> tag.
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public class HtmlTable extends HtmlTag {

	// HTML attributes
	private Boolean autoWidth;
	private Boolean deferRender;
	private Boolean info;
	private Boolean filterable;
	private Boolean paginate;
	private PaginationType paginationType;
	private Boolean lengthChange;
	private Boolean sort;
	private Boolean stateSave;
	private String labels;
	private Boolean cdn;
	private Boolean jqueryUI;
	private String appear;
	private String appearDuration;
	private String lengthMenu;
	private String stripeClasses;
	
	// Ajax
	private Boolean processing;
	private Boolean serverSide;
	private String ajaxSource;
	private Boolean pipelining;
	private int pipeSize;
	private Boolean jsonp;
	private String serverData;
	private String serverParam;
	private String serverMethod;
	
	// Extra features
	private String scrollY;
	private String fixedPosition;
	private Integer fixedOffsetTop;
	private List<Callback> callbacks;

	// Internal attributes
	private List<HtmlRow> head = new LinkedList<HtmlRow>();
	private List<HtmlRow> body = new LinkedList<HtmlRow>();
	private List<HtmlRow> foot = new LinkedList<HtmlRow>();
	private TableProperties tableProperties = new TableProperties();
	private String datasourceUrl;
	private List<AbstractPlugin> plugins;
	private List<AbstractFeature> features;
	private List<ExtraFile> extraFiles;
	private List<ExtraConf> extraConfs;
	private String randomId;
	
	// Class of the iterated objects. Only used in XML export.
	private String objectType;
	private String currentUrl;

	// Export
	private ExportProperties exportProperties;
	private Boolean exporting;
	private Map<ExportType, ExportConf> exportConfMap = new HashMap<ExportType, ExportConf>();
	private List<ExportLinkPosition> exportLinkPositions;
	private Boolean isExportable = false;

	// Theme
	private AbstractTheme theme;
	private ThemeOption themeOption;

	public HtmlTable(String id, String randomId) {
		init();
		this.id = id;
		this.randomId = randomId;
	}

	/**
	 * Initialize the default values.
	 */
	private void init(){
		// Basic attributes
		this.cdn = false;
		
		// Export
		this.isExportable = false;
		
		// Export links position
		List<ExportLinkPosition> exportLinkPositions = new ArrayList<ExportLinkPosition>();
		exportLinkPositions.add(ExportLinkPosition.TOP_RIGHT);
		this.exportLinkPositions = exportLinkPositions;
		
		// AJAX
		this.pipeSize = 5;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringBuffer toHtml() {
		StringBuffer html = new StringBuffer();

		if(this.appear != null && !"".equals(this.appear)){
			html.append("<table style=\"display:none;\" id=\"");
		}
		else{
			html.append("<table id=\"");
		}
		html.append(this.id);
		html.append("\"");

		if (this.cssClass != null) {
			html.append(" class=\"");
			html.append(this.cssClass);
			html.append("\"");
		}

		if (this.cssStyle != null) {
			html.append(" style=\"");
			html.append(this.cssStyle);
			html.append("\"");
		}

		html.append(">");
		html.append("<thead>");

		for (HtmlRow row : this.head) {
			html.append(row.toHtml());
		}
		html.append("</thead>");
		html.append("<tbody>");

		for (HtmlRow row : this.body) {
			html.append(row.toHtml());
		}

		html.append("</tbody>");

		if (!this.foot.isEmpty()) {
			html.append("<tfoot>");
			for (HtmlRow row : this.foot) {
				html.append(row.toHtml());
			}

			html.append("</tfoot>");
		}
		html.append("</table>");
		
		return html;
	}

	public List<HtmlRow> getHeadRows() {
		return head;
	}

	public List<HtmlRow> getBodyRows() {
		return body;
	}

	public HtmlRow addHeaderRow() {
		HtmlRow row = new HtmlRow();
		this.head.add(row);
		return row;
	}

	public HtmlRow addRow() {
		HtmlRow row = new HtmlRow();
		this.body.add(row);
		return row;
	}

	public HtmlRow addFooterRow() {
		HtmlRow row = new HtmlRow();
		this.foot.add(row);
		return row;
	}

	public HtmlRow addRow(String rowId) {
		HtmlRow row = new HtmlRow(rowId);
		this.body.add(row);
		return row;
	}

	public HtmlTable addRows(HtmlRow... rows) {
		for (HtmlRow row : rows) {
			this.body.add(row);
		}
		return this;
	}

	public HtmlRow getLastFooterRow() {
		return ((LinkedList<HtmlRow>) this.foot).getLast();
	}

	public HtmlRow getLastHeaderRow() {
		return ((LinkedList<HtmlRow>) this.head).getLast();
	}

	public HtmlRow getLastBodyRow() {
		return ((LinkedList<HtmlRow>) this.body).getLast();
	}

	/**
	 * Register a plugin to the table.
	 * 
	 * @param plugin
	 *            The plugin to activate.
	 */
	public void registerPlugin(AbstractPlugin plugin) {
		if (this.plugins == null) {
			this.plugins = new ArrayList<AbstractPlugin>();
		}
		this.plugins.add(plugin);
	}

	/**
	 * Register a feature to the table.
	 * 
	 * @param feature
	 *            The feature to activate.
	 */
	public void registerFeature(AbstractFeature feature) {
		if (this.features == null) {
			this.features = new ArrayList<AbstractFeature>();
		}
		this.features.add(feature);
	}

	/**
	 * Returns <code>true</code> if the table has one filterable column,
	 * <code>false</code> otherwise. This way, the {@link FilteringFeature} will
	 * be activated or not.
	 * 
	 * @return <code>true</code> if the table has one filterable column,
	 *         <code>false</code> otherwise
	 */
	public Boolean hasOneFilterableColumn() {

		Boolean retval = false;

		for (HtmlRow headerRow : this.head) {
			for (HtmlColumn headerColumn : headerRow.getHeaderColumns()) {
				if (headerColumn.isFilterable() != null && headerColumn.isFilterable()) {
					retval = true;
					break;
				}
			}
		}

		return retval;
	}

	public HtmlColumn getColumnHeadByUid(String uid) {
		for(HtmlRow row : this.head){
			for(HtmlColumn column : row.getColumns()){
				if(column.isHeaderColumn() != null && column.isHeaderColumn() && column.getUid() != null && column.getUid().equals(uid)){
					return column;
				}
			}
			
		}
		return null;
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

	public Boolean getInfo() {
		return info;
	}

	public void setInfo(Boolean info) {
		this.info = info;
	}

	public Boolean getFilterable() {
		return filterable;
	}

	public void setFilterable(Boolean filterable) {
		this.filterable = filterable;
	}

	public Boolean getPaginate() {
		return paginate;
	}

	public void setPaginate(Boolean paginate) {
		this.paginate = paginate;
	}

	public PaginationType getPaginationType() {
		return paginationType;
	}

	public void setPaginationType(PaginationType paginationStyle) {
		this.paginationType = paginationStyle;
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

	public String getAjaxSource() {
		return ajaxSource;
	}

	public void setAjaxSource(String ajaxSource) {
		this.ajaxSource = ajaxSource;
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

	public String getDatasourceUrl() {
		return datasourceUrl;
	}

	public void setDatasourceUrl(String datasourceUrl) {
		this.datasourceUrl = datasourceUrl;
	}

	public String getScrollY() {
		return scrollY;
	}

	public void setScrollY(String scrollY) {
		this.scrollY = scrollY;
	}

	public List<ExtraFile> getExtraFiles() {
		return extraFiles;
	}

	public void addExtraFile(ExtraFile extraFile){
		if(this.extraFiles == null){
			this.extraFiles = new ArrayList<ExtraFile>();
		}
		this.extraFiles.add(extraFile);
	}

	public List<ExtraConf> getExtraConfs() {
		return extraConfs;
	}

	public void addExtraConf(ExtraConf extraConf){
		if(this.extraConfs == null){
			this.extraConfs = new ArrayList<ExtraConf>();
		}
		this.extraConfs.add(extraConf);
	}

	public List<AbstractPlugin> getPlugins() {
		return plugins;
	}

	public void setPlugins(List<AbstractPlugin> plugins) {
		this.plugins = plugins;
	}

	public List<AbstractFeature> getFeatures() {
		return features;
	}

	public void setFeatures(List<AbstractFeature> features) {
		this.features = features;
	}

	public String getFixedPosition() {
		return fixedPosition;
	}

	public void setFixedPosition(String fixedPosition) {
		this.fixedPosition = fixedPosition;
	}

	public Boolean getCdn() {
		return cdn;
	}

	public void setCdn(Boolean cdn) {
		this.cdn = cdn;
	}

	public Integer getFixedOffsetTop() {
		return fixedOffsetTop;
	}

	public void setFixedOffsetTop(Integer fixedOffsetTop) {
		this.fixedOffsetTop = fixedOffsetTop;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public String getRandomId() {
		return randomId;
	}

	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}

	public Boolean getJqueryUI() {
		return jqueryUI;
	}

	public void setJqueryUI(Boolean jqueryUI) {
		this.jqueryUI = jqueryUI;
	}

	public TableProperties getTableProperties() {
		return tableProperties;
	}

	public void setTableProperties(TableProperties tableProperties) {
		this.tableProperties = tableProperties;
	}

	public Boolean getExporting() {
		return exporting;
	}

	public void setExporting(Boolean exporting) {
		this.exporting = exporting;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public ExportProperties getExportProperties() {
		return exportProperties;
	}

	public void setExportProperties(ExportProperties exportProperties) {
		this.exportProperties = exportProperties;
	}

	public Map<ExportType, ExportConf> getExportConfMap() {
		return exportConfMap;
	}

	public void setExportConfMap(Map<ExportType, ExportConf> exportConfs) {
		this.exportConfMap = exportConfs;
	}

	public Boolean isExportable() {
		return isExportable;
	}

	public void setIsExportable(Boolean isExportable) {
		this.isExportable = isExportable;
	}

	public List<ExportLinkPosition> getExportLinkPositions() {
		return exportLinkPositions;
	}

	public void setExportLinkPositions(List<ExportLinkPosition> exportLinkPositions) {
		this.exportLinkPositions = exportLinkPositions;
	}

	public String getCurrentUrl() {
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}

	public AbstractTheme getTheme() {
		return theme;
	}

	public void setTheme(AbstractTheme theme) {
		this.theme = theme;
	}

	public ThemeOption getThemeOption() {
		return themeOption;
	}

	public void setThemeOption(ThemeOption themeOption) {
		this.themeOption = themeOption;
	}
	
	public String getAppear() {
		return appear;
	}

	public void setAppear(String appear) {
		this.appear = appear;
	}

	public String getAppearDuration() {
		return appearDuration;
	}

	public void setAppearDuration(String appearDuration) {
		this.appearDuration = appearDuration;
	}

	public Boolean getPipelining() {
		return pipelining;
	}

	public void setPipelining(Boolean pipelining) {
		this.pipelining = pipelining;
	}

	public int getPipeSize() {
		return pipeSize;
	}

	public void setPipeSize(int pipeSize) {
		this.pipeSize = pipeSize;
	}
	
	public Boolean getJsonp() {
		return jsonp;
	}

	public void setJsonp(Boolean jsonp) {
		this.jsonp = jsonp;
	}

	public List<Callback> getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(List<Callback> callbacks) {
		this.callbacks = callbacks;
	}
	
	public void registerCallback(Callback callback){
		if (this.callbacks == null) {
			this.callbacks = new ArrayList<Callback>();
		}
		this.callbacks.add(callback);
	}

	public String getLengthMenu() {
		return lengthMenu;
	}

	public void setLengthMenu(String lengthMenu) {
		this.lengthMenu = lengthMenu;
	}

	public String getStripeClasses() {
		return stripeClasses;
	}

	public void setStripeClasses(String stripeClasses) {
		this.stripeClasses = stripeClasses;
	}

	public String getServerData() {
		return serverData;
	}

	public void setServerData(String serverData) {
		this.serverData = serverData;
	}

	public String getServerParam() {
		return serverParam;
	}

	public void setServerParam(String serverParam) {
		this.serverParam = serverParam;
	}

	public String getServerMethod() {
		return serverMethod;
	}

	public void setServerMethod(String serverMethod) {
		this.serverMethod = serverMethod;
	}

	@Override
	public String toString() {
		return "HtmlTable [autoWidth=" + autoWidth + ", deferRender=" + deferRender + ", info="
				+ info + ", filterable=" + filterable + ", paginate=" + paginate
				+ ", paginationType=" + paginationType + ", lengthChange=" + lengthChange
				+ ", sort=" + sort + ", stateSave=" + stateSave + ", labels=" + labels + ", cdn="
				+ cdn + ", jqueryUI=" + jqueryUI + ", appear=" + appear + ", appearDuration="
				+ appearDuration + ", lengthMenu=" + lengthMenu + ", stripeClasses="
				+ stripeClasses + ", processing=" + processing + ", serverSide=" + serverSide
				+ ", ajaxSource=" + ajaxSource + ", pipelining=" + pipelining + ", pipeSize="
				+ pipeSize + ", jsonp=" + jsonp + ", serverData=" + serverData + ", serverParam="
				+ serverParam + ", serverMethod=" + serverMethod + ", scrollY=" + scrollY
				+ ", fixedPosition=" + fixedPosition + ", fixedOffsetTop=" + fixedOffsetTop
				+ ", callbacks=" + callbacks + ", head=" + head + ", body=" + body + ", foot="
				+ foot + ", tableProperties=" + tableProperties + ", datasourceUrl="
				+ datasourceUrl + ", plugins=" + plugins + ", features=" + features
				+ ", extraFiles=" + extraFiles + ", extraConfs=" + extraConfs + ", randomId="
				+ randomId + ", objectType=" + objectType + ", currentUrl=" + currentUrl
				+ ", exportProperties=" + exportProperties + ", exporting=" + exporting
				+ ", exportConfMap=" + exportConfMap + ", exportLinkPositions="
				+ exportLinkPositions + ", isExportable=" + isExportable + ", theme=" + theme
				+ ", themeOption=" + themeOption + "]";
	}
}