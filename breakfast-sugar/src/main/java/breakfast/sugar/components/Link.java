package breakfast.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import breakfast.coffee.util.Util;
import breakfast.sugar.Component;


public class Link extends Component {

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<a href=\"")
				.append(getAttributeValue("src"))
			.append("\" class=\"")
				.append(getSkin())
			.append("\" style=\"")
				.append(getStyleDefinition())
			.append("\" >");

		renderChildren();

		String textContent = getTextContent();
		if (!Util.isNull(textContent))
			writer.append(textContent);
		writer.append("</a>");
	}

}
