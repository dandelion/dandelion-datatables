package my.custom.packagee;

import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.feature.AbstractFeature;
import com.github.dandelion.datatables.core.html.HtmlTable;

public class MyCustomFeature extends AbstractFeature {

	@Override
	public String getName() {
		return "myCustomFeature";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public void setup(HtmlTable table) throws ExtensionLoadingException {
		addParameter(new Parameter("bStateSave", true));
	}
}