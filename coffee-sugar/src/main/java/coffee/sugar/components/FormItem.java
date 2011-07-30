package coffee.sugar.components;

import java.io.IOException;
import java.util.ArrayList;

import coffee.core.components.IComponent;
import coffee.core.components.TextNode;
import coffee.core.components.xhtml.XHtmlComponent;
import coffee.core.util.Util;
import coffee.sugar.Component;
import coffee.sugar.Widget;

public class FormItem extends Box {

	public static final String LABEL_ALIGN_LEFT = "left";
	public static final String LABEL_ALIGN_RIGHT = "right";
	public static final String LABEL_ALIGN_CENTER = "center";

	private boolean required = false;
	private String label;
	private String labelWidth;
	private String labelAlign;
	private String direction;

	@Override
	public void configure() {
		label = getAttributeValue("label");

		if (!Util.isNull(id))
			registerComponent("new FormItem( {id:\""+id+"\", label:\"" + getLabel() + "\"} )");
	}

	@Override
	public void renderChildren() throws IOException {
		ArrayList<IComponent> customChildren = new ArrayList<IComponent>();
		
		if (!Util.isNull(getLabel())) {
			Text labelComponent = new Text();
			labelComponent.setSkin("Label");

			if (!Util.isNull(getLabelAlign()))
				labelComponent.setAlign(getLabelAlign());

			labelComponent.setWidth(getLabelWidth());
			labelComponent.setCoffeeContext(coffeeContext);

			if (!Util.isNull(id))
				labelComponent.setAttribute("id", id + "Label");
			labelComponent.addChild(
				new TextNode(getLabel())
					.setCoffeeContext(coffeeContext));

			if (isRequired()) {
				IComponent required = 
					new XHtmlComponent("span")
							.setAttribute("class", "required")
							.setTextContent("*")
							.setCoffeeContext(coffeeContext);
				labelComponent.addChild(required);
			}
			
			customChildren.add(labelComponent);
		}
		
		customChildren.addAll(getChildren());
		for (IComponent child : customChildren) {
			configureChildBeforeRender(child);
			child.setCoffeeContext(coffeeContext);
			child.render();
		}
	}

	@Override
	public void configureChildBeforeRender(IComponent child) throws IOException {
		if (getDirection().equals(DIRECTION_HORIZONTAL)
		&&  Component.class.isInstance(child)) {
			Component component = (Component)child;
			component.setFloating(true);
		}
		if (Widget.class.isInstance(child)) {
			Widget widget = (Widget)child;
			widget.setRequired(Boolean.parseBoolean(getAttributeValue("required")));
			widget.setLabel(getAttributeValue("label"));
		}
	}

	public void setRequired(String required) {
		this.required = Boolean.parseBoolean(required);
	}
	
	public void setRequired (Boolean required) {
		this.required = required;
	}

	public boolean isRequired() {
		return required;
	}

	public String getLabel() {
		return label;
	}

	public void setLabelWidth(String labelWidth) {
		this.labelWidth = labelWidth;
	}

	public String getLabelWidth() {
		if (Util.isNull(labelWidth))
			return "120px";
		return labelWidth;
	}

	public void setLabelAlign(String labelAlign) {
		this.labelAlign = labelAlign;
	}

	public String getLabelAlign() {
		if (Util.isNull(labelAlign))
			return LABEL_ALIGN_LEFT;
		return labelAlign;
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		if (Util.isNull(direction))
			return DIRECTION_HORIZONTAL;
		return direction;
	}
	
	@Override
	public String getSkin() {
		return new StringBuilder()
			.append("FormItem ")
			.append(super.getSkin())
				.toString();
	}
}
