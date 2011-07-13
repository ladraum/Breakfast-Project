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
package coffee.core.components.template;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.CoffeeContext;
import coffee.core.binding.CoffeeBinder;
import coffee.core.components.AbstractComponent;
import coffee.core.components.IComponent;
import coffee.core.util.Util;

public class Var extends AbstractComponent {

	private String name;

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		Object definedValue = getValue();
		
		if (Util.isNull(definedValue))
			return;

		if (IComponent.class.isInstance(definedValue)) {
			((IComponent)definedValue).render();
			return;
		}

		PrintWriter writer = coffeeContext.getResponse().getWriter();
		Object value = CoffeeBinder.getValue(definedValue.toString(), getCoffeeContext());

		if (!Util.isNull(value))
			writer.append(value.toString());
	}

	public Object getValue() {
		return getCoffeeContext().get(CoffeeContext.COFFEE_COMPONENTS_TEMPLATE_VALUE + name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
