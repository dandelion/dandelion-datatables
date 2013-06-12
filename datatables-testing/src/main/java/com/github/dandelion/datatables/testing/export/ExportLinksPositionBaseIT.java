package com.github.dandelion.datatables.testing.export;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.springframework.util.StringUtils;

import com.github.dandelion.datatables.testing.BaseIT;
import com.github.dandelion.datatables.testing.utils.Constants;

public class ExportLinksPositionBaseIT extends BaseIT {

	@Test
	public void should_generate_bottom_right_link() throws IOException, Exception {
		goToPage("export/bottom_right_link");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_paginate");
		
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:right;");
	}
	
	@Test
	public void should_generate_bottom_middle_link() throws IOException, Exception {
		goToPage("export/bottom_middle_link");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_paginate");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dandelion_dataTables_export");
		
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:left;margin-left:10px;");
	}
	
	@Test
	public void should_generate_bottom_left_link() throws IOException, Exception {
		goToPage("export/bottom_left_link");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_paginate");
				
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:left;margin-right:10px;");		
	}
	
	@Test
	public void should_generate_top_right_link() throws IOException, Exception {
		goToPage("export/top_right_link");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_paginate");
				
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:right");
	}
	
	@Test
	public void should_generate_top_middle_link() throws IOException, Exception {
		goToPage("export/top_middle_link");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_paginate");
				
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:left;margin-left:10px;");
	}
	
	@Test
	public void should_generate_top_left_link() throws IOException, Exception {
		goToPage("export/top_left_link");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_paginate");
				
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:left;margin-right:10px;");
	}
	
	@Test
	public void should_generate_top_and_bottom_right_links() throws IOException, Exception {
		goToPage("export/top_and_bottom_right_links");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find("div", 5).getAttribute("class")).contains("dataTables_paginate");
	}
}
