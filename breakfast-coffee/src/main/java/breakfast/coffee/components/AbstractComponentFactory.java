/**
 * Copyright 2011 Miere Liniel Teixeira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package breakfast.coffee.components;

import java.util.HashMap;
import java.util.Map;

import breakfast.coffee.CoffeeContext;
import breakfast.coffee.util.Util;


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
		return component;
	}

}
