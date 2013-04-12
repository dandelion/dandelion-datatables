package com.github.dandelion.datatables.core.html;

public class HtmlCaption extends HtmlTag{

	private String title;
	private String value = "";

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getValue(){
		return value;
	}

	public void setValue(String value){
		this.value = value;
	}

	@Override
	public StringBuffer toHtml(){

		StringBuffer html = new StringBuffer("<caption");

		if(this.id != null){
			html.append(" id=\"");
			html.append(this.id);
			html.append("\"");
		}
		if(this.cssClass != null){
			html.append(" class=\"");
			html.append(this.cssClass);
			html.append("\"");
		}
		if(this.cssStyle != null){
			html.append(" style=\"");
			html.append(this.cssStyle);
			html.append("\"");
		}
		if(this.title != null){
			html.append(" title=\"");
			html.append(this.title);
			html.append("\"");
		}
		html.append(">");
		html.append(this.value);
		html.append("</caption>");

		return html;
	}
}