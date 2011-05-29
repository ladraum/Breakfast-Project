package sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.components.IComponent;
import coffee.util.Util;

import sugar.core.Widget;

public class RadioGroup extends Widget {

	public static final String DIRECTION_HORIZONTAL = "horizontal";
	public static final String DIRECTION_VERTICAL = "vertical";
	
	private String direction;

	@Override
	public void configure() {
		StringBuilder buffer = new StringBuilder();
		buffer
			.append("new RadioGroup( {required:")
				.append(isRequired())
			.append(",label:\"")
				.append(getAttributeValue("label"))
			.append("\",id:\"")
				.append(getId())
			.append("\",");
		
		buffer.append("target:[");
		for (short i=0; i<getNumChildren(); i++)
			buffer.append(getId()).append(i).append(',');
		buffer.append("]})");

		registerComponent(buffer.toString());
	}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<ul class=\"RadioGroup\" id=\"")
			.append(getId())
			.append("\" style=\"")
			.append(getStyleDefinition())
			.append("\">");

		renderChildren();

		writer.append("</ul>");
	}

	@Override
	public void renderChildren() throws IOException {
		String value = getAttributeValue("value");

		short counter = 0;
		for (IComponent child : getChildren()) {
			if (!RadioItem.class.isInstance(child))
				continue;
			RadioItem radioItem = (RadioItem)child;
			if (radioItem.getAttributeValue("value").equals(value))
				radioItem.setSelected(true);
			radioItem.setName(getId());
			radioItem.setId(getId() + counter);
			radioItem.setEvents(getEventsDefinition().toString());
			radioItem.setDirection(getDirection());
			radioItem.render();
		}
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		if (Util.isNull(direction))
			return DIRECTION_VERTICAL;
		return direction;
	}

}
