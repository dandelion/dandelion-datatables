package com.github.dandelion.datatables.integration.issues;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.integration.JspBaseIT;

/**
 * See the related issues:
 * <ul>
 * <li><a href=
 * "http://dandelion-forum.48353.x6.nabble.com/Is-there-any-way-to-store-hidden-fields-in-Datatables-td686.html"
 * >http://dandelion-forum.48353.x6.nabble.com/Is-there-any-way-to-store-hidden-
 * fields-in-Datatables-td686.html</a></li>
 * </ul>
 * 
 * @author Thibault Duchateau
 */
public class HiddenColumns extends JspBaseIT {

	@Test
	public void should_not_generate_markup_for_hidden_columns() {
		goToPage("issues/hidden_columns");

		assertThat(getTable().find("tbody").find("tr", 0).find("td")).hasSize(4);
	}
}