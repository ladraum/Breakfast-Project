package sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.util.Util;

import sugar.core.Component;

public class Label extends Component {
	
	private String className;

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer.append("<span class=\"").append(getClassName()).append("\" ")
					.append("style=\"").append(getStyleDefinition()).append("\" ");

		if (!Util.isNull(getId()))
			writer.append("id=\"").append(getId()).append("\" ");
		writer.append('>');

		renderChildren();

		String textContent = getTextContent();
		if (!Util.isNull(textContent))
			writer.append(textContent);
		writer.append("</span>");
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		if (Util.isNull(className))
			return "Label";
		return className;
	}

}
