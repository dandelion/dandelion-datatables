package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.mock.Mock;
import com.github.dandelion.datatables.mock.Person;

/**
 * >> builder <<
 *
 * @author Thibault Duchateau
 */
public class HtmlTableBuilderTest {

	private HtmlTable table;
	private MockServletContext mockServletContext;
	private MockPageContext mockPageContext;
	private HttpServletRequest request;
	
	@Before
	public void setup(){
		mockServletContext = new MockServletContext();
		mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
	}
	
	@Test
	public void should_generate_markup_using_full_source(){
		table = new HtmlTable.Builder<Person>("tableId", Mock.persons, request)
				.column("id")
				.column("firstName")
				.column("lastName")
				.column("address.town.name")
				.column("mail")
				.build();
		
		assertThat(table.getBodyRows().size()).isEqualTo(1000);
		assertThat(table.getHeadRows().get(0).getColumns().size()).isEqualTo(5);
	}
	
	@Test
	public void should_generate_markup_using_null_source(){
		table = new HtmlTable.Builder<Person>("tableId", null, request)
				.column("id")
				.column("firstName")
				.column("lastName")
				.column("address.town.name")
				.column("mail")
				.build();
		
		assertThat(table.getBodyRows().size()).isEqualTo(0);
		assertThat(table.getHeadRows().get(0).getColumns().size()).isEqualTo(5);
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\"><thead><tr><th>id</th><th>firstName</th><th>lastName</th><th>address.town.name</th><th>mail</th></tr></thead><tbody></tbody></table>");
	}
	
	
	@Test
	public void should_set_column_title(){
		table = new HtmlTable.Builder<Person>("tableId", Mock.persons, request)
				.column("id").title("Id")
				.column("firstName").title("FirstName")
				.column("lastName").title("LastName")
				.column("address.town.name").title("City")
				.column("mail").title("Mail")
				.build();
		
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(0).getTitle()).isEqualTo("Id");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(1).getTitle()).isEqualTo("FirstName");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(2).getTitle()).isEqualTo("LastName");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(3).getTitle()).isEqualTo("City");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(4).getTitle()).isEqualTo("Mail");
	}
	
	@Test
	public void should_set_default_column_title(){
		table = new HtmlTable.Builder<Person>("tableId", Mock.persons, request)
				.column("id")
				.column("firstName")
				.column("lastName")
				.column("address.town.name")
				.column("mail")
				.build();
		
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(0).getTitle()).isEqualTo("id");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(1).getTitle()).isEqualTo("firstName");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(2).getTitle()).isEqualTo("lastName");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(3).getTitle()).isEqualTo("address.town.name");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(4).getTitle()).isEqualTo("mail");
	}
}