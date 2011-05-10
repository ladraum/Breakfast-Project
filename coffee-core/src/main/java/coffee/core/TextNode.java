package coffee.core;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.components.html.HtmlComponent;

public class TextNode extends HtmlComponent {
	
	public TextNode() {}
	
	public TextNode(String content) {
		this.setTextContent(content);
	}

	@Override
	public void configure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() throws IOException {
		PrintWriter writer = coffeeContext.getResponse().getWriter();
		writer.append(bindTextContent());
	}

}
