package coffee.components.html;

import java.io.IOException;

import coffee.components.IComponent;

public class Select extends HtmlComponent {
	
	private String name;

	@Override
	public void configure() {
		super.configure();
		setComponentName("select");
		ignoreAttribute("selected");
	}
	
	@Override
	public void renderChildren() throws IOException {
		for (IComponent child : getChildren()) {
			String selected = getAttributeValue("selected");
			if (child.getAttribute("value").equals(selected))
				child.setAttribute("selected", "selected");
			child.render();
		}
	}

	public void setName(String name) {
		this.name = name;
		setId(name);
	}

	public String getName() {
		return name;
	}

	public void setSelected(String selected) {
		holdValue(selected);
	}
}
