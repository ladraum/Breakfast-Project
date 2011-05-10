package coffee.components.template;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import coffee.components.AbstractComponent;
import coffee.components.IComponent;
import coffee.core.CoffeeResourceLoader;

public class Template extends AbstractComponent {
	
	private String src;

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		try {
			CoffeeResourceLoader resourceLoader = CoffeeResourceLoader.getInstance();
			IComponent template = resourceLoader.compile(src, getCoffeeContext());
			template.render();
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		} catch (SAXException e) {
			throw new IOException(e);
		} catch (CloneNotSupportedException e) {
			throw new IOException(e);
		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrc() {
		return src;
	}
}
