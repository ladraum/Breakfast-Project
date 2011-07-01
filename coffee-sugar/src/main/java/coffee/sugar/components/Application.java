package coffee.sugar.components;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import coffee.core.CoffeeContext;
import coffee.core.CoffeeResourceLoader;
import coffee.core.components.IComponent;
import coffee.core.components.xhtml.XHtmlComponent;
import coffee.core.util.Util;
import coffee.sugar.Component;

public class Application extends Component {

	private String template;
	private String onload;
	private String httpMethod;

	@Override
	public void configure() {
		if (!Util.isNull(onload))
			defineOnLoadMethod();
	}

	@Override
	public void render() throws IOException {
		if (!Util.isNull(template))
			renderApplicationTemplate();
	}

	public void renderApplicationTemplate() {
		try {
			CoffeeContext context = getCoffeeContext();

			XHtmlComponent form = new XHtmlComponent("form");
			form.setAttribute("class", "Application");
			form.setAttribute("method", getHttpMethod());
			form.setAttribute("onsubmit", "return application.getMethod('validate')(event);");
			form.setAttribute("style", getStyleDefinition());
			form.setCoffeeContext(getCoffeeContext());
			form.setChildren(getChildren());
			context.put(ApplicationBody.SUGAR_APPLICATION_BODY, form);
			
			CoffeeResourceLoader resourceLoader = CoffeeResourceLoader.getInstance();
			IComponent compiledTemplate = resourceLoader.compile(template, context);
			compiledTemplate.render();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public void defineOnLoadMethod() {
		getCoffeeContext().put(ApplicationResources.SUGAR_JS_ON_LOAD_EVENT, onload);
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setOnload(String onLoad) {
		getCoffeeContext().put(ApplicationResources.SUGAR_JS_ON_LOAD_EVENT, onLoad);
		this.onload = onLoad;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getHttpMethod() {
		if (Util.isNull(httpMethod))
			return "GET";
		return httpMethod.toUpperCase();
	}

}
