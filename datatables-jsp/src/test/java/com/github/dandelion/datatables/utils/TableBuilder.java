package com.github.dandelion.datatables.utils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.mock.web.MockPageContext;

import com.github.dandelion.datatables.jsp.tag.ColumnTag;
import com.github.dandelion.datatables.jsp.tag.TableTag;

public class TableBuilder {
	private TableTag table = new TableTag();
	private LinkedList<ColumnTag> columns = new LinkedList<ColumnTag>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TableBuilder(Collection<?> data, String id){
		this.table.setData((Collection) data);
		this.table.setId(id);
	}
	
	public TableBuilder context(MockPageContext mockPageContext) {
		table.setPageContext(mockPageContext);
		return this;
	}
	
	/**
	 * Creates and returns the default table that is mostly used (5 columns).
	 * 
	 * @return a table with 5 new default columns.
	 */
	public TableBuilder defaultTable(){
		return column("id").column("firstName").column("lastName").column("address.town.name").column("mail");
	}
	
	public TableBuilder cssClass(String cssClass){
		table.setCssClass(cssClass);
		return this;
	}
	
	public TableBuilder cssStyle(String cssStyle){
		table.setCssStyle(cssStyle);
		return this;
	}
	
	public TableBuilder cssStripes(String cssStripes){
		table.setCssStripes(cssStripes);
		return this;
	}
	
	public TableBuilder appear(String appear){
		table.setAppear(appear);
		return this;
	}
	
	public TableBuilder autoWidth(Boolean auto){
		table.setAutoWidth(auto);
		return this;
	}
	
	public TableBuilder info(Boolean info){
		table.setInfo(info);
		return this;
	}
	
	public TableBuilder paginate(Boolean paginate){
		table.setPaginate(paginate);
		return this;
	}
	
	public TableBuilder filter(Boolean filter){
		table.setFilter(filter);
		return this;
	}
	
	public TableBuilder lengthChange(Boolean lengthChange){
		table.setLengthChange(lengthChange);
		return this;
	}
	
	public TableBuilder sort(Boolean sort){
		table.setSort(sort);
		return this;
	}
	
	public TableBuilder displayLength(int length){
		table.setDisplayLength(length);
		return this;
	}
	
	// Column builder
	
	public TableBuilder column(){
		ColumnTag columnTag = new ColumnTag();
		columnTag.setParent(table);
		this.columns.addLast(columnTag);
		return this;
	}
	
	public TableBuilder column(String property){
		ColumnTag columnTag = new ColumnTag();
		columnTag.setParent(table);
		columnTag.setProperty(property);
		this.columns.addLast(columnTag);
		return this;
	}
	
	public TableBuilder property(String property){
		this.columns.getLast().setProperty(property);
		return this;
	}
	
	public TableTag getTableTag(){
		return table;
	}
	
	public List<ColumnTag> getColumnTags(){
		return columns;
	}
}