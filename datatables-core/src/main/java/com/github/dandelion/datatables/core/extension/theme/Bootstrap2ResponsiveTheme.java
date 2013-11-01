package com.github.dandelion.datatables.core.extension.theme;

import com.github.dandelion.datatables.core.asset.Parameter.Mode;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;

public class Bootstrap2ResponsiveTheme extends AbstractExtension {

	@Override
	public String getName() {
		return "bootstrap2-responsive";
	}

	@Override
	public void setup(HtmlTable table) throws ExtensionLoadingException {
		
		// Add necessary web resources
		addScope("bootstrap-responsive");
		
		// Necessary variables and breakpoint definitions
		appendToBeforeAll("var responsiveHelper; var breakpointDefinition = { tablet: 1024, phone : 480 };");
		appendToBeforeAll("var tableContainer = $('#" + table.getId() + "');");
		
		// Datatables' params
		addParameter(DTConstants.DT_AUTO_WIDTH, false, Mode.OVERRIDE);
		addCallback(CallbackType.PREDRAW, "if (!responsiveHelper) { responsiveHelper = new ResponsiveDatatablesHelper(tableContainer, breakpointDefinition); }");
        addCallback(CallbackType.ROW, "responsiveHelper.createExpandIcon(nRow);");
        addCallback(CallbackType.DRAW, "responsiveHelper.respond();");
        
        // HTML markup updates
        for(HtmlColumn headerColumn : table.getFirstHeaderRow().getColumns()){
        	headerColumn.addDynamicAttribute("data-hide", "phone,tablet");
        }
	}
}