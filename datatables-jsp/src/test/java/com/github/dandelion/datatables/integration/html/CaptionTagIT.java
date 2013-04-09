package com.github.dandelion.datatables.integration.html;

import static org.fest.assertions.Assertions.assertThat;
import java.io.IOException;
import org.junit.Test;
import com.github.dandelion.datatables.integration.DomBaseIT;

/**
 * Test the HTML markup generation with custom caption tag.
 *
 * @author Gautier Dhordain
 */
public class CaptionTagIT extends DomBaseIT{

	@Test
	public void should_generate_caption_tag() throws IOException, Exception{
		goTo("/html/table_with_caption.jsp");

		assertThat(getTable().find("caption")).hasSize(1);
	}

	@Test
	public void should_populate_caption_tag() throws IOException, Exception{
		goTo("/html/table_with_caption.jsp");

		assertThat(getTable().find("caption").getAttribute("title")).isEqualTo("captionTitle");
		assertThat(getTable().find("caption").getText()).isEqualTo("captionValue");
	}
}