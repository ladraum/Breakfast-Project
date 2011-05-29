package sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.components.IComponent;
import coffee.util.Util;

import sugar.core.Widget;

public class ComboBox extends Widget {

	@Override
	public void configure() {
		StringBuilder buffer = new StringBuilder();
		buffer
			.append("new ComboBox( {required:")
				.append(isRequired())
			.append(",label:\"")
				.append(getAttributeValue("label"))
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
			.append("<select name=\"")
			.append(getId())
			.append("\" id=\"")
			.append(getId())
			.append("\" style=\"")
			.append(getStyleDefinition())
			.append("\" ")
			.append(getEventsDefinition())
			.append(">");

		renderChildren();

		writer.append("</select>");
	}

	@Override
	public void renderChildren() throws IOException {
		String value = getAttributeValue("value");
		for (IComponent child : getChildren()) {
			if (!ComboItem.class.isInstance(child))
				continue;
			ComboItem comboItem = (ComboItem)child;
			if (comboItem.getAttributeValue("value").equals(value))
				comboItem.setSelected(true);
			comboItem.render();
		}
	}

}
