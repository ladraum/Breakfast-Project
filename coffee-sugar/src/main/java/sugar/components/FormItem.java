package sugar.components;

import java.io.IOException;
import java.util.ArrayList;

import sugar.core.Component;
import sugar.core.Widget;
import coffee.components.IComponent;
import coffee.components.html.HtmlComponent;
import coffee.core.TextNode;
import coffee.util.Util;

public class FormItem extends Box {

	public static final String LABEL_ALIGN_LEFT = "left";
	public static final String LABEL_ALIGN_RIGHT = "right";
	public static final String LABEL_ALIGN_CENTER = "center";

	private boolean required = false;
	private String label;
	private String labelWidth;
	private String labelAlign;

	@Override
	public void configure() {
		setAttribute("class", "FormItem");
		label = getAttributeValue("label");

		if (!Util.isNull(id))
			registerComponent("new FormItem( {id:\""+id+"\", label:\"" + getLabel() + "\"} )");
	}

	@Override
	public void renderChildren() throws IOException {
		ArrayList<IComponent> customChildren = new ArrayList<IComponent>();
		
		if (!Util.isNull(getLabel())) {
			Label labelComponent = new Label();
			labelComponent.setWidth(getLabelWidth());
			labelComponent.setCoffeeContext(coffeeContext);

			if (!Util.isNull(id))
				labelComponent.setAttribute("id", id + "Label");
			labelComponent.addChild(
				new TextNode(getLabel())
					.setCoffeeContext(coffeeContext));

			if (isRequired()) {
				IComponent required = 
					new HtmlComponent("span")
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
			component.setFloatingBox(true);
		}
		if (Widget.class.isInstance(child)) {
			Widget widget = (Widget)child;
			widget.setRequired(isRequired());
			widget.setLabel(getLabel());
		}
	}
	
	@Override
	public StringBuilder getStyleDefinition() {
		StringBuilder style = super.getStyleDefinition();
		style.append("text-align:").append(getLabelAlign()).append(';');
		return style;
	}

	public void setRequired(String required) {
		this.required = Boolean.parseBoolean(required);
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
			return "200px";
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
}
