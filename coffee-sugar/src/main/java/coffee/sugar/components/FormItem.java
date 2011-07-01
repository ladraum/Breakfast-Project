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
			return LABEL_ALIGN_RIGHT;
		return labelAlign;
	}
}
