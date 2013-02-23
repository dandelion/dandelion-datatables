package com.github.dandelion.datatables.jsp.tag;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.github.dandelion.datatables.jsp.DomPhantomJsTest;
import com.github.dandelion.datatables.utils.Mock;

/**
 * Test the basic Features of Dandelion-Datatables.
 *
 * @author Thibault Duchateau
 */
public class BasicsTest extends DomPhantomJsTest {

	@Test
	public void should_disable_paging() throws IOException, Exception {
		goTo("/basicFeatures/table_paging.jsp");

		assertThat(find("#" + TABLE_ID + "_length")).hasSize(0);
		
		// If paging is disabled, the entire collection is displayed
		assertThat(getTable().find("tbody").find("tr")).hasSize(Mock.persons.size());
	}
	
	@Test
	public void should_disable_filtering() throws IOException, Exception {
		goTo("/basicFeatures/table_filtering.jsp");

		// If paging is disabled, the entire collection is displayed
		assertThat(find("#" + TABLE_ID + "_filter")).hasSize(0);
	}
	
	@Test
	public void should_disable_info() throws IOException, Exception {
		goTo("/basicFeatures/table_info.jsp");

		// If paging is disabled, the entire collection is displayed
		assertThat(find("#" + TABLE_ID + "_info")).hasSize(0);
	}
	
	@Test
	public void should_disable_length_changing() throws IOException, Exception {
		goTo("/basicFeatures/table_lengthChange.jsp");

		// If paging is disabled, the entire collection is displayed
		assertThat(find("#" + TABLE_ID + "_length")).hasSize(0);
	}
}
