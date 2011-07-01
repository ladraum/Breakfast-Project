package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.components.IComponent;
import coffee.core.util.Util;
import coffee.sugar.Widget;

public class ComboBox extends Widget {

	@Override
	public void configure() {}

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
		
		writer
			.append("<script>application.addChild(new ComboBox( {required:")
				.append(isRequired().toString())
			.append(",label:\"")
				.append(getAttributeValue("label"))
			.append("\",");

		if (!Util.isNull(getId()))
			writer
				.append("id:\"")
					.append(getId())
				.append("\",");

		writer.append("}));</script>");
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
