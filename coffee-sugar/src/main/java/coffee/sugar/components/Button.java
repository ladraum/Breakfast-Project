package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.util.Util;
import coffee.sugar.Widget;

public class Button extends Widget {

	@Override
	public void configure() {
		if (!Util.isNull(getId()))
			registerComponent("new Button( {id:\""+getId()+"\"})");
	}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<input type=\"button\" class=\"")
				.append(getSkin())
			.append("\" style=\"")
			.append(getStyleDefinition())
			.append("\" ");

		if (!Util.isNull(getId()))
			writer.append("id=\"")
				  .append(getId())
				  .append("\"" );

		String label = getAttributeValue("label");
		if (!Util.isNull(label))
			writer
				.append("value=\"")
				.append(label)
				.append("\" ");

		writer
			.append(getEventsDefinition())
			.append(" />");
	}

}
