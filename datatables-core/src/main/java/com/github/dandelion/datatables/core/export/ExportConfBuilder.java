package com.github.dandelion.datatables.core.export;


public class ExportConfBuilder {
	private ExportConf exportConf;

	public ExportConfBuilder(ExportType exportType){
		this.exportConf = new ExportConf(exportType);
	}
	
	public ExportConf includeHeader(Boolean includeHeader){
		this.exportConf.setIncludeHeader(includeHeader);
		return exportConf;
	}
}