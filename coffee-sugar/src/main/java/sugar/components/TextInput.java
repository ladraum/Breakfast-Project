package sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.util.Util;

import sugar.core.Widget;

public class TextInput extends Widget {

	@Override
	public void configure() {
		StringBuilder buffer = new StringBuilder();
		buffer
			.append("new TextInput( {required:")
				.append(isRequired())
			.append(",label:\"")
				.append(getLabel())
			.append("\",");
		
		if (!Util.isNull(getId()))
			buffer
				.append("id:\"")
					.append(getId())
				.append("\",");
		
		buffer.append("})");
		
		registerComponent(buffer.toString());
	}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<input type=\"text\" name=\"")
			.append(getId())
			.append("\" id=\"")
			.append(getId())
			.append("\" style=\"")
			.append(getStyleDefinition())
			.append("\" ");

		String value = getAttributeValue("value");
		if (!Util.isNull(value))
				writer.append("value=\"").append(value).append("\" ");

		writer
			.append(getEventsDefinition())
			.append(" />");
	}

}
