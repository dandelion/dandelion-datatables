package com.github.dandelion.datatables.core.html;

public class HtmlCaption extends HtmlTagWithContent {

	private String title;
	
	public HtmlCaption(){
		tag = "caption";
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	@Override
	protected StringBuffer getHtmlAttributes() {
		StringBuffer html = super.getHtmlAttributes();
		html.append(writeAttribute("title", this.title));
		return html;
	}
}