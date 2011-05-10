package coffee.core;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import coffee.components.IComponent;
import coffee.components.IComponentFactory;
import coffee.components.html.HtmlComponentFactory;
import coffee.components.template.ComponentFactory;
import coffee.util.Util;


/**
 * 
 * @author Miere Liniel Teixeira
 * @since Coffee 1.0
 */
public class CoffeeParser extends DefaultHandler {

	private IComponent rootComponent;
	private IComponent currentComponent;
	private StringBuffer textContent;
	private CoffeeContext context;

	public CoffeeParser() {
		this.textContent = new StringBuffer();
		CoffeeContext.registerNamespace(
				"http://www.w3.org/1999/xhtml", new HtmlComponentFactory());
		CoffeeContext.registerNamespace(
				"urn:coffee:components", new ComponentFactory());
	}

/**
 * 
 * @param template
 * @return
 * @throws ParserConfigurationException
 * @throws SAXException
 * @throws IOException
 */
	public IComponent parse(InputStream template) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory sax = SAXParserFactory.newInstance();
		sax.setValidating(false);
		sax.setNamespaceAware(true);

		SAXParser parser = sax.newSAXParser();
		parser.parse(template, this);

		return rootComponent;
	}

	@Override
/**
 * Stores the namespace for future component initialization.
 */
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		if (CoffeeContext.isRegisteredNamespace(uri))
			return;

		CoffeeResourceLoader resourceLoader = CoffeeResourceLoader.getInstance();
		String className = String.format("%s.ComponentFactory",
			Util.join(
				uri.replace("urn:", "").split(":"), "."));
		IComponentFactory factory = (IComponentFactory)resourceLoader.instanceForName(className);

		if (Util.isNull(factory))
			throw new NullPointerException("The 'factory' object is null: Can't find components factory.");

		CoffeeContext.registerNamespace(uri, factory);
	}

	@Override
/**
 * Creates the current element and set its attributes. At same time it
 * stores the current component at child hierarchy.
 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		extractTextContentBeforeNesting();
		
		try {
			IComponentFactory factory = CoffeeContext.getComponentFactory(uri);
			IComponent newComponent = factory.newComponent(localName, context);

			if (Util.isNull(newComponent))
				throw new SAXException("Can't parse the unknown element '" + localName + "'");

			for (int i=0; i<attributes.getLength();i++)
				newComponent.setAttribute(
						attributes.getQName(i),
						attributes.getValue(i)
					);

			if (!Util.isNull(currentComponent)) {
				newComponent.setParent(currentComponent);
				currentComponent.addChild(newComponent);
			} else
				rootComponent = newComponent;
			currentComponent = newComponent;

		} catch (InstantiationException e) {
			throw new SAXException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new SAXException(e.getMessage(), e);
		}
	}

/**
 * Before nesting the next components, it's needed to save some text nodes
 * to be rendered correctly in the HTML web page.
 */
	public void extractTextContentBeforeNesting() {
		String string = textContent.toString();
		if (!Util.isNull(currentComponent)
		&&  !string.replaceAll("[\\n\\r]", "").trim().isEmpty())
			currentComponent.addChild(new TextNode(string));
		textContent.delete(0, textContent.length());
	}

	@Override
/**
 * After parse a child element it stores the parentChild to the currentComponent
 * and sets the textContent, if any.
 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		currentComponent.setTextContent(textContent.toString());
		currentComponent.bind();
		textContent.delete(0, textContent.length());
		currentComponent = currentComponent.getParent();
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		textContent.append(ch, start, length);
	}

	public void setRootComponent(IComponent rootComponent) {
		this.rootComponent = rootComponent;
	}

	public IComponent getRootComponent() {
		return rootComponent;
	}

	public void setContext(CoffeeContext context) {
		this.context = context;
	}

	public CoffeeContext getContext() {
		return context;
	}
	
	
}
