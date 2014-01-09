package com.github.dandelion.datatables.core.extension.theme;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.asset.web.AssetRequestContext;
import com.github.dandelion.datatables.core.asset.JavascriptFunction;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

public class Bootstrap2ResponsiveThemeTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension() {
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap2ResponsiveTheme())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(1);
		assertThat(mainConfig).hasSize(4);
		assertThat(mainConfig).includes(entry(DTConstants.DT_AUTO_WIDTH, false));
		assertThat(mainConfig.keySet()).contains(CallbackType.DRAW.getName(), CallbackType.ROW.getName(), CallbackType.PREDRAW.getName());
		
		assertThat(mainConfig.get(CallbackType.DRAW.getName()).toString())
			.isEqualTo(new JavascriptFunction("responsiveHelper_fakeId.respond();", CallbackType.DRAW.getArgs()).toString());
		assertThat(mainConfig.get(CallbackType.ROW.getName()).toString())
			.isEqualTo(new JavascriptFunction("responsiveHelper_fakeId.createExpandIcon(nRow);", CallbackType.ROW.getArgs()).toString());
		assertThat(mainConfig.get(CallbackType.PREDRAW.getName()).toString())
			.isEqualTo(new JavascriptFunction("if (!responsiveHelper_fakeId) { responsiveHelper_fakeId = new ResponsiveDatatablesHelper(oTable_fakeId, breakpointDefinition); }", CallbackType.PREDRAW.getArgs()).toString());
		
		assertThat(mainJsFile.getBeforeAll().toString()).isEqualTo("var responsiveHelper_fakeId;\nvar breakpointDefinition = { tablet: 1024, phone : 480 };\n");
	}
}
