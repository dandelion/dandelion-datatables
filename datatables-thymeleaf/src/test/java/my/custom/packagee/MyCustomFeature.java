package my.custom.packagee;

import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlTable;

public class MyCustomFeature extends AbstractExtension {

	public String getName() {
		return "myCustomFeature";
	}

	public void setupWrapper(HtmlTable table) throws ExtensionLoadingException {
		addParameter(new Parameter("bStateSave", true));
	}
}