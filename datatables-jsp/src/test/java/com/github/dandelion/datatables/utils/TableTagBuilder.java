package com.github.dandelion.datatables.utils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.mock.web.MockPageContext;

import com.github.dandelion.datatables.jsp.tag.ColumnTag;
import com.github.dandelion.datatables.jsp.tag.TableTag;

public class TableTagBuilder {
	private TableTag table = new TableTag();
	private LinkedList<ColumnTag> columns = new LinkedList<ColumnTag>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TableTagBuilder(Collection<?> data, String id){
		this.table.setData((Collection) data);
		this.table.setId(id);
	}
	
	public TableTagBuilder context(MockPageContext mockPageContext) {
		table.setPageContext(mockPageContext);
		return this;
	}
	
	/**
	 * Creates and returns the default table that is mostly used (5 columns).
	 * 
	 * @return a table with 5 new default columns.
	 */
	public TableTagBuilder defaultTable(){
		return column("id").column("firstName").column("lastName").column("address.town.name").column("mail");
	}
	
	public TableTagBuilder cssClass(String cssClass){
		table.setCssClass(cssClass);
		return this;
	}
	
	public TableTagBuilder cssStyle(String cssStyle){
		table.setCssStyle(cssStyle);
		return this;
	}
	
	public TableTagBuilder cssStripes(String cssStripes){
		table.setCssStripes(cssStripes);
		return this;
	}
	
	public TableTagBuilder appear(String appear){
		table.setAppear(appear);
		return this;
	}
	
	public TableTagBuilder autoWidth(Boolean auto){
		table.setAutoWidth(auto);
		return this;
	}
	
	public TableTagBuilder info(Boolean info){
		table.setInfo(info);
		return this;
	}
	
	public TableTagBuilder paginate(Boolean paginate){
		table.setPaginate(paginate);
		return this;
	}
	
	public TableTagBuilder filter(Boolean filter){
		table.setFilter(filter);
		return this;
	}
	
	public TableTagBuilder lengthChange(Boolean lengthChange){
		table.setLengthChange(lengthChange);
		return this;
	}
	
	public TableTagBuilder sort(Boolean sort){
		table.setSort(sort);
		return this;
	}
	
	public TableTagBuilder displayLength(int length){
		table.setDisplayLength(length);
		return this;
	}
	
	// Column builder
	
	public TableTagBuilder column(){
		ColumnTag columnTag = new ColumnTag();
		columnTag.setParent(table);
		this.columns.addLast(columnTag);
		return this;
	}
	
	public TableTagBuilder column(String property){
		ColumnTag columnTag = new ColumnTag();
		columnTag.setParent(table);
		columnTag.setProperty(property);
		this.columns.addLast(columnTag);
		return this;
	}
	
	public TableTagBuilder property(String property){
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