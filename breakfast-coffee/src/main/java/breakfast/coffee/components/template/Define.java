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
package breakfast.coffee.components.template;

import java.io.IOException;

import breakfast.coffee.CoffeeContext;
import breakfast.coffee.binding.CoffeeBinder;
import breakfast.coffee.components.AbstractComponent;


public class Define extends AbstractComponent {

	@Override
	public void configure() {
		String name = getAttributeAsString("name");
		coffeeContext.put(CoffeeContext.COFFEE_COMPONENTS_TEMPLATE_VALUE + name, getDefinedValue());
	}

	@Override
	public void render() throws IOException {}
	
	public Object getDefinedValue() {
		if (getNumChildren() == 0)
			return CoffeeBinder.getValue(getAttributeAsString("value"), coffeeContext);

		HolderComponent component = new HolderComponent();
		component.setCoffeeContext(getCoffeeContext());
		component.setChildren(getChildren());
		return component;
	}

}
