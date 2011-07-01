package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import coffee.core.components.IComponent;
import coffee.core.util.JSON;
import coffee.core.util.Util;
import coffee.sugar.Widget;

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
				.append("\",service:\"")
					.append(getAttributeValue("service"))
				.append("\",children:")
					.append(getChildrenAsJSON())
				.append(",selectedChildren:")
					.append(getSelectedChildrenAsJSON())
			.append("}))</script></div>");

	}

	public StringBuilder getSelectedChildrenAsJSON(){
		Collection<?> selectedChildren = (Collection<?>)getAttribute("selectedChildren");
		if (Util.isNull(selectedChildren))
			return null;
		return JSON.serializeArrayOfObjects(selectedChildren);
	}

	@SuppressWarnings("unchecked")
	public StringBuilder getChildrenAsJSON(){
		Collection<Object> children = new ArrayList<Object>();

		for (IComponent child : getChildren())
			if (TreeItem.class.isInstance(child))
				children.add(child);

		Collection<Object> childrenAttribute = (Collection<Object>)getAttribute("childrenNodes");
		if (!Util.isNull(childrenAttribute))
			children.addAll(childrenAttribute);

		return JSON.serializeArrayOfObjects(children);
	}

	@Override
	public String getId() {
		String id = super.getId();
		if (Util.isNull(id))
			id = coffeeContext.getNextId();
		return id;
	}

}
