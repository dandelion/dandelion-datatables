package com.github.dandelion.datatables.core.extension.theme;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.asset.generator.js.JsFunction;
import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.generator.DTConstants;
import com.github.dandelion.datatables.core.option.CallbackType;

import static com.github.dandelion.core.asset.generator.js.jquery.JQueryContentPlaceholder.BEFORE_ALL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class Bootstrap2ResponsiveThemeTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension() {
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap2ResponsiveTheme())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(mainConfig).hasSize(4);
		assertThat(mainConfig).contains(entry(DTConstants.DT_AUTO_WIDTH, false));
		assertThat(mainConfig.keySet()).contains(CallbackType.DRAW.getName(), CallbackType.ROW.getName(), CallbackType.PREDRAW.getName());
		
		assertThat(mainConfig.get(CallbackType.DRAW.getName()).toString())
			.isEqualTo(new JsFunction("responsiveHelper_fakeId.respond();", CallbackType.DRAW.getArgs()).toString());
		assertThat(mainConfig.get(CallbackType.ROW.getName()).toString())
			.isEqualTo(new JsFunction("responsiveHelper_fakeId.createExpandIcon(nRow);", CallbackType.ROW.getArgs()).toString());
		assertThat(mainConfig.get(CallbackType.PREDRAW.getName()).toString())
			.isEqualTo(new JsFunction("if (!responsiveHelper_fakeId) { responsiveHelper_fakeId = new ResponsiveDatatablesHelper(oTable_fakeId, breakpointDefinition); }", CallbackType.PREDRAW.getArgs()).toString());
		
		assertThat(datatableContent.getPlaceholderContent().get(BEFORE_ALL).toString()).contains("var responsiveHelper_fakeId;\nvar breakpointDefinition = { tablet: 1024, phone : 480 };\n");
	}
}
