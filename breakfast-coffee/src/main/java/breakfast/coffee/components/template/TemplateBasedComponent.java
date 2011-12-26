package breakfast.coffee.components.template;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import breakfast.coffee.Cafeteria;
import breakfast.coffee.CoffeeContext;
import breakfast.coffee.components.AbstractComponent;
import breakfast.coffee.components.IComponent;
import breakfast.coffee.loader.CoffeeResourceLoader;

public class TemplateBasedComponent extends AbstractComponent {

	private String template;
	private String componentName;

	@Override
	public void configure() {
		for (String key : getAttributeKeys())
			set(key, getAttributes().get(key));
	}

	@Override
	public void render() throws IOException {
		try {
			HolderComponent holder = new HolderComponent();
			holder.setChildren(getChildren());
			CoffeeContext coffeeContext = getCoffeeContext();
			coffeeContext.put(TemplateBasedComponentChildren.CHILDREN, holder);

			CoffeeResourceLoader resourceLoader = Cafeteria
					.getResourceLoader(coffeeContext);
			IComponent compiledTemplate = resourceLoader.compile(getTemplate(),
					coffeeContext);
			compiledTemplate.render();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}

	public void set(String name, Object value) {
		System.out.println(getComponentName() + ":" + name + "," + value);
		getCoffeeContext().put(getComponentName() + ":" + name, value);
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentName() {
		return componentName;
	}

}