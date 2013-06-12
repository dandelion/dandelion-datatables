package com.github.dandelion.datatables.testing.basics;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.github.dandelion.datatables.testing.BaseIT;
import com.github.dandelion.datatables.testing.utils.Constants;

public class MultipleTablesBaseIT extends BaseIT {

	@Test
	public void should_disable_paging() throws IOException, Exception {
		goToPage("basics/multiple");
		
		assertThat(find("#" + Constants.TABLE_ID + "_wrapper")).hasSize(1);
		assertThat(find("#" + Constants.TABLE_ID2 + "_wrapper")).hasSize(1);
	}
}
