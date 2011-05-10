package coffee.components.template;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.binding.CoffeeBinder;
import coffee.components.AbstractComponent;
import coffee.components.IComponent;
import coffee.core.CoffeeContext;
import coffee.util.Util;

public class Var extends AbstractComponent {

	private String name;

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		Object definedValue = getValue();

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
