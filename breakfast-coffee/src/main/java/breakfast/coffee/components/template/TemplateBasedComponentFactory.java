package breakfast.coffee.components.template;

import java.util.HashMap;
import java.util.Map;

import breakfast.coffee.CoffeeContext;
import breakfast.coffee.components.IComponent;
import breakfast.coffee.components.IComponentFactory;
import breakfast.coffee.components.template.TemplateBasedComponent;
import breakfast.coffee.util.Util;

public abstract class TemplateBasedComponentFactory implements IComponentFactory {

	protected Map<String, Class<? extends IComponent>> components;
	private Class<? extends TemplateBasedComponent> defaultComponent;

	public TemplateBasedComponentFactory() {
		defaultComponent = TemplateBasedComponent.class;
		components = new HashMap<String, Class<? extends IComponent>>();
		configure();
	}

	public abstract void configure();

	/**
	 * Registers a component and its respective class.
	 * @param componentName
	 * @param componentClass
	 */
	public void register(String componentName, Class<? extends IComponent> componentClass) {
		components.put(componentName, componentClass);
	}

	@Override
	public IComponent newComponent(String name, CoffeeContext context)
			throws InstantiationException, IllegalAccessException {
		TemplateBasedComponent component = null;
		
		Class<? extends IComponent> clazz = components.get(name);
		if (Util.isNull(clazz))
			clazz = defaultComponent;

		component = (TemplateBasedComponent)clazz.newInstance();
		component.setTemplate(name);
		component.setComponentName(name);
		component.setCoffeeContext(context);
		return component;
	}

	public void setDefaultComponent(Class<? extends TemplateBasedComponent> defaultComponent) {
		this.defaultComponent = defaultComponent;
	}

	public Class<? extends TemplateBasedComponent> getDefaultComponent() {
		return defaultComponent;
	}

}