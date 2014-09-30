package com.github.dandelion.datatables.core.extension.feature;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.datatables.core.config.DatatableOptions;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

import static org.assertj.core.api.Assertions.assertThat;

public class AppearFeatureTest extends AbstractExtensionTest {

	@Test
	public void shoud_update_the_table_style_when_using_fadein_and_add_fadein_animation() {

		AppearFeature appearFeature = new AppearFeature();
		
		DatatableOptions.FEATURE_APPEAR.setIn(table.getTableConfiguration(), "fadein");
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(appearFeature)));

		StringBuilder cssStyle = DatatableOptions.CSS_STYLE.valueFrom(table.getTableConfiguration());
		
		assertThat(cssStyle.toString()).contains("display:none;");
		assertThat(appearFeature.getBeforeEndDocumentReady().toString()).contains("$('#fakeId').fadeIn();");
	}

	@Test
	public void shoud_update_the_table_style_when_using_fadein_and_add_fadein_animation_with_duration() {

		AppearFeature appearFeature = new AppearFeature();
		
		DatatableOptions.FEATURE_APPEAR.setIn(table.getTableConfiguration(), "fadein");
		DatatableOptions.FEATURE_APPEAR_DURATION.setIn(table.getTableConfiguration(), "2000");
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(appearFeature)));

		StringBuilder cssStyle = DatatableOptions.CSS_STYLE.valueFrom(table.getTableConfiguration());
		
		assertThat(cssStyle.toString()).contains("display:none;");
		assertThat(appearFeature.getBeforeEndDocumentReady().toString()).contains("$('#fakeId').fadeIn(2000);");
	}
	
	@Test
	public void shoud_update_the_table_style_when_using_block() {

		AppearFeature appearFeature = new AppearFeature();
		
		DatatableOptions.FEATURE_APPEAR.setIn(table.getTableConfiguration(), "block");
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(appearFeature)));

		assertThat(appearFeature.getBeforeEndDocumentReady().toString()).contains("$('#fakeId').show();");
	}
}

