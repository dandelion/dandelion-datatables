package com.github.dandelion.datatables.core.extension.feature;

import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.asset.ResourceType;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * 
 * @see http://www.datatables.net/plug-ins/pagination
 */
public class PaginationTypeBootstrapFourButtonFeature extends AbstractExtension {

	@Override
	public String getName() {
		return "PaginationTypeBootstrapFourButton";
	}

	@Override
	public void setup(HtmlTable table) {
		addJsResource(new JsResource(ResourceType.FEATURE, "PaginationTypeBootstrapFourButton", "datatables/features/paginationType/bootstrap_four_button.js"));
		addParameter(new Parameter(DTConstants.DT_PAGINATION_TYPE, "bootstrap", Parameter.Mode.OVERRIDE));
	}
}
