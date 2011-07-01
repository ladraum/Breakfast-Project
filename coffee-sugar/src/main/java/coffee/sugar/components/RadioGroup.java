package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.components.IComponent;
import coffee.core.util.Util;
import coffee.sugar.Widget;


public class RadioGroup extends Widget {

	public static final String DIRECTION_HORIZONTAL = "horizontal";
	public static final String DIRECTION_VERTICAL = "vertical";
	
	private String direction;

	@Override
	public void configure() {}

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

		writer.append("</ul>")
			  .append("<script>application.addChild(new RadioGroup( {required:")
					.append(isRequired().toString())
			  .append(",label:\"")
				.append(getLabel())
			  .append("\",id:\"")
				.append(getId())
			  .append("\",")
		
			  .append("target:[");
				for (Short i=0; i<getNumChildren(); i++)
					writer.append('"').append(getId()).append(i.toString()).append("\",");
	
		writer.append("]}));</script>");
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
			counter++;
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
