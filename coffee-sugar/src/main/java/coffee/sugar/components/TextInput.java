package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.util.Util;
import coffee.sugar.Widget;


public class TextInput extends Widget {

	@Override
	public void configure() {}

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
		
		writer
			.append("<script>application.addChild(new TextInput( {required:")
			.append(isRequired().toString())
			.append(",label:\"")
				.append(getLabel())
			.append("\",");
		
		if (!Util.isNull(getId()))
			writer
				.append("id:\"")
					.append(getId())
				.append("\",");
		
		writer.append("}));</script>");
	}

}
