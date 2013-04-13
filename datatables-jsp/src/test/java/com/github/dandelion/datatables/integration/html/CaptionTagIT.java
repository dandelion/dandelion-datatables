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
public class CaptionTagIT extends DomBaseIT {

	@Test
	public void should_not_generate_caption_tag_by_default() throws IOException, Exception{
		goTo("/html/table_default.jsp");

		assertThat(getTable().find("caption")).hasSize(0);
	}

	@Test
	public void should_generate_caption_tag() throws IOException, Exception{
		goTo("/html/table_with_caption.jsp");

		assertThat(getTable().find("caption")).hasSize(1);
	}

	@Test
	public void should_populate_default_HTML_attribute() throws IOException, Exception{
		goTo("/html/table_with_caption.jsp");
		System.out.println(driver.getPageSource());

		assertThat(getTable().findFirst("caption").getId()).isEqualTo("captionId");
		assertThat(getTable().findFirst("caption").getAttribute("class")).isEqualTo("captionCssClass");
//		assertThat(getTable().findFirst("caption").getAttribute("style")).isEqualTo("captionCssStyle");
		assertThat(getTable().findFirst("caption").getAttribute("title")).isEqualTo("captionTitle");
	}

	@Test
	public void should_populate_caption_value_with_text() throws IOException, Exception{
		goTo("/html/table_with_caption.jsp");

		assertThat(getTable().find("caption").getText()).isEqualTo("captionValue");
	}

	@Test
	public void should_populate_caption_value_with_evaluated_tag() throws IOException, Exception{
		goTo("/html/table_with_dynamic_caption.jsp");

		assertThat(getTable().find("caption").getText()).isEqualTo("dynamic with JSP tag");
	}
}