package breakfast.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import breakfast.coffee.components.IComponent;
import breakfast.coffee.util.StringUtil;
import breakfast.coffee.util.Util;
import breakfast.sugar.Component;


public class Box extends Component {

	public static final String DIRECTION_HORIZONTAL = "horizontal";
	public static final String DIRECTION_VERTICAL = "vertical";

	protected String id;
	private String direction;

	@Override
	public void configure() {
		if (!Util.isNull(id))
			registerComponent("new Box( {id:\""+id+"\"} )");
	}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<div class=\"Box ")
			.append(getSkin())
			.append("\" ");

		if (!Util.isNull(id))
			writer.append(" id=\"")
				  .append(id)
				  .append("\" ");

		writer.append("style=\"")
			  .append(getStyleDefinition())
			  .append("\">");

		renderChildren();

		writer.append("</div>");
	}

	@Override
	public void renderChildren() throws IOException {
		for (IComponent child : getChildren()) {
			configureChildBeforeRender(child);
			child.setCoffeeContext(coffeeContext);
			child.render();
		}
	}

	public void configureChildBeforeRender(IComponent child) throws IOException {
		int measuredWidth = 100 / getNumChildren();
		if (getDirection().equals(DIRECTION_HORIZONTAL)
		&&  Component.class.isInstance(child)) {
			Component component = (Component)child;
			if (!StringUtil.isEmpty(component.getWidth()))
				component.setWidth(measuredWidth + "%");
			component.setFloating(true);
		}
	}

	public IComponent setId(String id) {
		this.id = id;
		return this;
	}

	public String getId() {
		return id;
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
