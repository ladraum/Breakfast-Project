package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.components.IComponent;
import coffee.core.util.Util;
import coffee.sugar.Component;

public class ButtonBar extends Component {

	@Override
	public void configure() {
		if (!Util.isNull(getId()))
			registerComponent("new Component( {id:\""+getId()+"\", label:\"" + getLabel() + "\"})");
	}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<ul class=\"ButtonBar ")
			.append(getSkin())
			.append("\" ");

		if (!Util.isNull(getId()))
			writer.append("id=\"").append(getId()).append("\" ");

		writer
			.append(" style=\"")
			.append(getStyleDefinition())
			.append("\">");

		renderChildren();
		
		writer.append("</ul>");
	}
	
	@Override
	public void renderChildren() throws IOException {
		PrintWriter writer = getWriter();
		for (IComponent child : getChildren()) {
			writer.append("<li>");
			child.render();
			writer.append("</li>");
		}
	}

}
