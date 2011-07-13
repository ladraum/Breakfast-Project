package coffee.core.components.template;

import java.io.IOException;
import java.util.Collection;

import coffee.core.binding.CoffeeBinder;
import coffee.core.components.AbstractComponent;
import coffee.core.components.IComponent;

public class Foreach extends AbstractComponent {
	
	private Collection<?> values;
	private String var;

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		for (Object value : values) {
			coffeeContext.put(var, value);
			for (IComponent child : getChildren()) {
				child.setCoffeeContext(coffeeContext);
				child.render();
			}
		}
	}
	
	public void setValues(String values) {
		this.values = (Collection<?>)CoffeeBinder.getValue(
							values, getCoffeeContext());
	}

	public void setValues(Collection<?> values) {
		this.values = values;
	}

	public Collection<?> getValues() {
		return values;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getVar() {
		return var;
	}

}
