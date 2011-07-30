package coffee.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.util.Util;
import coffee.sugar.Component;


public class Text extends Component {
	
	private String textContent;

	@Override
	public void configure() {
		if (!Util.isNull(getId()))
			registerComponent("new Text( {id:\""+getId()+"\", text:\""+getTextContent()+"\"})");
	}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer.append("<span class=\"").append(getSkin()).append("\" ")
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
	
	@Override
	public String getTextContent() {
		if (Util.isNull(textContent))
			textContent = super.getTextContent();
		return textContent;
	}

}
