package coffee.components;

import java.util.HashMap;
import java.util.Map;

import coffee.core.CoffeeContext;
import coffee.util.Util;

/**
 * Abstract implementation of IComponentFactory. This implementation focus on
 * provide a easy way to create custom components.
 * 
 * @author Miere Liniel Teixeira
 */
public abstract class AbstractComponentFactory implements IComponentFactory {

	private Map<String, Class<? extends IComponent>> components;

	public AbstractComponentFactory() {
		components = new HashMap<String, Class<? extends IComponent>>();
		configure();
	}

/**
 * Configure the component factory.
 */
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
	public IComponent newComponent(String name, CoffeeContext context) throws InstantiationException, IllegalAccessException {
		Class<? extends IComponent> clazz = components.get(name);
		
		if (Util.isNull(clazz))
			throw new InstantiationException("Can't find '" + name + "' component implementation.");
		
		IComponent component = clazz.newInstance();
		component.setCoffeeContext(context);
		if (Util.isNull(component.getComponentName()))
			component.setComponentName(name);
		return component;
	}

}
