package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.util.StringUtil;
import coffee.core.util.Util;
import coffee.sugar.Widget;


public class CheckBox extends Widget {

	@Override
	public void configure() {}

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
		
		writer
			.append("<script>application.addChild(new Checkbox( {required:")
				.append(isRequired().toString())
			.append(",label:\"")
				.append(getAttributeValue("label"))
			.append("\",")
			.append("id:\"")
				.append(getId())
			.append("\",")
	
		.append("}));</script>");
	}

	public void setChecked(String value) {
		holdValue(value);
	}

}
