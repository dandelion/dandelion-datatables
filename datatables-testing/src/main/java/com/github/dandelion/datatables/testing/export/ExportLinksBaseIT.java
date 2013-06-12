package com.github.dandelion.datatables.testing.export;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.testing.BaseIT;

public abstract class ExportLinksBaseIT extends BaseIT {
	
	@Test
	public void should_generate_export_markup() throws Exception {
		goToPage("export/default_csv_link");
		assertThat(find("div.dandelion_dataTables_export")).hasSize(1);
	}
}