package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.components.IComponent;
import coffee.core.util.Util;
import coffee.sugar.Widget;

public class ComboBox extends Widget {
	
	private Integer size;
	private String selectedValue;

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
			.append("\" ");

		if (getSize() > 0)
			writer
				.append("size=\"")
				.append(getSize().toString())
				.append("\" ");

		writer
			.append(getEventsDefinition())
			.append(">");

		renderChildren();

		writer.append("</select>");
		
		writer
			.append("<script>application.addChild(new ComboBox( {required:")
				.append(isRequired().toString())
			.append(",label:\"")
				.append(getLabel())
			.append("\",selected:\"")
				.append(getSelectedValue())
			.append('"');

		if (!Util.isNull(getId()))
			writer
				.append(",id:\"")
					.append(getId())
				.append('"');

		writer.append("}));</script>");
	}

	@Override
	public void renderChildren() throws IOException {
		String value = getSelectedValue();
		for (IComponent child : getChildren()) {
			if (ComboItem.class.isInstance(child)) {
				ComboItem comboItem = (ComboItem)child;
				if (comboItem.getAttributeValue("value").equals(value))
					comboItem.setSelected(true);
			}
			child.render();
		}
	}

	public void setSize(String size) {
		this.size = Integer.parseInt(size);
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getSize() {
		if (Util.isNull(size))
			size = 0;
		return size;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public String getSelectedValue() {
		if (Util.isNull(selectedValue)) {
			selectedValue = getAttributeValue("value");
			if (Util.isNull(selectedValue))
				selectedValue = "";
		}
		return selectedValue;
	}

}
