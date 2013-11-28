package my.custom.packagee;

import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlTable;

public class MyCustomFeature extends AbstractExtension {

	@Override
	public String getName() {
		return "myCustomFeature";
	}

	@Override
	public void setup(HtmlTable table) {
		addParameter(new Parameter("bStateSave", true));
	}
}