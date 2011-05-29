package sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.util.Util;

import sugar.core.Widget;

public class Button extends Widget {

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<input type=\"button\" style=\"")
			.append(getStyleDefinition())
			.append("\" ");

		String label = getAttributeValue("label");
		if (!Util.isNull(label))
			writer
				.append("value=\"")
				.append(label)
				.append("\" ");

		writer
			.append(getEventsDefinition())
			.append(" />");
	}

}
