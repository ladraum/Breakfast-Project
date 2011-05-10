package coffee.components.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coffee.components.IComponent;
import coffee.components.IComponentFactory;
import coffee.core.CoffeeContext;

public class HtmlComponentFactory implements IComponentFactory {
	
	private Map<String, Class<? extends IComponent>> components;
	private List<String> selfCloseableComponents;
	
	public HtmlComponentFactory() {
		components = new HashMap<String, Class<? extends IComponent>>();
		components.put("input", Input.class);
		components.put("select", Select.class);
		components.put("textarea", TextArea.class);
		components.put("html", Html.class);
		components.put("link", Link.class);
		components.put("img", Image.class);
		components.put("a", Anchor.class);
		components.put("script", Script.class);
		
		selfCloseableComponents = new ArrayList<String>();
		selfCloseableComponents.add("br");
		selfCloseableComponents.add("meta");
	}

	@Override
	public IComponent newComponent(String name, CoffeeContext context) throws InstantiationException, IllegalAccessException {
		if (components.containsKey(name)) {
			IComponent newInstance = components.get(name).newInstance();
			newInstance.setCoffeeContext(context);
			return newInstance;
		}

		HtmlComponent component = new HtmlComponent();
		component.setComponentName(name);
		component.setCoffeeContext(context);
		component.setSelfCloseable(
			selfCloseableComponents.contains(name)
		);

		return component;
	}

}
