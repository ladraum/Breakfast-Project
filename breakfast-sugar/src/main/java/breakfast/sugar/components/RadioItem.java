package breakfast.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import breakfast.sugar.Widget;



public class RadioItem extends Widget {
	
	private boolean selected;
	private String name;
	private String events;
	private String direction;

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<li style=\"")
			.append(getStyleDefinition())
			.append("\"><label><input type=\"radio\" value=\"")
			.append(getAttributeValue("value"))
			.append("\" name=\"")
			.append(getName())
			.append("\" id=\"")
			.append(getId())
			.append("\" ")
			.append(getEvents());

		if (isSelected())
			writer.append("checked=\"checked\"");

		writer
			.append('>')
			.append(getAttributeValue("label"))
			.append("</label></li>");
	}
	
	@Override
	public StringBuilder getStyleDefinition() {
		StringBuilder style = super.getStyleDefinition();
		
		if (getDirection().equals(RadioGroup.DIRECTION_HORIZONTAL))
			style.append("display:inline;");

		return style;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEvents(String events) {
		this.events = events;
	}

	public String getEvents() {
		return events;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}

}
