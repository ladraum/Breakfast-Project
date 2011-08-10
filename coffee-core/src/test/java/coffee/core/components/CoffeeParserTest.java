package coffee.core.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import coffee.core.Cafeteria;
import coffee.core.CoffeeParser;
import coffee.core.CoffeeResource;
import coffee.core.loader.CoffeeResourceLoader;

public class CoffeeParserTest {

	private CoffeeResourceLoader resourceLoader;

	public CoffeeParserTest() throws ClassNotFoundException, IOException {
		resourceLoader = Cafeteria.getResourceLoader("/test");
		resourceLoader.initialize();
	}

	@Test
	public void parseHelloWorldTemplate() throws IOException, ParserConfigurationException, SAXException {
		CoffeeResource resource = resourceLoader.getResource("/hello");
		assertNotNull("Can't find resource for uri /hello", resource);
		InputStream template = resourceLoader.loadTemplate(resource.getTemplate());
		CoffeeParser parser = new CoffeeParser();
		IComponent application = parser.parse(template);
		assertNotNull(application);
	}

	@Test
	public void grantThatNestingDontLooseTexts() throws IOException, ParserConfigurationException, SAXException {
		InputStream template = resourceLoader.loadTemplate("templates/nestedTest.xhtml");
		CoffeeParser parser = new CoffeeParser();
		IComponent application = parser.parse(template);
		assertNotNull(application);
		assertEquals(2, application.getNumChildren());
		assertEquals(" Home!", application.getTextContent());
	}

}
