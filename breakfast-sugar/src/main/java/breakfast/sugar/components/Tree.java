package breakfast.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import breakfast.coffee.components.IComponent;
import breakfast.coffee.util.Util;
import breakfast.coffee.util.json.JSON;
import breakfast.sugar.Widget;


public class Tree extends Widget {

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<div class=\"Tree\" id=\"")
			.append(getId())
			.append("\" style=\"")
			.append(getStyleDefinition())
			.append("\"><script>")
				.append("application.addChild(new Tree({label:\"")
					.append(getAttributeValue("label"))
				.append("\",id:\"")
					.append(getId())
				.append("\",service:")
					.append(getService())
				.append(",children:")
					.append(getChildrenAsJSON())
				.append(",selectedChildren:")
					.append(getSelectedChildrenAsJSON())
			.append("}))</script></div>");

	}

	private StringBuilder getService() {
		String value = getAttributeValue("service");
		if (Util.isNull(value))
			return null;
		return new StringBuilder()
				.append("\"")
				.append(value)
				.append("\"");
	}

	public StringBuilder getSelectedChildrenAsJSON(){
		Collection<?> selectedChildren = (Collection<?>)getAttribute("selectedChildren");
		if (Util.isNull(selectedChildren))
			return null;
		return new JSON().serialize(selectedChildren);
	}

	@SuppressWarnings("unchecked")
	public StringBuilder getChildrenAsJSON(){
		List<Object> children = new ArrayList<Object>();

		for (IComponent child : getChildren()) {
			child.setParent(null);
			if (TreeItem.class.isInstance(child))
				children.add(child);
		}

		Collection<Object> childrenAttribute = (Collection<Object>)getAttribute("childrenNodes");
		if (!Util.isNull(childrenAttribute))
			for (Object child : childrenAttribute) {
				if (!IComponent.class.isInstance(child))
					continue;
				((IComponent)child).setParent(null);
				children.add(child);
			}

		return new JSON().serialize(children);
	}

	@Override
	public String getId() {
		String id = super.getId();
		if (Util.isNull(id))
			id = coffeeContext.getNextId();
		return id;
	}

	public void setSelectedChildren(String value) {
		holdValue(value);
	}
}
