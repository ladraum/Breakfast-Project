package coffee.core.components.template;

import java.io.IOException;
import java.util.Collection;

import coffee.core.binding.CoffeeBinder;
import coffee.core.components.AbstractComponent;
import coffee.core.components.IComponent;
import coffee.core.util.Util;

public class Foreach extends AbstractComponent {

	private String values;
	private String var;

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		Object collection = CoffeeBinder.getValue(
						this.values, getCoffeeContext());

		Collection<?> values = (Collection<?>)collection;
		if (!Util.isNull(values))
			for (Object value : values) {
				coffeeContext.put(var, value);
				for (IComponent child : getChildren()) {
					child.setCoffeeContext(coffeeContext);
					child.render();
				}
			}
	}

	public void setValues(String values) {
		this.values = values;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getVar() {
		return var;
	}
}
