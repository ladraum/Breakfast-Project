package breakfast.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import breakfast.coffee.util.StringUtil;
import breakfast.coffee.util.Util;
import breakfast.sugar.Widget;



public class CheckBox extends Widget {

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();

		String label = getAttributeValue("label");

		writer
			.append("<label style=\"")
			.append(getStyleDefinition())
			.append("\">")

			.append("<input type=\"checkbox\" name=\"")
			.append(getId())
			.append("\" id=\"")
			.append(getId())
			.append("\" ")
			.append(getEventsDefinition());

		String checked = getAttributeValue("checked");
		if (!Util.isNull(checked) && checked.equals("true"))
				writer.append("checked=\"checked\" ");

		writer.append(" />");

		if (!StringUtil.isEmpty(label))
			writer
				.append("<span id=\"")
				.append(getId())
				.append("Label\">")
				.append(label)
				.append("</span>");
			writer
				.append("<script>application.addChild(new Checkbox( {required:")
					.append(isRequired().toString())
				.append(",label:\"")
					.append(getAttributeValue("label"))
				.append("\",")
				.append("id:\"")
					.append(getId())
				.append("\",")

			.append("}));</script>")
		.append("</label>");
	}

	public void setChecked(String value) {
		holdValue(value);
	}

}
