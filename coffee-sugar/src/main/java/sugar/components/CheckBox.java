package sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.util.StringUtil;
import coffee.util.Util;

import sugar.core.Widget;

public class CheckBox extends Widget {

	@Override
	public void configure() {
		StringBuilder buffer = new StringBuilder();
		buffer
			.append("new Checkbox( {required:")
				.append(isRequired())
			.append(",label:\"")
				.append(getAttributeValue("label"))
			.append("\",")
			.append("id:\"")
				.append(getId())
			.append("\",")
			.append("})");
		
		registerComponent(buffer.toString());
	}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		
		String label = getAttributeValue("label");
		
		if (!StringUtil.isEmpty(label))
			writer.append("<label>");

		writer
			.append("<input type=\"checkbox\" name=\"")
			.append(getId())
			.append("\" id=\"")
			.append(getId())
			.append("\" ");

		String checked = getAttributeValue("checked");
		if (!Util.isNull(checked) && checked.equals("true"))
				writer.append("checked=\"checked\" ");

		writer
			.append(getEventsDefinition())
			.append(" />");
		
		if (!StringUtil.isEmpty(label))
			writer.append(label).append("</label>");
	}

	public void setChecked(String value) {
		holdValue(value);
	}

}
