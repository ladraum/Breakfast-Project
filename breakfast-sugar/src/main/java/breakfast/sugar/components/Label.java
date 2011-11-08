package breakfast.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import breakfast.coffee.util.Util;
import breakfast.sugar.Component;


public class Label extends Component {
	
	private String textContent;
	private boolean bold = false;

	@Override
	public void configure() {
		if (!Util.isNull(getId()))
			registerComponent("new Text( {id:\""+getId()+"\", text:\""+getTextContent()+"\"})");
	}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer.append("<label class=\"").append(getSkin()).append("\" ")
					.append("style=\"").append(getStyleDefinition()).append("\" ");

		if (!Util.isNull(getId()))
			writer.append("id=\"").append(getId()).append("\" ");
		writer.append('>');

		renderChildren();

		String textContent = getTextContent();
		if (!Util.isNull(textContent))
			writer.append(textContent);
		writer.append("</label>");
	}
	
	@Override
	public StringBuilder getStyleDefinition() {
		StringBuilder definition = super.getStyleDefinition();

		if (isBold())
			definition.append("font-weight: bold;");

		return definition;
	}

	@Override
	public String getTextContent() {
		if (Util.isNull(textContent))
			textContent = super.getTextContent();
		return textContent;
	}

	public void setBold(String bold) {
		this.bold = Boolean.parseBoolean(bold);
	}

	public void setBold(Boolean bold) {
		this.bold = bold;
	}

	public Boolean isBold() {
		return bold;
	}

}
