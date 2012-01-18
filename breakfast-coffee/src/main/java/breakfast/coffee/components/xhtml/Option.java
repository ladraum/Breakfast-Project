package breakfast.coffee.components.xhtml;

import java.io.IOException;

public class Option extends XHtmlComponent {

	@Override
	public void configure() {
		super.configure();
		setComponentName("option");

		setId(getParent().getId());
		holdExpression(getAttribute("selected").toString());
	}

	@Override
	public void render() throws IOException {

		String selected = getAttributeAsString("selected");
		if (!selected.equals(getAttributeAsString("value")))
			ignoreAttribute("selected");

		super.render();
	}
}
